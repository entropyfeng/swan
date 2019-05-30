package com.example.swan.swanserver.config.interceptors;

import com.alibaba.fastjson.JSON;
import com.example.swan.swanserver.config.anno.AuthTokenRequired;
import com.example.swan.swanserver.model.Message;
import com.example.swan.swanserver.security.JsonWebTokenUtil;
import com.example.swan.swanserver.security.JwtAccount;
import com.example.swan.swanserver.util.HttpUtil;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

/**
 * AuthToken拦截器
 *
 * @author entropyfeng
 */
public class AuthTokenInterceptor implements HandlerInterceptor {

    private static final Logger logger = LoggerFactory.getLogger(AuthTokenInterceptor.class);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {


        boolean res = true;
        if (handler instanceof HandlerMethod) {

            HandlerMethod handlerMethod = (HandlerMethod) handler;
            AuthTokenRequired authTokenRequired = handlerMethod.getMethod().getAnnotation(AuthTokenRequired.class);

            if(authTokenRequired!=null){

             res=checkAuthToken(request,response);
            }

        }

        return res;
    }

    private boolean checkAuthToken( HttpServletRequest request, HttpServletResponse response) {
        boolean res = false;

        String authToken = request.getParameter("auth_token");

        Message message=new Message();
        if (authToken != null) {
            JwtAccount jwtAccount=null;
            try {


                jwtAccount= JsonWebTokenUtil.parseJwt(authToken, JsonWebTokenUtil.SECRET_KEY);
            } catch (ExpiredJwtException e){

                logger.info(e.getMessage(),"jwt {} expired ",authToken);
                message.setSuccess(false);
                message.setMsg("token expired");
                HttpUtil.writeJsonResponse(response, JSON.toJSONString(message));
            }catch (SignatureException | UnsupportedJwtException | MalformedJwtException | IllegalArgumentException e) {

                logger.info(e.getMessage(),"jwtAccount {} parse error",authToken);
                message.setSuccess(false);
                message.setMsg("token error");
                HttpUtil.writeJsonResponse(response, JSON.toJSONString(message));
            } catch (Exception e) {
                message.setSuccess(false);
                message.setMsg("unknown error");
                HttpUtil.writeJsonResponse(response, JSON.toJSONString(message));
                logger.info(e.getMessage(),"jwtAccount {} unknown exception",authToken);

            }
            if (jwtAccount!=null){

                request.setAttribute("user_id",jwtAccount.getAppId());
                res=true;
            }

        }

        return res;
    }




}
