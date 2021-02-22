package com.zzb.redis.utils;

import cn.hutool.core.collection.CollUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.DefaultTypedTuple;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Component
public class RedisUtils {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    // =============================common============================

    /**
     * 方法：expire
     * 描述：指定缓存失效时间
     * 作者：赵增斌 E-mail:zhaozengbin@gmail.com QQ:4415599 weibo:http://weibo.com/zhaozengbin
     *
     * @param key  : 键
     * @param time : 时间(秒)
     * @return : boolean
     * @date: 2020年12月02日 11:30 上午
     */
    public boolean expire(String key, long time) {

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
     * 方法：getExpire
     * 描述：根据key 获取过期时间
     * 作者：赵增斌 E-mail:zhaozengbin@gmail.com QQ:4415599 weibo:http://weibo.com/zhaozengbin
     *
     * @param key : 键 不能为null
     * @return : long  时间(秒) 返回0代表为永久有效
     * @date: 2020年12月02日 11:30 上午
     */
    public long getExpire(String key) {
        return redisTemplate.getExpire(key, TimeUnit.SECONDS);
    }

    /**
     * 方法：hasKey
     * 描述：判断key是否存在
     * 作者：赵增斌 E-mail:zhaozengbin@gmail.com QQ:4415599 weibo:http://weibo.com/zhaozengbin
     *
     * @param key : 键
     * @return : boolean true 存在 false不存在
     * @date: 2020年12月02日 11:31 上午
     */
    public boolean hasKey(String key) {
        try {
            return redisTemplate.hasKey(key);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 方法：del
     * 描述：删除缓存
     * 作者：赵增斌 E-mail:zhaozengbin@gmail.com QQ:4415599 weibo:http://weibo.com/zhaozengbin
     *
     * @param key : 可以传一个值 或多个
     * @return : void
     * @date: 2020年12月02日 11:31 上午
     */
    @SuppressWarnings("unchecked")
    public void del(String... key) {

        if (key != null && key.length > 0) {
            if (key.length == 1) {
                redisTemplate.delete(key[0]);
            } else {
                redisTemplate.delete(CollUtil.newArrayList(key));
            }
        }
    }
    // ============================String=============================

    /**
     * 方法：get
     * 描述：普通缓存获取
     * 作者：赵增斌 E-mail:zhaozengbin@gmail.com QQ:4415599 weibo:http://weibo.com/zhaozengbin
     *
     * @param key : 键
     * @return : java.lang.Object 值
     * @date: 2020年12月02日 11:31 上午
     */
    public Object get(String key) {
        return key == null ? null : redisTemplate.opsForValue().get(key);
    }

    /**
     * 方法：set
     * 描述：普通缓存放入
     * 作者：赵增斌 E-mail:zhaozengbin@gmail.com QQ:4415599 weibo:http://weibo.com/zhaozengbin
     *
     * @param key   : 键
     * @param value : 值
     * @return : boolean true成功 false失败
     * @date: 2020年12月02日 11:32 上午
     */
    public boolean set(String key, Object value) {

        try {
            redisTemplate.opsForValue().set(key, value);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 方法：set
     * 描述：普通缓存放入并设置时间
     * 作者：赵增斌 E-mail:zhaozengbin@gmail.com QQ:4415599 weibo:http://weibo.com/zhaozengbin
     *
     * @param key   : 键
     * @param value : 值
     * @param time  : 时间(秒) time要大于0 如果time小于等于0 将设置无限期
     * @return : boolean true成功 false 失败
     * @date: 2020年12月02日 11:32 上午
     */
    public boolean set(String key, Object value, long time) {
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
     * 方法：incr
     * 描述：递增
     * 作者：赵增斌 E-mail:zhaozengbin@gmail.com QQ:4415599 weibo:http://weibo.com/zhaozengbin
     *
     * @param key   : 键
     * @param delta :  要增加几(大于0)
     * @return : long
     * @date: 2020年12月02日 11:33 上午
     */
    public long incr(String key, long delta) {
        if (delta < 0) {
            throw new RuntimeException("递增因子必须大于0");
        }
        return redisTemplate.opsForValue().increment(key, delta);
    }

    /**
     * 方法：decr
     * 描述：递减
     * 作者：赵增斌 E-mail:zhaozengbin@gmail.com QQ:4415599 weibo:http://weibo.com/zhaozengbin
     *
     * @param key   : 键
     * @param delta : 要减少几(小于0)
     * @return : long
     * @date: 2020年12月02日 11:33 上午
     */
    public long decr(String key, long delta) {
        if (delta < 0) {
            throw new RuntimeException("递减因子必须大于0");
        }
        return redisTemplate.opsForValue().increment(key, -delta);
    }

    // ================================Map=================================

    /**
     * 方法：hget
     * 描述：HashGet
     * 作者：赵增斌 E-mail:zhaozengbin@gmail.com QQ:4415599 weibo:http://weibo.com/zhaozengbin
     *
     * @param key  :  键 不能为null
     * @param item : 项 不能为null
     * @return : java.lang.Object
     * @date: 2020年12月02日 11:34 上午
     */
    public Object hget(String key, String item) {
        return redisTemplate.opsForHash().get(key, item);
    }

    /**
     * 方法：hmget
     * 描述：获取hashKey对应的所有键值
     * 作者：赵增斌 E-mail:zhaozengbin@gmail.com QQ:4415599 weibo:http://weibo.com/zhaozengbin
     *
     * @param key : 键
     * @return : java.util.Map<java.lang.Object,java.lang.Object> 对应的多个键值
     * @date: 2020年12月02日 11:34 上午
     */
    public Map<Object, Object> hmget(String key) {
        return redisTemplate.opsForHash().entries(key);
    }

    /**
     * 方法：hmset
     * 描述：HashSet
     * 作者：赵增斌 E-mail:zhaozengbin@gmail.com QQ:4415599 weibo:http://weibo.com/zhaozengbin
     *
     * @param key : 键
     * @param map : 对应多个键值
     * @return : boolean  true 成功 false 失败
     * @date: 2020年12月02日 11:35 上午
     */
    public boolean hmset(String key, Map<String, Object> map) {
        try {
            redisTemplate.opsForHash().putAll(key, map);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 方法：hmset
     * 描述：HashSet 并设置时间
     * 作者：赵增斌 E-mail:zhaozengbin@gmail.com QQ:4415599 weibo:http://weibo.com/zhaozengbin
     *
     * @param key  : 键
     * @param map  : 对应多个键值
     * @param time : 时间(秒)
     * @return : boolean true成功 false失败
     * @date: 2020年12月02日 11:35 上午
     */
    public boolean hmset(String key, Map<String, Object> map, long time) {
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
     * 方法：hset
     * 描述：向一张hash表中放入数据,如果不存在将创建
     * 作者：赵增斌 E-mail:zhaozengbin@gmail.com QQ:4415599 weibo:http://weibo.com/zhaozengbin
     *
     * @param key   : 键
     * @param item  : 项
     * @param value : 值
     * @return : boolean true 成功 false失败
     * @date: 2020年12月02日 11:36 上午
     */
    public boolean hset(String key, String item, Object value) {

        try {
            redisTemplate.opsForHash().put(key, item, value);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 方法：hset
     * 描述：向一张hash表中放入数据,如果不存在将创建
     * 作者：赵增斌 E-mail:zhaozengbin@gmail.com QQ:4415599 weibo:http://weibo.com/zhaozengbin
     *
     * @param key   : 键
     * @param item  : 项
     * @param value : 值
     * @param time  : 时间(秒) 注意:如果已存在的hash表有时间,这里将会替换原有的时间
     * @return : boolean true 成功 false失败
     * @date: 2020年12月02日 11:36 上午
     */
    public boolean hset(String key, String item, Object value, long time) {
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
     * 方法：hdel
     * 描述：删除hash表中的值
     * 作者：赵增斌 E-mail:zhaozengbin@gmail.com QQ:4415599 weibo:http://weibo.com/zhaozengbin
     *
     * @param key  : 不能为null
     * @param item : 可以使多个 不能为null
     * @return : void
     * @date: 2020年12月02日 11:37 上午
     */
    public void hdel(String key, Object... item) {
        redisTemplate.opsForHash().delete(key, item);
    }

    /**
     * 方法：hHasKey
     * 描述：判断hash表中是否有该项的值
     * 作者：赵增斌 E-mail:zhaozengbin@gmail.com QQ:4415599 weibo:http://weibo.com/zhaozengbin
     *
     * @param key  :  键 不能为null
     * @param item : 项 不能为null
     * @return : boolean true 存在 false不存在
     * @date: 2020年12月02日 11:37 上午
     */
    public boolean hHasKey(String key, String item) {
        return redisTemplate.opsForHash().hasKey(key, item);
    }

    /**
     * 方法：hincr
     * 描述：hash递增 如果不存在,就会创建一个 并把新增后的值返回
     * 作者：赵增斌 E-mail:zhaozengbin@gmail.com QQ:4415599 weibo:http://weibo.com/zhaozengbin
     *
     * @param key  :   键
     * @param item : 项
     * @param by   : 要增加几(大于0)
     * @return : double
     * @date: 2020年12月02日 11:38 上午
     */
    public double hincr(String key, String item, double by) {
        return redisTemplate.opsForHash().increment(key, item, by);
    }

    /**
     * 方法：hdecr
     * 描述：hash递减
     * 作者：赵增斌 E-mail:zhaozengbin@gmail.com QQ:4415599 weibo:http://weibo.com/zhaozengbin
     *
     * @param key  : 键
     * @param item : 项
     * @param by   : 要减少记(小于0)
     * @return : double
     * @date: 2020年12月02日 11:38 上午
     */
    public double hdecr(String key, String item, double by) {
        return redisTemplate.opsForHash().increment(key, item, -by);
    }

    // ============================set=============================

    /**
     * 方法：sGet
     * 描述：根据key获取Set中的所有值
     * 作者：赵增斌 E-mail:zhaozengbin@gmail.com QQ:4415599 weibo:http://weibo.com/zhaozengbin
     *
     * @param key : 键
     * @return : java.util.Set<java.lang.Object>
     * @date: 2020年12月02日 11:39 上午
     */
    public Set<Object> sGet(String key) {
        try {
            return redisTemplate.opsForSet().members(key);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 方法：sHasKey
     * 描述：根据value从一个set中查询,是否存在
     * 作者：赵增斌 E-mail:zhaozengbin@gmail.com QQ:4415599 weibo:http://weibo.com/zhaozengbin
     *
     * @param key   : 键
     * @param value : 值
     * @return : boolean true 存在 false不存在
     * @date: 2020年12月02日 11:39 上午
     */
    public boolean sHasKey(String key, Object value) {
        try {
            return redisTemplate.opsForSet().isMember(key, value);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 方法：sSet
     * 描述：将数据放入set缓存
     * 作者：赵增斌 E-mail:zhaozengbin@gmail.com QQ:4415599 weibo:http://weibo.com/zhaozengbin
     *
     * @param key    : 键
     * @param values : 值 可以是多个
     * @return : long 成功个数
     * @date: 2020年12月02日 11:39 上午
     */
    public long sSet(String key, Object... values) {
        try {
            return redisTemplate.opsForSet().add(key, values);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * 方法：sSetAndTime
     * 描述： 将set数据放入缓存
     * 作者：赵增斌 E-mail:zhaozengbin@gmail.com QQ:4415599 weibo:http://weibo.com/zhaozengbin
     *
     * @param key    : 键
     * @param time   : 时间(秒)
     * @param values : 值 可以是多个
     * @return : long 成功个数
     * @date: 2020年12月02日 11:40 上午
     */
    public long sSetAndTime(String key, long time, Object... values) {

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
     * 方法：sGetSetSize
     * 描述：获取set缓存的长度
     * 作者：赵增斌 E-mail:zhaozengbin@gmail.com QQ:4415599 weibo:http://weibo.com/zhaozengbin
     *
     * @param key : 键
     * @return : long
     * @date: 2020年12月02日 11:40 上午
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
     * 方法：setRemove
     * 描述：移除值为value的
     * 作者：赵增斌 E-mail:zhaozengbin@gmail.com QQ:4415599 weibo:http://weibo.com/zhaozengbin
     *
     * @param key    : 键
     * @param values : 值 可以是多个
     * @return : long 移除的个数
     * @date: 2020年12月02日 11:40 上午
     */
    public long setRemove(String key, Object... values) {
        try {
            Long count = redisTemplate.opsForSet().remove(key, values);
            return count;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    // ===============================list=================================

    /**
     * 方法：lGet
     * 描述：获取list缓存的内容
     * 作者：赵增斌 E-mail:zhaozengbin@gmail.com QQ:4415599 weibo:http://weibo.com/zhaozengbin
     *
     * @param key   : 键
     * @param start : 开始
     * @param end   : 结束 0 到 -1代表所有值
     * @return : java.util.List<java.lang.Object>
     * @date: 2020年12月02日 11:41 上午
     */
    public List<Object> lGet(String key, long start, long end) {
        try {
            return redisTemplate.opsForList().range(key, start, end);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 方法：lGetListSize
     * 描述：获取list缓存的长度
     * 作者：赵增斌 E-mail:zhaozengbin@gmail.com QQ:4415599 weibo:http://weibo.com/zhaozengbin
     *
     * @param key : 键
     * @return : long
     * @date: 2020年12月02日 11:41 上午
     */
    public long lGetListSize(String key) {
        try {
            return redisTemplate.opsForList().size(key);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * 方法：lGetIndex
     * 描述：通过索引 获取list中的值
     * 作者：赵增斌 E-mail:zhaozengbin@gmail.com QQ:4415599 weibo:http://weibo.com/zhaozengbin
     *
     * @param key   : 键
     * @param index : index 索引 index>=0时， 0 表头，1 第二个元素，依次类推；index<0时，-1，表尾，-2倒数第二个元素，依次类推
     * @return : java.lang.Object
     * @date: 2020年12月02日 11:41 上午
     */
    public Object lGetIndex(String key, long index) {
        try {
            return redisTemplate.opsForList().index(key, index);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 方法：lSet
     * 描述：将list放入缓存
     * 作者：赵增斌 E-mail:zhaozengbin@gmail.com QQ:4415599 weibo:http://weibo.com/zhaozengbin
     *
     * @param key   : 键
     * @param value : 值
     * @return : boolean
     * @date: 2020年12月02日 11:42 上午
     */
    public boolean lSet(String key, Object value) {
        try {
            redisTemplate.opsForList().rightPush(key, value);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 方法：lSet
     * 描述：将list放入缓存
     * 作者：赵增斌 E-mail:zhaozengbin@gmail.com QQ:4415599 weibo:http://weibo.com/zhaozengbin
     *
     * @param key   : 键
     * @param value : 值
     * @param time  : 时间(秒)
     * @return : boolean
     * @date: 2020年12月02日 11:42 上午
     */
    public boolean lSet(String key, Object value, long time) {
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
     * 方法：lSet
     * 描述：将list放入缓存
     * 作者：赵增斌 E-mail:zhaozengbin@gmail.com QQ:4415599 weibo:http://weibo.com/zhaozengbin
     *
     * @param key   : 键
     * @param value : 值
     * @return : boolean
     * @date: 2020年12月02日 11:42 上午
     */
    public boolean lSet(String key, List<Object> value) {
        try {
            redisTemplate.opsForList().rightPushAll(key, value);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 方法：lSet
     * 描述：将list放入缓存
     * 作者：赵增斌 E-mail:zhaozengbin@gmail.com QQ:4415599 weibo:http://weibo.com/zhaozengbin
     *
     * @param key   : 键
     * @param value : 值
     * @param time  : 时间(秒)
     * @return : boolean
     * @date: 2020年12月02日 11:43 上午
     */
    public boolean lSet(String key, List<Object> value, long time) {
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
     * 方法：lUpdateIndex
     * 描述：根据索引修改list中的某条数据
     * 作者：赵增斌 E-mail:zhaozengbin@gmail.com QQ:4415599 weibo:http://weibo.com/zhaozengbin
     *
     * @param key   : 键
     * @param index : 索引
     * @param value : 值
     * @return : boolean
     * @date: 2020年12月02日 11:43 上午
     */
    public boolean lUpdateIndex(String key, long index, Object value) {
        try {
            redisTemplate.opsForList().set(key, index, value);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 方法：lRemove
     * 描述：移除N个值为value
     * 作者：赵增斌 E-mail:zhaozengbin@gmail.com QQ:4415599 weibo:http://weibo.com/zhaozengbin
     *
     * @param key   : 键
     * @param count : 移除多少个
     * @param value : 值
     * @return : long 移除的个数
     * @date: 2020年12月02日 11:44 上午
     */
    public long lRemove(String key, long count, Object value) {
        try {
            Long remove = redisTemplate.opsForList().remove(key, count, value);
            return remove;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }


    // ===============================zset=================================

    /**
     * 方法：zSetCount
     * 描述：统计数量
     * 作者：赵增斌 E-mail:zhaozengbin@gmail.com QQ:4415599 weibo:http://weibo.com/zhaozengbin
     *
     * @param key : 键
     * @param min : 最小值
     * @param max : 最大值
     * @return : long
     * @date: 2020年12月02日 11:44 上午
     */
    public long zSetCount(String key, long min, long max) {
        try {
            return redisTemplate.opsForZSet().count(key, min, max);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * 方法：zSetSize
     * 描述：获取list缓存的长度
     * 作者：赵增斌 E-mail:zhaozengbin@gmail.com QQ:4415599 weibo:http://weibo.com/zhaozengbin
     *
     * @param key : 键
     * @return : long
     * @date: 2020年12月02日 11:45 上午
     */
    public long zSetSize(String key) {
        try {
            return redisTemplate.opsForZSet().size(key);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * 方法：zSetIncrementScore
     * 描述：对指定成员的分数加上增量 increment
     * 作者：赵增斌 E-mail:zhaozengbin@gmail.com QQ:4415599 weibo:http://weibo.com/zhaozengbin
     *
     * @param key   :键
     * @param value :值
     * @param delta : 增量值
     * @return : java.lang.Object
     * @date: 2020年12月02日 11:49 上午
     */
    public Object zSetIncrementScore(String key, Object value, double delta) {
        try {
            return redisTemplate.opsForZSet().incrementScore(key, value, delta);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 方法：zSetSet
     * 描述：向有序集合添加一个,或者更新已存在成员的分数
     * 作者：赵增斌 E-mail:zhaozengbin@gmail.com QQ:4415599 weibo:http://weibo.com/zhaozengbin
     *
     * @param key   : 键
     * @param value :  值
     * @param score : 分数
     * @return : boolean
     * @date: 2020年12月02日 11:50 上午
     */
    public boolean zSetSet(String key, Object value, double score) {
        try {
            redisTemplate.opsForZSet().add(key, value, score);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 方法：zSetSet
     * 描述：向有序集合添加一个,或者更新已存在成员的分数
     * 作者：赵增斌 E-mail:zhaozengbin@gmail.com QQ:4415599 weibo:http://weibo.com/zhaozengbin
     *
     * @param key   : 键
     * @param value :  值
     * @param score : 分数
     * @param time  : 有效期
     * @return : boolean
     * @date: 2020年12月02日 11:53 上午
     */
    public boolean zSetSet(String key, Object value, double score, long time) {
        if (zSetSet(key, value, score)) {
            if (time > 0) {
                expire(key, time);
            }
            return true;
        }
        return false;
    }

    /**
     * 方法：zSetSet
     * 描述：向有序集合添加多个成员，或者更新已存在成员的分数
     * 作者：赵增斌 E-mail:zhaozengbin@gmail.com QQ:4415599 weibo:http://weibo.com/zhaozengbin
     *
     * @param key    : 键
     * @param values : 值
     * @return : boolean
     * @date: 2020年12月02日 11:56 上午
     */
    public boolean zSetSet(String key, Map<Object, Double> values) {
        try {
            Set<ZSetOperations.TypedTuple<Object>> typedTupleSet = new HashSet<ZSetOperations.TypedTuple<Object>>();
            values.forEach((value, source) -> {
                typedTupleSet.add(new DefaultTypedTuple<Object>(value, source));
            });
            redisTemplate.opsForZSet().add(key, typedTupleSet);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 方法：zSetSet
     * 描述：向有序集合添加多个成员，或者更新已存在成员的分数
     * 作者：赵增斌 E-mail:zhaozengbin@gmail.com QQ:4415599 weibo:http://weibo.com/zhaozengbin
     *
     * @param key    : 键
     * @param values : 值
     * @param time   : 有效期
     * @return : boolean
     * @date: 2020年12月02日 11:56 上午
     */
    public boolean zSetSet(String key, Map<Object, Double> values, long time) {
        try {
            zSetSet(key, values);
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
     * 方法：zSetRank
     * 描述：返回有序集中指定成员的排名。其中有序集成员按分数值递增(从小到大)顺序排列。
     * 作者：赵增斌 E-mail:zhaozengbin@gmail.com QQ:4415599 weibo:http://weibo.com/zhaozengbin
     *
     * @param key   : 键
     * @param value : 值
     * @return : long 排序 从零开始
     * @date: 2020年12月02日 12:08 下午
     */
    public long zSetRank(String key, Object value) {
        try {
            return redisTemplate.opsForZSet().rank(key, value);
        } catch (Exception e) {
            return -1;
        }
    }

    /**
     * 方法：zSetRange
     * 描述：通过索引区间返回有序集合指定区间内的成员
     * 作者：赵增斌 E-mail:zhaozengbin@gmail.com QQ:4415599 weibo:http://weibo.com/zhaozengbin
     *
     * @param key   : 键
     * @param start : 开始索引
     * @param end   : 结束索引
     * @return : boolean
     * @date: 2020年12月02日 12:08 下午
     */
    public Set<Object> zSetRange(String key, long start, long end) {
        try {
            return redisTemplate.opsForZSet().range(key, start, end);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 方法：zSetRangeByScore
     * 描述：通过索引区间返回有序集合指定区间内的成员
     * 作者：赵增斌 E-mail:zhaozengbin@gmail.com QQ:4415599 weibo:http://weibo.com/zhaozengbin
     *
     * @param key : 键
     * @param min : 最小分数
     * @param max : 最大分数
     * @return : boolean
     * @date: 2020年12月02日 12:08 下午
     */
    public Set<Object> zSetRangeByScore(String key, long min, long max) {
        try {
            return redisTemplate.opsForZSet().rangeByScore(key, min, max);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 方法：zSetRemove
     * 描述：移除有序集合中的一个或多个成员
     * 作者：赵增斌 E-mail:zhaozengbin@gmail.com QQ:4415599 weibo:http://weibo.com/zhaozengbin
     *
     * @param key    : 键
     * @param values : 值
     * @return : long
     * @date: 2020年12月02日 12:10 下午
     */
    public long zSetRemove(String key, Object... values) {
        try {
            return redisTemplate.opsForZSet().remove(key, values);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * 方法：zSetRemoveRange
     * 描述：移除有序集合中给定的排名区间的所有成员
     * 作者：赵增斌 E-mail:zhaozengbin@gmail.com QQ:4415599 weibo:http://weibo.com/zhaozengbin
     *
     * @param key   : 键
     * @param start : 开始排名
     * @param stop  : 结束排名
     * @return : long
     * @date: 2020年12月02日 12:10 下午
     */
    public long zSetRemoveRange(String key, long start, long stop) {
        try {
            return redisTemplate.opsForZSet().removeRange(key, start, stop);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * 方法：zSetRemoveRange
     * 描述：移除有序集合中给定的分数区间的所有成员
     * 作者：赵增斌 E-mail:zhaozengbin@gmail.com QQ:4415599 weibo:http://weibo.com/zhaozengbin
     *
     * @param key : 键
     * @param min : 最小分数
     * @param max : 最大分数
     * @return : long
     * @date: 2020年12月02日 12:10 下午
     */
    public long zSetRemoveRangeByScore(String key, long min, long max) {
        try {
            return redisTemplate.opsForZSet().removeRangeByScore(key, min, max);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }
}
