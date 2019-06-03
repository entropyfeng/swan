package com.example.swan.util;

import android.support.annotation.NonNull;

import com.alibaba.fastjson.JSON;
import com.example.swan.config.CommonConfig;
import com.example.swan.domain.Message;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class HttpUtil {

    public static void sendGetWithOkHttp(String address, Callback callback) {

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(address).build();
        client.newCall(request).enqueue(callback);

    }

    public static void asynchronizedPost(String address, Map<String, String> data, Callback callback) {


        OkHttpClient client = new OkHttpClient();

        FormBody.Builder builder = new FormBody.Builder();
        data.forEach(builder::add);
        Request request = new Request.Builder().url(address).post(builder.build()).build();
        client.newCall(request).enqueue(callback);

    }

    /**
     * 同步方式获取验证码
     *
     * @return
     */
    public Map<String, String> synGetKaptcha() {

        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder().url(CommonConfig.URL + "/request/kaptcha").build();
        Map<String, String> resParams = new HashMap<>();
        try {
            Response response = client.newCall(request).execute();
            if (response.isSuccessful() && response.body() != null) {
                try {
                    Message message = JSON.parseObject(response.body().string(), Message.class);
                    if (message != null && message.isSuccess()) {
                        String kaptchaToken = message.getParams().get("kaptcha_token");
                        String img = message.getParams().get("img");
                        if (kaptchaToken != null && img != null) {
                            resParams.put("kaptcha_token", kaptchaToken);
                            resParams.put("img", img);
                        } else {
                            resParams = null;
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    resParams = null;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            resParams = null;
        }
        return resParams;
    }
}
