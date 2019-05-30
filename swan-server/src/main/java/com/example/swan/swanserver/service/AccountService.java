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
     *
     * @param username 用户名
     * @param password 密码
     * @return authToken
     */
    public String login(String username,String password);

    /**
     * 修改密码
     * @param userId 用户Id
     * @param password 旧密码
     * @param newPassword 新密码
     * @return 是否成功
     */
    public boolean modifyPassword(String userId,String password,String newPassword);
}
