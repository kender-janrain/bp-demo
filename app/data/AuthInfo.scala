package data

import play.api.libs.json.JsValue

object AuthInfo {
	def apply(json: JsValue): AuthInfo = AuthInfo(
		stat = (json \ "stat").as[String],
		profile = AuthInfoProfile(
			displayName = (json \ "profile" \ "displayName").as[String],
			preferredUsername = (json \ "profile" \ "preferredUsername").as[String]
		)
	)
}
case class AuthInfo(stat: String, profile: AuthInfoProfile)

case class AuthInfoProfile(displayName: String, preferredUsername: String)
