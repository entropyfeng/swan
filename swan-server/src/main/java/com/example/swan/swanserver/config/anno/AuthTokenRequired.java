package com.example.swan.swanserver.config.anno;

import java.lang.annotation.*;

/**
 * 需要token 的验证
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface AuthTokenRequired {

}
