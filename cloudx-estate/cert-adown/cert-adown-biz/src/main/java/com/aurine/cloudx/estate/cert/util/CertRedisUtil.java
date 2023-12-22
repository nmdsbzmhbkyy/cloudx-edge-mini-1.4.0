package com.aurine.cloudx.estate.cert.util;

import com.aurine.cloudx.estate.cert.entity.SysCertAdownRequest;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;


/**
 * @description: Redis工具包
 * @author: wangwei
 * @date: 2021/12/16 9:02
 **/
@Component
public class CertRedisUtil implements ApplicationContextAware {
	private static RedisTemplate redisTemplate;

	private static final ThreadLocal<String> lockValue = new ThreadLocal<>();

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {

//        redisTemplate = applicationContext.getBean(RedisTemplate.class);
		redisTemplate = (RedisTemplate) applicationContext.getBean("redisTemplate");
	}

	/**
	 * 创建锁
	 *
	 * @param key         锁的Key
	 * @param releaseTime 锁过期时间 防止死锁
	 * @return
	 */
//	public static boolean lock(String key, long releaseTime) {
//		// 尝试获取锁
//		Boolean boo = redisTemplate.opsForValue().setIfAbsent(key, 0, releaseTime, TimeUnit.SECONDS);
//		// 判断结果
//		return boo != null && boo;
//	}


	/**
	 * 创建锁
	 * @param key
	 * @param releaseTime
	 * @return
	 */
	public static boolean lock(String key, long releaseTime) {
		// 生成一个唯一的UUID作为锁的值
		String value = UUID.randomUUID().toString();

		// 尝试获取锁
		Boolean success = redisTemplate.opsForValue().setIfAbsent(key, value, releaseTime, TimeUnit.MILLISECONDS);

		// 如果成功获取锁，将该锁的值存储在ThreadLocal变量中
		if (success != null && success) {
			lockValue.set(value);
			return true;
		}

		return false;
	}

	/**
	 * 创建同步锁
	 *
	 * @param key         锁的Key
	 * @param releaseTime 锁过期时间 防止死锁
	 * @return
	 */
	public static void syncLock(String key, long releaseTime) {
		boolean locked = false;
		do {
			locked = lock(key, releaseTime);
		} while (locked);
	}


	/**
	 * 根据key删除锁
	 *
	 * @param key
	 */
//	public static void unLock(String key) {
//		redisTemplate.delete(key);
//	}


	/**
	 *
	 * @param key
	 */
	public static void unLock(String key) {
		// 获取当前线程的锁值
		String value = lockValue.get();

		// 如果当前线程没有锁值，直接返回
		if (value == null) {
			return;
		}

		// 使用Lua脚本来确保只有锁的持有者才能解锁
		String script = "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end";
		RedisScript<Long> redisScript = new DefaultRedisScript<>(script, Long.class);

		// 执行解锁操作
		redisTemplate.execute(redisScript, Collections.singletonList(key), value);

		// 清除存储在ThreadLocal变量中的锁值
		lockValue.remove();
	}

