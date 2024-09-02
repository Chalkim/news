package com.heima.cache.test;

import com.alibaba.fastjson.JSON;
import com.heima.redis.RedisApplication;
import com.heima.redis.constants.BehaviorConstants;
import com.heima.redis.dtos.LikesBehaviorDto;
import com.heima.redis.service.CacheService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest(classes = RedisApplication.class)
@RunWith(SpringRunner.class)
public class RedisTest {
    @Autowired
    private CacheService cacheService;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Test
    public void batchSave() {
        LikesBehaviorDto dto = new LikesBehaviorDto();
        Long id = 2824540423503351801L;
        dto.setType((short) 0);
        dto.setOperation((short) 0);

        // 显式指定泛型类型
        redisTemplate.executePipelined(new SessionCallback<Void>() {
            @Override
            public Void execute(RedisOperations operations) throws DataAccessException {
                String keyPrefix = BehaviorConstants.LIKE_BEHAVIOR + id.toString();
                for (Long i = 0L; i < 100000; i++) {
                    String key = keyPrefix;
                    String field = i.toString();
                    // String value = JSON.toJSONString(dto);
                    operations.opsForHash().put(key, field, dto);
                    if (i % 10000 == 0) {
                        System.out.println(i);
                    }
                }
                return null;
            }
        });
    }

}
