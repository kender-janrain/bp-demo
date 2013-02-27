package data

import com.redis.RedisClient

trait RedisSupport {
  val client = new RedisClient("localhost", 6379)
}
