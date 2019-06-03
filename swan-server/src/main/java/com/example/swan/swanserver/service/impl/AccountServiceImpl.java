package com.example.swan.swanserver.service.impl;

import com.example.swan.swanserver.dao.AuthUserMapper;
import com.example.swan.swanserver.model.Message;
import com.example.swan.swanserver.model.po.DefaultAuthUser;
import com.example.swan.swanserver.security.JsonWebTokenUtil;
import com.example.swan.swanserver.service.AccountService;
import com.example.swan.swanserver.util.CommonUtil;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;

/**
 * @author feng
 * 账户服务实现类
 */
@Service
public class AccountServiceImpl implements AccountService {

    @Autowired
    private AuthUserMapper authUserMapper;

    @Override
    public Message register(String username, String password) {

        Message message = new Message();

        DefaultAuthUser defaultAuthUser = new DefaultAuthUser();
        defaultAuthUser.setUserId(username);
        defaultAuthUser.setUsername(username);
        defaultAuthUser.setPassword(password);
        int res = authUserMapper.selectCountByUserId(username);

        if (res != 0) {
            message.setSuccess(false);
            message.setMsg("不可重复注册");

        } else if (authUserMapper.insert(defaultAuthUser) == 1) {

            message.setSuccess(true);
            message.setMsg("注册成功");
        } else {
            message.setSuccess(false);
            message.setMsg("注册失败");
        }

        return message;
    }


    @Override
    public Message login(String username, String password) {

        Message message = new Message();

        DefaultAuthUser authUser = authUserMapper.selectAuthUserByUsername(username);
        if (authUser != null) {
            if (authUser.getPassword().equals(password)) {
                String authToken = JsonWebTokenUtil.issueJWT(CommonUtil.getJWTId(), authUser.getUserId(), "JWT-SERVER", 24 * 60 * 60L, null, null, SignatureAlgorithm.HS512);
                message.setSuccess(true);
                message.setMsg("获取成功");
                message.addParams("auth_token", authToken);
            }else {
                message.setSuccess(false);
                message.setMsg("账号密码不匹配");
            }
        }else {
            message.setSuccess(false);
            message.setMsg("不存在该用户");
        }

        return message;
    }


    @Override
    public Message modifyPassword(String userId, String password, String newPassword) {

        Message message = new Message();

        DefaultAuthUser authUser = authUserMapper.selectAuthUserByUserId(userId);

        if(authUser!=null){
            if (authUser.getPassword().equals(password)) {

                if (authUserMapper.updatePasswordByUserId(userId, newPassword) == 1){
                    message.setSuccess(true);
                }else {
                    message.setSuccess(false);
                    message.setMsg("修改失败");
                }

            }else {
                message.setSuccess(false);
                message.setMsg("密码不匹配");
            }
        }else {
            message.setSuccess(false);
            message.setMsg("不存在该用户");
        }

      return message;
    }
}
