package com.example.swan.swanserver.util;

import java.util.UUID;

/**
 * @author feng
 * 通用工具类
 */
public class CommonUtil {
    /**
     * 获得kaptcha 的token
     * 在分布式情况下可能导致未定义的结果
     * @return kaptchaToken
     */
    public static String getKaptchaToken(){

        return UUID.randomUUID().toString();

    }


}
