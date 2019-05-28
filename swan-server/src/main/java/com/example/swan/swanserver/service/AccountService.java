package com.example.swan.swanserver.service;

/**账户服务
 * @author feng
 */
public interface AccountService {


    /**
     * 根据用户名密码注册账户
     * @param username 用户名
     * @param password 密码
     */
    public boolean register(String username,String password);



}
