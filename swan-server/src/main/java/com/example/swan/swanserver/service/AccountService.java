package com.example.swan.swanserver.service;

import com.example.swan.swanserver.model.Message;

/**账户服务
 * @author feng
 */
public interface AccountService {


    /**
     * 根据用户名密码注册账户
     * @param username 用户名
     * @param password 密码
     * @return {@link Message}
     */
    public Message register(String username, String password);


    /**
     * 根据用户名 密码登录
     *
     * @param username 用户名
     * @param password 密码
     * @return {@link Message}
     */
    public Message login(String username,String password);

    /**
     * 修改密码
     * @param userId 用户Id
     * @param password 旧密码
     * @param newPassword 新密码
     * @return {@link Message}
     */
    public Message modifyPassword(String userId,String password,String newPassword);
}
