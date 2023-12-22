package com.aurine.cloudx.edge.sync.common.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.lang.reflect.Method;


/**
 * @Auther: CJW
 * @Date: 2019/8/27 10:28
 * @Description:
 */

@Configuration
@EnableCaching // 开启缓存支持
@ConfigurationProperties(prefix = "spring.redis") //指明配置节点
public class CommonRedisConfig extends CachingConfigurerSupport {


//	// Redis服务器地址
//	@Value("${spring.redis.host}")
//	private String host;
//	// Redis服务器连接端口
////	@Value("${spring.redis.port}")
////	private Integer port;
//	// Redis数据库索引（默认为0）
//	@Value("${spring.redis.database}")
//	private Integer database;
//	// Redis服务器连接密码（默认为空）
//
//	@Value("${spring.redis.password}")
    private String password;
//	// 连接超时时间（毫秒）
//	@Value("${spring.redis.timeout}")
//	private Integer timeout;
//
//	// 连接池最大连接数（使用负值表示没有限制）
//	@Value("${spring.redis.lettuce.pool.max-active}")
//	private Integer maxTotal;
//	// 连接池最大阻塞等待时间（使用负值表示没有限制）
//	@Value("${spring.redis.lettuce.pool.max-wait}")
//	private Integer maxWait;
//	// 连接池中的最大空闲连接
//	@Value("${spring.redis.lettuce.pool.max-idle}")
//	private Integer maxIdle;
//	// 连接池中的最小空闲连接
//	@Value("${spring.redis.lettuce.pool.min-idle}")
//	private Integer minIdle;
//	// 关闭超时时间
//	@Value("${spring.redis.lettuce.shutdown-timeout}")
//	private Integer shutdown;


    @Bean
    public KeyGenerator keyGenerator() {
        return new KeyGenerator() {
            @Override
            public Object generate(Object target, Method method, Object... params) {
                StringBuffer sb = new StringBuffer();
                sb.append(target.getClass().getName());
                sb.append(method.getName());
                for (Object obj : params) {
                    sb.append(obj.toString());
                }
                return sb.toString();
            }
        };
    }


//	// 缓存管理器
//	@Bean
//	public CacheManager cacheManager() {
//		//以锁写入的方式创建RedisCacheWriter对象
//		RedisCacheWriter writer = RedisCacheWriter.lockingRedisCacheWriter(getConnectionFactory());
//        /*
//        设置CacheManager的Value序列化方式为JdkSerializationRedisSerializer,
//        但其实RedisCacheConfiguration默认就是使用
//        StringRedisSerializer序列化key，
//        JdkSerializationRedisSerializer序列化value,
//        所以以下注释代码就是默认实现，没必要写，直接注释掉
//         */
//		// RedisSerializationContext.SerializationPair pair = RedisSerializationContext.SerializationPair.fromSerializer(new JdkSerializationRedisSerializer(this.getClass().getClassLoader()));
//		// RedisCacheConfiguration conf = RedisCacheConfiguration.defaultCacheConfig().serializeValuesWith(pair);
//		//创建默认缓存配置对象
//		RedisCacheConfiguration config = RedisCacheConfiguration.defaultCacheConfig();
//		RedisCacheManager cacheManager = new RedisCacheManager(writer, config);
//		return cacheManager;
//	}


    /**
     * RedisTemplate配置
     */
    @Bean
    @Primary
    public RedisTemplate<String, Object> redisTemplateCloudx(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        GenericJackson2JsonRedisSerializer genericJackson2JsonRedisSerializer = new GenericJackson2JsonRedisSerializer(objectMapper);
        redisTemplate.setValueSerializer(genericJackson2JsonRedisSerializer);
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashValueSerializer(new StringRedisSerializer());
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.afterPropertiesSet();
        return redisTemplate;
    }

    /**
     * 获取缓存连接
     *
     * @return
     */
//	@Bean
//	public RedisConnectionFactory getConnectionFactory() {
//		System.out.println("get cache connect");
//		//单机模式
//		RedisStandaloneConfiguration configuration = new RedisStandaloneConfiguration();
//		configuration.setHostName(host);
//		configuration.setPort(6379);
//		configuration.setDatabase(database);
//		configuration.setPassword(password);
//		//哨兵模式
//		//RedisSentinelConfiguration configuration1 = new RedisSentinelConfiguration();
//		//集群模式
//		//RedisClusterConfiguration configuration2 = new RedisClusterConfiguration();
//		LettuceConnectionFactory factory = new LettuceConnectionFactory(configuration);
//		//factory.setShareNativeConnection(false);//是否允许多个线程操作共用同一个缓存连接，默认true，false时每个操作都将开辟新的连接
//		return factory;
//	}

    /**
     * 获取缓存连接池
     *
     * @return
     */
//	@Bean
//	public LettucePoolingClientConfiguration getPoolConfig() {
//		System.out.println("getPoolConfig");
//		GenericObjectPoolConfig config = new GenericObjectPoolConfig();
//		config.setMaxTotal(maxTotal);
//		config.setMaxWaitMillis(maxWait);
//		config.setMaxIdle(maxIdle);
//		config.setMinIdle(minIdle);
//		LettucePoolingClientConfiguration pool = LettucePoolingClientConfiguration.builder()
//				.poolConfig(config)
//				.commandTimeout(Duration.ofMillis(timeout))
//				.shutdownTimeout(Duration.ofMillis(shutdown))
//				.build();
//		return pool;
//	}

}
