package controllers

import com.ning.http.client.Realm.AuthScheme
import concurrent.ExecutionContext.Implicits.global
import data.{AuthInfo, AuthInfoCache}
import play.Logger
import play.api.libs.json.Json
import play.api.libs.ws.WS
import play.api.mvc._

object Application extends Controller {

	def index = Action { request =>
		request.cookies.get("token").map(_.value).flatMap { token =>
			AuthInfoCache.refresh(token)
			AuthInfoCache.retrieve(token)
		} match {
			case Some(authInfo) => {
				Ok(views.html.index(AuthInfo(Json.parse(authInfo))))
			}
			case _ => Ok(views.html.login())
		}
	}

	def token = Action { implicit request =>
		Async {
			val token = request.body match {
				case AnyContentAsFormUrlEncoded(data) => {
					data.find(_._1 == "token").map(_._2.head).getOrElse(throw new RuntimeException("no token"))
				}
				case _ => sys.error("unhandled request for token")
			}
			WS.url("https://rpxnow.com/api/v2/auth_info").post(Map(
				"apiKey" -> Seq("a8d89749b99b0a1695ae71d92b91cc1c1c3f5813"),
				"token" -> Seq(token)
			)).map { response =>
				AuthInfoCache.store(token, response.json.toString())
				println(response.json)
				Redirect("/").withCookies(Cookie("token", token))
			}
		}
	}

	def w1_post = Action { implicit request =>
		Async {
			val (bus, channel, messageType, payload) = request.body match {
				case AnyContentAsFormUrlEncoded(data) => {
					def v(name: String) = data.get(name).map(_.head).getOrElse(sys.error("%s required".format(name)))
					val fixedKeys = Set("bus", "channel", "type")
					val payload = data.filter(f => !fixedKeys.contains(f._1)).map {
						case (key, value) => key -> value.head
					}.toMap
					(v("bus"), v("channel"), v("type"), payload)
				}
				case _ => sys.error("form data required")
			}
			for {
				token <- BP2.accessToken
				message <- BP2.postMessage(token, bus, channel, messageType, payload)
			} yield Ok(message.toString)
		}
	}

	def w2_get = Action { implicit request =>
		Async {
			val messageUrl = request.body match {
				case AnyContentAsFormUrlEncoded(data) => {
					data.get("messageUrl").map(_.head).getOrElse(sys.error("messageUrl required"))
				}
				case _ => sys.error("form data required")
			}
			for {
				token <- BP2.accessToken
				message <- BP2.getMessage(token, messageUrl)
			} yield Ok(message.payload("text"))
		}

	}
}