package com.example.swan.swanserver.controller;

import com.alibaba.fastjson.JSON;
import com.example.swan.swanserver.config.anno.AuthTokenRequired;
import com.example.swan.swanserver.config.anno.KaptchaRequired;
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

    @KaptchaRequired
    @PostMapping("/account/register")
    public String accountRegister(@RequestParam("username") String username, @RequestParam("password") String password) {

        return JSON.toJSONString(accountService.register(username,password));
    }

    @PostMapping("/account/login")
    public String accountLogin(@RequestParam("username") String username, @RequestParam("password") String password) {

        return JSON.toJSONString(accountService.login(username,password));
    }
    @AuthTokenRequired
    @PostMapping("/account/modify/password")
    public String accountModifyPassword(@RequestAttribute("user_id")String userId, @RequestParam("new_password")String newPassword, @RequestParam("password") String password) {

        return JSON.toJSONString(accountService.modifyPassword(userId,password,newPassword));
    }

}
