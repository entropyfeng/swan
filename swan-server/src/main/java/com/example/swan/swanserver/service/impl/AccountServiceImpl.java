package com.example.swan.swanserver.service.impl;

import com.example.swan.swanserver.dao.AuthUserMapper;
import com.example.swan.swanserver.model.po.DefaultAuthUser;
import com.example.swan.swanserver.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author feng
 * 账户服务实现类
 */
@Service
public class AccountServiceImpl implements AccountService {

    @Autowired
    private AuthUserMapper authUserMapper;

    @Override
    public boolean register(String username, String password) {

        DefaultAuthUser defaultAuthUser=new DefaultAuthUser();
        defaultAuthUser.setUserId(username);
        defaultAuthUser.setUsername(username);
        defaultAuthUser.setPassword(password);



        return   authUserMapper.insert(defaultAuthUser)==1;
    }
}
