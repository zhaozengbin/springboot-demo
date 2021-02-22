package com.zzb.redis;

import com.zzb.core.aop.ann.CallTimeAnn;
import com.zzb.redis.utils.RedisUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RedisZSetTest {
    private static String key = "testZSet";

    @Autowired
    private RedisUtils redisUtils;

    public void add() {
        for (int i = 0; i < 100000; i++) {
            System.out.println(String.format("正在处理第[%d]个", i));
            redisUtils.zSetSet(key, "test_" + i, 100000 - i);
        }
    }

    @Test
    @CallTimeAnn
    public void rank() {
        System.out.println(redisUtils.zSetRank(key, "test_1"));
    }

    public void remove() {
        redisUtils.del(key);
    }
}
