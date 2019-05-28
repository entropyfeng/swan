package com.example.swan.swanserver.controller;

import com.alibaba.fastjson.JSON;

import com.example.swan.swanserver.model.Message;
import com.example.swan.swanserver.service.MessageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
public class MessageController {


    private static final Logger logger = LoggerFactory.getLogger(MessageController.class);
    private static final String SHORT_MESSAGE_SUCCESS = "OK";
    @Autowired
    private MessageService messageService;

    @GetMapping("/request/kaptcha")
    public String requestKaptcha() {
        Message res = new Message();
        Map map = messageService.getKaptcha();

        if (map == null) {
            res.setSuccess(false);
        } else {
            res.setSuccess(true);
            res.addParams("img", (String) map.get("img"));
            res.addParams("kaptcha_token", (String) map.get("kaptcha_token"));
        }

        return JSON.toJSONString(res);
    }

}
