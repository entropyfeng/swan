package com.example.swan.swanserver.service;

/**账户服务
 * @author feng
 */
public interface AccountService {


    /**
     * 根据用户名密码注册账户
     * @param username 用户名
     * @param password 密码
     * @return bool 注册成功 false 注册失败
     */
    public boolean register(String username,String password);


    /**
     * 根据用户名 密码登录
     * @param username 用户名
     * @param password 密码
     * @return authToken
     */
    public String login(String username,String password);
}
