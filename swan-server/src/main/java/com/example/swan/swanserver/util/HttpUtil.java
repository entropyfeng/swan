package com.example.swan.swanserver.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @author eng
 * Http 工具类
 */
public class HttpUtil {

    private static final Logger logger=LoggerFactory.getLogger(HttpUtil.class);
    public static void writeJsonResponse(HttpServletResponse response, String jsonString) {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=utf-8");
        try (PrintWriter out = response.getWriter()) {
            out.write(jsonString);
            out.flush();
        } catch (IOException e) {
            logger.warn(e.toString());
        }


    }
}
