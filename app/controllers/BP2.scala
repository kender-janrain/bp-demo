package controllers

import com.janrain.bp.v2.Backplane2
import data.RedisSupport
import spray.json.DefaultJsonProtocol
import play.api.libs.concurrent.Akka
import concurrent.Future
import concurrent.ExecutionContext.Implicits.global


object BP2 extends Backplane2 with RedisSupport with DefaultJsonProtocol {
	import play.api.Play
	implicit def system = Akka.system(Play.current)
	def config = Play.configuration(Play.current)

	val admin = config.getString("bp.admin.user").getOrElse("bpadmin")
	val secret = config.getString("bp.admin.secret").getOrElse("bpadmin")
	val host = config.getString("bp.host").getOrElse("localhost")
	val port = config.getInt("bp.port").getOrElse(9000)

	val clientId = "client1"
	val clientSecret = "client1"

	def accessToken: Future[String] = {
		val accessTokenKey = "bpAccessToken"

		def getAccessToken: Future[String] = for {
			auth <- token(clientId, clientSecret)
			token <- Future {
				val accessToken = auth.accessToken
				client.set(accessTokenKey, accessToken)
				for (expiresIn <- auth.expiresIn) {
					client.expire(accessTokenKey, expiresIn)
				}
				accessToken
			}
		} yield token

		Future(client.get(accessTokenKey)) flatMap { _ match {
			case Some(token) => Future(token)
			case _ => getAccessToken
		}}
	}
}
