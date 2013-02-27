package data

object AuthInfoCache extends RedisSupport {
	def refresh(key: String) {
		client.expire("authinfo_" + key, 60 * 60 * 24)
	}

	def store(key: String, value: String) {
		client.set("authinfo_" + key, value)
		refresh(key)
	}

	def retrieve(key: String): Option[String] = {
		client.get[String]("authinfo_" + key)
	}
}
