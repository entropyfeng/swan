package com.example.swan.swanserver.service.impl;

import com.example.swan.swanserver.dao.AuthUserMapper;
import com.example.swan.swanserver.model.po.DefaultAuthUser;
import com.example.swan.swanserver.security.JsonWebTokenUtil;
import com.example.swan.swanserver.service.AccountService;
import com.example.swan.swanserver.util.CommonUtil;
import io.jsonwebtoken.SignatureAlgorithm;
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

        DefaultAuthUser defaultAuthUser = new DefaultAuthUser();
        defaultAuthUser.setUserId(username);
        defaultAuthUser.setUsername(username);
        defaultAuthUser.setPassword(password);


        return authUserMapper.insert(defaultAuthUser) == 1;
    }


    @Override
    public String login(String username, String password) {


        String res = "ERROR";
        DefaultAuthUser authUser = authUserMapper.selectAuthUserByUsername(username);
        if (authUser != null) {
            if (authUser.getPassword().equals(password)) {

                res = JsonWebTokenUtil.issueJWT(CommonUtil.getJWTId(), authUser.getUserId(), "JWT-SERVER", 24 * 60 * 60L, null, null, SignatureAlgorithm.HS512);

            }
        }


        return res;
    }

    @Override
    public boolean modifyPassword(String userId, String password, String newPassword) {

        boolean res=false;
      DefaultAuthUser authUser=  authUserMapper.selectAuthUserByUserId(userId);

      if(authUser.getPassword().equals(password)){

        res=  authUserMapper.updatePasswordByUserId(userId,newPassword)==1;

      }

        return res;
    }
}