	/**
	 * 判断key是否存在
	 *
	 * @param key 键
	 * @return true 存在 false不存在
	 */
	public static boolean hasKey(String key) {
		try {
			return redisTemplate.hasKey(key);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * 指定缓存失效时间
	 *
	 * @param key  键
	 * @param time 时间(秒)
	 * @return
	 */
	public static boolean expire(String key, long time) {
		try {
			if (time > 0) {
				redisTemplate.expire(key, time, TimeUnit.SECONDS);
			}
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * 根据key 获取过期时间
	 *
	 * @param key 键 不能为null
	 * @return 时间(秒) 返回0代表为永久有效
	 */
	public static long getExpire(String key) {
		return redisTemplate.getExpire(key, TimeUnit.SECONDS);
	}


	/**
	 * 删除缓存
	 *
	 * @param key 可以传一个值 或多个
	 */
	@SuppressWarnings("unchecked")
	public static void del(String... key) {
		if (key != null && key.length > 0) {
			if (key.length == 1) {
				redisTemplate.delete(key[0]);
			} else {
				redisTemplate.delete(CollectionUtils.arrayToList(key));
			}
		}
	}

	/**
	 * 普通缓存获取
	 *
	 * @param key 键
	 * @return 值
	 */
	public static Object get(String key) {
		return key == null ? null : redisTemplate.opsForValue().get(key);
	}

	/**
	 * 普通缓存放入
	 *
	 * @param key   键
	 * @param value 值
	 * @return true成功 false失败
	 */
	public static boolean set(String key, Object value) {
		try {
			redisTemplate.opsForValue().set(key, value);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * 普通缓存放入并设置时间
	 *
	 * @param key   键
	 * @param value 值
	 * @param time  时间(秒) time要大于0 如果time小于等于0 将设置无限期
	 * @return true成功 false 失败
	 */
	public static boolean set(String key, Object value, long time) {
		try {
			if (time > 0) {
				redisTemplate.opsForValue().set(key, value, time, TimeUnit.SECONDS);
			} else {
				set(key, value);
			}
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * 递增
	 *
	 * @param key   键
	 * @param delta 要增加几(大于0)
	 * @return
	 */
	public static long incr(String key, long delta) {
		if (delta < 0) {
			throw new RuntimeException("递增因子必须大于0");
		}
		return redisTemplate.opsForValue().increment(key, delta);
	}

	/**
	 * 递减
	 *
	 * @param key   键
	 * @param delta 要减少几(小于0)
	 * @return
	 */
	public static long decr(String key, long delta) {
		if (delta < 0) {
			throw new RuntimeException("递减因子必须大于0");
		}
		return redisTemplate.opsForValue().increment(key, -delta);
	}

	/**
	 * HashGet
	 *
	 * @param key  键 不能为null
	 * @param item 项 不能为null
	 * @return 值
	 */
	public static Object hget(String key, String item) {
		return redisTemplate.opsForHash().get(key, item);
	}

	/**
	 * 获取hashKey对应的所有键值
	 *
	 * @param key 键
	 * @return 对应的多个键值
	 */
	public static Map<Object, Object> hmget(String key) {
		return redisTemplate.opsForHash().entries(key);
	}

	/**
	 * HashSet
	 *
	 * @param key 键
	 * @param map 对应多个键值
	 * @return true 成功 false 失败
	 */
	public static boolean hmset(String key, Map<String, Object> map) {
		try {
			redisTemplate.opsForHash().putAll(key, map);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * HashSet 并设置时间
	 *
	 * @param key  键
	 * @param map  对应多个键值
	 * @param time 时间(秒)
	 * @return true成功 false失败
	 */
	public static boolean hmset(String key, Map<String, Object> map, long time) {
		try {
			redisTemplate.opsForHash().putAll(key, map);
			if (time > 0) {
				expire(key, time);
			}
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * 向一张hash表中放入数据,如果不存在将创建
	 *
	 * @param key   键
	 * @param item  项
	 * @param value 值
	 * @return true 成功 false失败
	 */
	public static boolean hset(String key, String item, Object value) {
		try {
			redisTemplate.opsForHash().put(key, item, value);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * 向一张hash表中放入数据,如果不存在将创建
	 *
	 * @param key   键
	 * @param item  项
	 * @param value 值
	 * @param time  时间(秒) 注意:如果已存在的hash表有时间,这里将会替换原有的时间
	 * @return true 成功 false失败
	 */
	public static boolean hset(String key, String item, Object value, long time) {
		try {
			redisTemplate.opsForHash().put(key, item, value);
			if (time > 0) {
				expire(key, time);
			}
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * 删除hash表中的值
	 *
	 * @param key  键 不能为null
	 * @param item 项 可以使多个 不能为null
	 */
	public static void hdel(String key, Object... item) {
		redisTemplate.opsForHash().delete(key, item);
	}

	/**
	 * 判断hash表中是否有该项的值
	 *
	 * @param key  键 不能为null
	 * @param item 项 不能为null
	 * @return true 存在 false不存在
	 */
	public static boolean hHasKey(String key, String item) {
		return redisTemplate.opsForHash().hasKey(key, item);
	}

	/**
	 * hash递增 如果不存在,就会创建一个 并把新增后的值返回
	 *
	 * @param key  键
	 * @param item 项
	 * @param by   要增加几(大于0)
	 * @return
	 */
	public static double hincr(String key, String item, double by) {
		return redisTemplate.opsForHash().increment(key, item, by);
	}

	/**
	 * hash递减
	 *
	 * @param key  键
	 * @param item 项
	 * @param by   要减少记(小于0)
	 * @return
	 */
	public static double hdecr(String key, String item, double by) {
		return redisTemplate.opsForHash().increment(key, item, -by);
	}

	/**
	 * 获取大小
	 *
	 * @param key
	 * @return
	 */
	public static long hSize(String key) {
		return redisTemplate.opsForHash().size(key);
	}

	/**
	 * 根据key获取Set中的所有值
	 *
	 * @param key 键
	 * @return
	 */
	public static Set<Object> sGet(String key) {
		try {
			return redisTemplate.opsForSet().members(key);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 根据value从一个set中查询,是否存在
	 *
	 * @param key   键
	 * @param value 值
	 * @return true 存在 false不存在
	 */
	public static boolean sHasKey(String key, Object value) {
		try {
			return redisTemplate.opsForSet().isMember(key, value);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * 将数据放入set缓存
	 *
	 * @param key    键
	 * @param values 值 可以是多个
	 * @return 成功个数
	 */
	public static long sSet(String key, Object... values) {
		try {
			return redisTemplate.opsForSet().add(key, values);
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}

	/**
	 * 将set数据放入缓存
	 *
	 * @param key    键
	 * @param time   时间(秒)
	 * @param values 值 可以是多个
	 * @return 成功个数
	 */
	public static long sSetAndTime(String key, long time, Object... values) {
		try {
			Long count = redisTemplate.opsForSet().add(key, values);
			if (time > 0) {
				expire(key, time);
			}

			return count;
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}

	/**
	 * 获取set缓存的长度
	 *
	 * @param key 键
	 * @return
	 */
	public long sGetSetSize(String key) {
		try {
			return redisTemplate.opsForSet().size(key);
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}

	/**
	 * 移除值为value的
	 *
	 * @param key    键
	 * @param values 值 可以是多个
	 * @return 移除的个数
	 */
	public static long setRemove(String key, Object... values) {
		try {
			Long count = redisTemplate.opsForSet().remove(key, values);
			return count;
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}


	//===============================zset==================================


	/**
	 * 将数据放入set缓存
	 *
	 * @param key   键
	 * @param value 值
	 * @param score 分数值，可使用时间戳
	 * @return 是否成功
	 */
	public static boolean zSet(String key, Object value, Double score) {
		try {
			return redisTemplate.opsForZSet().add(key, value, score);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * 获取集合的元素, 从小到大排序, start开始位置, end结束位置
	 *
	 * @param key
	 * @param start 开始位置
	 * @param end   结束位置 -1 为最后一个成员
	 * @return
	 */
	public static Set<String> zRange(String key, long start, long end) {
		try {
			return redisTemplate.opsForZSet().range(key, start, end);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 获取集合的元素, 从大到小排序, start开始位置, end结束位置
	 *
	 * @param key
	 * @param start 开始位置
	 * @param end   结束位置 -1 为最后一个成员
	 * @return
	 */
	public static Set<String> zReverseRange(String key, long start, long end) {
		try {
			return redisTemplate.opsForZSet().reverseRange(key, start, end);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}


	/**
	 * 获取集合元素, 并且把score值也获取, 从小到大排序, start开始位置, end结束位置
	 *
	 * @param key
	 * @param start 开始score
	 * @param end   结束score
	 * @return
	 */
	public static Set<String>  zRangeWithScores(String key, double start, double end) {
		try {
			return redisTemplate.opsForZSet().rangeByScore(key, start, end);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}


	/**
	 * 获取集合的元素, 从大到小排序, 并返回score值, 从小到大排序, start开始位置, end结束位置
	 *
	 * @param key
	 * @param start 开始score
	 * @param end   结束score
	 * @return
	 */
	public static Set<ZSetOperations.TypedTuple<String>> zReverseRangeWithScores(String key, double start, double end) {
		try {
			return redisTemplate.opsForZSet().reverseRangeByScore(key, start, end);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}


	/**
	 * 返回元素在集合的排名,有序集合是按照元素的score值由小到大排列
	 *
	 * @param key
	 * @param value
	 * @return
	 */
	public static Long zRank(String key, Object value) {
		try {
			return redisTemplate.opsForZSet().rank(key, value);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 返回元素在集合的排名,按元素的score值由大到小排列
	 *
	 * @param key
	 * @param value
	 * @return
	 */
	public static Long zReverseRank(String key, Object value) {
		try {
			return redisTemplate.opsForZSet().reverseRank(key, value);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}


	/**
	 * 根据score值获取集合元素数量
	 *
	 * @param key
	 * @param min
	 * @param max
	 * @return
	 */
	public static Long zCount(String key, double min, double max) {
		try {
			return redisTemplate.opsForZSet().count(key, min, max);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 获取集合大小
	 *
	 * @param key
	 * @return
	 */
	public static Long zSize(String key) {
		try {
			return redisTemplate.opsForZSet().size(key);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 获取集合大小
	 *
	 * @param key
	 * @return
	 */
	public static Long zZCard(String key) {
		try {
			return redisTemplate.opsForZSet().zCard(key);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 获取集合中value元素的score值
	 *
	 * @param key
	 * @param value
	 * @return
	 */
	public static Double zScore(String key, Object value) {
		try {
			return redisTemplate.opsForZSet().score(key, value);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}


	/**
	 * 移除
	 *
	 * @param key
	 * @param values
	 * @return
	 */
	public static Long zRemove(String key, Object... values) {
		try {
			return redisTemplate.opsForZSet().remove(key, values);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 移除指定索引位置的成员
	 *
	 * @param key
	 * @param start
	 * @param end   结束位置 -1 为最后一个成员
	 * @return
	 */
	public static Long zRemoveRange(String key, long start, long end) {
		try {
			return redisTemplate.opsForZSet().removeRange(key, start, end);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}


	/**
	 * 根据指定的score值的范围来移除成员
	 *
	 * @param key
	 * @param min
	 * @param max
	 * @return
	 */
	public static Long zRemoveRangeByScore(String key, double min, double max) {
		try {
			return redisTemplate.opsForZSet().removeRangeByScore(key, min, max);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}


	/**
	 * 增加元素的score值，并返回增加后的值
	 *
	 * @param key
	 * @param value
	 * @param delta
	 * @return
	 */
	public static Double zIncrementScore(String key, String value, double delta) {
		try {
			return redisTemplate.opsForZSet().incrementScore(key, value, delta);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}


	/**
	 * 根据key删除
	 *
	 * @param key 键
	 * @return 移除的个数
	 */
	public static void delete(String key) {
		try {
			redisTemplate.delete(key);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	// ===============================list=================================


	/**
	 * 以原子性地从列表中移除并返回第一个元素
	 * @param key
	 * @return
	 */
	public static Object lPop(String key) {
		return redisTemplate.opsForList().leftPop(key);
	}
	/**
	 * 获取list缓存的内容
	 *
	 * @param key   键
	 * @param start 开始
	 * @param end   结束 0 到 -1代表所有值
	 * @return
	 */
	public static List<Object> lGet(String key, long start, long end) {
		try {
			return redisTemplate.opsForList().range(key, start, end);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 获取list缓存的长度
	 *
	 * @param key 键
	 * @return
	 */
	public static long lGetListSize(String key) {
		if (hasKey(key)) {
			return redisTemplate.opsForList().size(key);
		} else {
			return 0;
		}
//        try {
//
//        } catch (Exception e) {
//            return 0;
//        }
	}

	/**
	 * 通过索引 获取list中的值
	 *
	 * @param key   键
	 * @param index 索引 index>=0时， 0 表头，1 第二个元素，依次类推；index<0时，-1，表尾，-2倒数第二个元素，依次类推
	 * @return
	 */
	public static Object lGetIndex(String key, long index) {
		try {
			return redisTemplate.opsForList().index(key, index);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 将list放入缓存
	 *
	 * @param key   键
	 * @param value 值
	 * @return
	 */
	public static boolean lSet(String key, Object value) {
		try {
			redisTemplate.opsForList().rightPush(key, value);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * 将list放入缓存
	 *
	 * @param key   键
	 * @param value 值
	 * @param time  时间(秒)
	 * @return
	 */
	public static boolean lSet(String key, Object value, long time) {
		try {
			redisTemplate.opsForList().rightPush(key, value);
			if (time > 0) {
				expire(key, time);
			}
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * 将list放入缓存
	 *
	 * @param key   键
	 * @param value 值
	 * @return
	 */
	public static boolean lSetList(String key, List<SysCertAdownRequest> value) {
		try {
			redisTemplate.opsForList().rightPushAll(key, value);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * 将list放入缓存
	 *
	 * @param key   键
	 * @param value 值
	 * @param time  时间(秒)
	 * @return
	 */
	public static boolean lSetList(String key, List<SysCertAdownRequest> value, long time) {
		try {
			redisTemplate.opsForList().rightPushAll(key, value);
			if (time > 0) {
				expire(key, time);
			}
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * 根据索引修改list中的某条数据
	 *
	 * @param key   键
	 * @param index 索引
	 * @param value 值
	 * @return
	 */
	public static boolean lUpdateIndex(String key, long index, Object value) {
		try {
			redisTemplate.opsForList().set(key, index, value);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * 移除N个值为value
	 *
	 * @param key   键
	 * @param count 移除多少个
	 * @param value 值
	 * @return 移除的个数
	 */
	public static long lRemove(String key, long count, Object value) {
		try {
			Long remove = redisTemplate.opsForList().remove(key, count, value);
			return remove;
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}

	/**
	 * 模糊删除
	 *
	 * @param key 模糊键
	 * @return
	 */
	public static void delPattenKey(String key) {
		Set<String> keys = redisTemplate.keys(key);
		keys.forEach(x -> {
			redisTemplate.delete(x);
		});
	}
}
