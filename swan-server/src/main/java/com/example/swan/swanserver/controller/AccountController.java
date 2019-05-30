package com.example.swan.swanserver.controller;

import com.alibaba.fastjson.JSON;
import com.example.swan.swanserver.config.Constants;
import com.example.swan.swanserver.config.anno.AuthTokenRequired;
import com.example.swan.swanserver.model.Message;
import com.example.swan.swanserver.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
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
    public String accountRegister(@RequestParam("username") String username, @RequestParam("password") String password) {


        Message message = new Message();
        if (accountService.register(username, password)) {
            message.setSuccess(true);
        } else {
            message.setSuccess(false);
            message.setMsg("register error");
        }


        return JSON.toJSONString(message);
    }

    @PostMapping("/account/login")
    public String accountLogin(@RequestParam("username") String username, @RequestParam("password") String password) {


        Message message = new Message();
        String res = accountService.login(username, password);
        if (!Constants.ERROR.equals(res)) {
            message.setSuccess(true);
            message.addParams("auth_token", res);
        } else {
            message.setSuccess(false);
            message.setMsg("login error");
        }

        return JSON.toJSONString(message);
    }
    @AuthTokenRequired
    @PostMapping("/account/modify/password")
    public String accountModifyPassword(@RequestAttribute("user_id")String userId, @RequestParam("new_password")String newPassword, @RequestParam("password") String password) {


        Message message = new Message();
        boolean res = accountService.modifyPassword(userId,password,newPassword);
        if(res){
            message.setSuccess(true);
        }else {
            message.setSuccess(false);
        }

        return JSON.toJSONString(message);
    }

}
