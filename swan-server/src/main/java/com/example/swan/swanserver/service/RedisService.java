package com.example.swan.swanserver.service;

/**
 * RedisService 操作redis
 * @author feng
 */
public interface RedisService {
    /**
     * 在redis中设置验证码
     * @param kaptchaToken 图形验证码Key
     * @param kaptcha 图形验证码值
     */
    public void setKaptcha(String kaptchaToken,String kaptcha);


    /**
     * 在redis 中验证是否验证码正确
     * @param kaptchaToken 图形验证码Key
     * @param kaptcha 图形验证码值
     * @return 是否验证成功
     */
    public boolean checkKaptcha(String kaptchaToken,String kaptcha);
}
