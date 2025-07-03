@TestConfiguration
class EmbeddedRedisConfig {
    
    private var redisServer: RedisServer? = null
    
    @PostConstruct
    fun startRedis() {
        redisServer = RedisServer(6379)
        redisServer?.start()
    }
    
    @PreDestroy
    fun stopRedis() {
        redisServer?.stop()
    }
}
