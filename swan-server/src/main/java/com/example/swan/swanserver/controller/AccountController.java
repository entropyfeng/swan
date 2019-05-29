package com.example.swan.swanserver.controller;

import com.alibaba.fastjson.JSON;
import com.example.swan.swanserver.model.Message;
import com.example.swan.swanserver.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author feng
 * 账户控制类
 */
@RestController
public class AccountController {


    @Autowired
    private AccountService accountService;

    @PostMapping("/account/register")
    public String accountRegister(@RequestParam("username")String username,@RequestParam("password") String password){


        Message message=new Message();
        if(accountService.register(username,password)){
            message.setSuccess(true);
        }else {
            message.setSuccess(false);
            message.setMsg("register error");
        }


        return JSON.toJSONString(message);
    }

    @PostMapping("/account/login")
    public String accountLogin(@RequestParam("username")String username,@RequestParam("password") String password){


        Message message=new Message();
        if(accountService.register(username,password)){
            message.setSuccess(true);
        }else {
            message.setSuccess(false);
            message.setMsg("login error");
        }


        return JSON.toJSONString(message);
    }


}
