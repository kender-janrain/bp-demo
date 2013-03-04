package data

import com.redis.RedisClient

object RedisSupport {
	val client = new RedisClient("localhost", 6379)
}

trait RedisSupport {
  def client = RedisSupport.client
}
