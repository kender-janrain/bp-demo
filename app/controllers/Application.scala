package controllers

import play.api.mvc._
import play.api.libs.ws.WS
import concurrent.ExecutionContext.Implicits.global
import data.{AuthInfo, AuthInfoCache}
import play.api.libs.json.{JsObject, JsArray, Json}
import sun.security.krb5.Realm
import com.ning.http.client.Realm.AuthScheme
import play.Logger


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

	def w1 = Action { implicit request =>
		Async {
			val (channelId, messageType, payload) = request.body match {
				case AnyContentAsFormUrlEncoded(form) => {
					val channelId = form("channelId").head
					val messageType = form("messageType").head
					val payload = Json.obj(form
						.filter(f => !Set("channelId", "messageType").contains(f._1))
						.map(f => (f._1, Json.toJsFieldJsValueWrapper(f._2.mkString(","))))
						.toSeq: _*)
					(channelId, messageType, payload)
				}
				case _ => sys.error("unhandled input")
			}
			Logger.debug("w1 publishing to %s".format(channelId))
			val data = Json.arr(
				Json.obj(
					"source" -> routes.Application.w1().absoluteURL(),
					"type" -> messageType,
					"sticky" -> false,
					"payload" -> payload
				)
			)
			WS.url(channelId).withAuth("kender1", "kender1", AuthScheme.BASIC).post(data).map { channelResponse =>
				Ok("GRAND!")
			}
		}
	}

	def w2 = Action { implicit request =>
		Async {
			val (channelId, messageType, payload) = request.body match {
				case AnyContentAsFormUrlEncoded(form) => {
					val channelId = form("channelId").head
					val messageType = form("messageType").head
					val payload = Json.obj(form
						.filter(f => !Set("channelId", "messageType").contains(f._1))
						.map(f => (f._1, Json.toJsFieldJsValueWrapper(f._2.mkString(","))))
						.toSeq: _*)
					(channelId, messageType, payload)
				}
				case _ => sys.error("unhandled input")
			}
			Logger.debug("w2 publishing to %s".format(channelId))
			val data = Json.arr(
				Json.obj(
					"source" -> routes.Application.w2().absoluteURL(),
					"type" -> messageType,
					"sticky" -> false,
					"payload" -> payload
				)
			)
			WS.url(channelId).withAuth("kender1", "kender1", AuthScheme.BASIC).post(data).map { channelResponse =>
				Ok("JOLLY!")
			}
		}
	}
}