package com.example.swan.swanserver.service.impl;

import com.example.swan.swanserver.service.KaptchaService;
import com.example.swan.swanserver.service.MessageService;
import com.example.swan.swanserver.service.RedisService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class MessageServiceImpl implements MessageService {

    private static final Logger logger=LoggerFactory.getLogger(MessageServiceImpl.class);

    @Autowired
    private KaptchaService kaptchaService;

    @Autowired
    private RedisService redisService;




    @Override
    public Map getKaptcha() {

      Map map=  kaptchaService.createKaptcha();

      if(map!=null){
          redisService.setKaptcha((String) map.get("kaptcha_token"),(String) map.get("kaptcha"));
          map.remove("kaptcha");

      }

        return map;
    }
}
