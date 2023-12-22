package com.aurine.cloudx.edge.sync.common.utils;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;


@Component
public class RedisUtil implements ApplicationContextAware {

    private static RedisTemplate redisTemplateCloudx;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {

        redisTemplateCloudx = applicationContext.getBean("redisTemplate",RedisTemplate.class);
//        redisTemplateCloudx = (RedisTemplate) applicationContext.getBean("redisTemplateCloudx");
    }

    /**
     * 创建锁
     *
     * @param key         锁的Key
     * @param releaseTime 锁过期时间 防止死锁
     * @return
     */
    public static boolean lock(String key, long releaseTime) {
        // 尝试获取锁
        Boolean boo = redisTemplateCloudx.opsForValue().setIfAbsent(key, 0, releaseTime, TimeUnit.SECONDS);
        // 判断结果
        return boo != null && boo;
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
    public static void unLock(String key) {
        redisTemplateCloudx.delete(key);
    }


    /**
     * 判断key是否存在
     *
     * @param key 键
     * @return true 存在 false不存在
     */
    public static boolean hasKey(String key) {
        try {
            return redisTemplateCloudx.hasKey(key);
        } catch (Exception e) {
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
                redisTemplateCloudx.expire(key, time, TimeUnit.SECONDS);
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
        return redisTemplateCloudx.getExpire(key, TimeUnit.SECONDS);
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
                Boolean delete = redisTemplateCloudx.delete(key[0]);
            } else {
                redisTemplateCloudx.delete(CollectionUtils.arrayToList(key));
            }
        }
    }

    /**
     * 删除缓存
     *
     * @param key 可以传一个值 或多个
     */
    @SuppressWarnings("unchecked")
    public static Boolean del(String key) {
        if (key != null) {
            return redisTemplateCloudx.delete(key);
        }
        return false;
    }

    /**
     * 普通缓存获取
     *
     * @param key 键
     * @return 值
     */
    public static Object get(String key) {
        return key == null ? null : redisTemplateCloudx.opsForValue().get(key);
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
            redisTemplateCloudx.opsForValue().set(key, value);
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
                redisTemplateCloudx.opsForValue().set(key, value, time, TimeUnit.SECONDS);
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
        return redisTemplateCloudx.opsForValue().increment(key, delta);
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
        return redisTemplateCloudx.opsForValue().increment(key, -delta);
    }

    /**
     * HashGet
     *
     * @param key  键 不能为null
     * @param item 项 不能为null
     * @return 值
     */
    public static Object hget(String key, String item) {
        return redisTemplateCloudx.opsForHash().get(key, item);
    }

    /**
     * 获取hashKey对应的所有键值
     *
     * @param key 键
     * @return 对应的多个键值
     */
    public static Map<Object, Object> hmget(String key) {
        return redisTemplateCloudx.opsForHash().entries(key);
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
            redisTemplateCloudx.opsForHash().putAll(key, map);
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
            redisTemplateCloudx.opsForHash().putAll(key, map);
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
            redisTemplateCloudx.opsForHash().put(key, item, value);
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
            redisTemplateCloudx.opsForHash().put(key, item, value);
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
        redisTemplateCloudx.opsForHash().delete(key, item);
    }

    /**
     * 判断hash表中是否有该项的值
     *
     * @param key  键 不能为null
     * @param item 项 不能为null
     * @return true 存在 false不存在
     */
    public static boolean hHasKey(String key, String item) {
        return redisTemplateCloudx.opsForHash().hasKey(key, item);
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
        return redisTemplateCloudx.opsForHash().increment(key, item, by);
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
        return redisTemplateCloudx.opsForHash().increment(key, item, -by);
    }

    /**
     * 获取大小
     *
     * @param key
     * @return
     */
    public static long hSize(String key) {
        return redisTemplateCloudx.opsForHash().size(key);
    }

    /**
     * 根据key获取Set中的所有值
     *
     * @param key 键
     * @return
     */
    public static Set<Object> sGet(String key) {
        try {
            return redisTemplateCloudx.opsForSet().members(key);
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
            return redisTemplateCloudx.opsForSet().isMember(key, value);
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
            return redisTemplateCloudx.opsForSet().add(key, values);
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
            Long count = redisTemplateCloudx.opsForSet().add(key, values);
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
            return redisTemplateCloudx.opsForSet().size(key);
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
            Long count = redisTemplateCloudx.opsForSet().remove(key, values);
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
            return redisTemplateCloudx.opsForZSet().add(key, value, score);
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
            return redisTemplateCloudx.opsForZSet().range(key, start, end);
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
            return redisTemplateCloudx.opsForZSet().reverseRange(key, start, end);
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
    public static Set<String> zRangeWithScores(String key, double start, double end) {
        try {
            return redisTemplateCloudx.opsForZSet().rangeByScore(key, start, end);
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
            return redisTemplateCloudx.opsForZSet().reverseRangeByScore(key, start, end);
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
            return redisTemplateCloudx.opsForZSet().rank(key, value);
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
            return redisTemplateCloudx.opsForZSet().reverseRank(key, value);
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
            return redisTemplateCloudx.opsForZSet().count(key, min, max);
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
            return redisTemplateCloudx.opsForZSet().size(key);
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
            return redisTemplateCloudx.opsForZSet().zCard(key);
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
            return redisTemplateCloudx.opsForZSet().score(key, value);
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
            return redisTemplateCloudx.opsForZSet().remove(key, values);
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
            return redisTemplateCloudx.opsForZSet().removeRange(key, start, end);
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
            return redisTemplateCloudx.opsForZSet().removeRangeByScore(key, min, max);
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
            return redisTemplateCloudx.opsForZSet().incrementScore(key, value, delta);
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
            redisTemplateCloudx.delete(key);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    // ===============================list=================================

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
            return redisTemplateCloudx.opsForList().range(key, start, end);
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
            return redisTemplateCloudx.opsForList().size(key);
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
            return redisTemplateCloudx.opsForList().index(key, index);
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
            redisTemplateCloudx.opsForList().rightPush(key, value);
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
            redisTemplateCloudx.opsForList().rightPush(key, value);
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
    public static boolean lSet(String key, List<Object> value) {
        try {
            redisTemplateCloudx.opsForList().rightPushAll(key, value);
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
    public static boolean lSet(String key, List<Object> value, long time) {
        try {
            redisTemplateCloudx.opsForList().rightPushAll(key, value);
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
            redisTemplateCloudx.opsForList().set(key, index, value);
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
            Long remove = redisTemplateCloudx.opsForList().remove(key, count, value);
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
        Set<String> keys = redisTemplateCloudx.keys(key);
        keys.forEach(x -> {
            redisTemplateCloudx.delete(x);
        });
    }

    /**
     *  从左弹出一条数据
     *
     * @param key
     */
    public static void leftPopKey(String key){
        redisTemplateCloudx.opsForList().leftPop(key);
    }


    /**
     *  从右弹出一条数据
     *
     * @param key
     */
    public static Object rightPop(String key) {
        return redisTemplateCloudx.opsForList().rightPop(key);
    }
}
