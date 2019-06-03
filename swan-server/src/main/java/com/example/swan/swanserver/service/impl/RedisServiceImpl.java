package com.example.swan.swanserver.service.impl;

import com.example.swan.swanserver.service.RedisService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * RedisService 默认实现
 * @author feng
 */
@Service
public class RedisServiceImpl implements RedisService {

    private static final String KAPTCHA_SUFFIX="KAPTCHA_TOKEN_";
    private static final Logger logger = LoggerFactory.getLogger(RedisServiceImpl.class);
    @Autowired
    StringRedisTemplate redisTemplate;

    @Override
    public void setKaptcha(String kaptchaToken, String kaptcha) {
        //设置过期时间300秒，并加入redis
        redisTemplate.opsForValue().set(KAPTCHA_SUFFIX + kaptchaToken, kaptcha, 5*60, TimeUnit.SECONDS);
    }


    private String getKaptcha(String kaptchaToken) {

        return redisTemplate.opsForValue().get(KAPTCHA_SUFFIX + kaptchaToken);

    }


    private void removeKaptcha(String kaptchaToken) {
        //一用一弃
        boolean res = redisTemplate.opsForValue().getOperations().delete(KAPTCHA_SUFFIX + kaptchaToken);

        if (!res) {
            logger.warn(" redis don't exist key->{}", KAPTCHA_SUFFIX + kaptchaToken);
        }
    }

    @Override
    public boolean checkKaptcha(String kaptchaToken, String kaptcha) {

        boolean res=false;
        if (kaptchaToken != null && kaptcha != null) {
            //redisService 获取的kaptcha 可能为空，此时返回false
            res = kaptcha.equals(getKaptcha(kaptchaToken));
           removeKaptcha(kaptchaToken);
        }

        return res;
    }




}
