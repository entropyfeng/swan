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

    public static void sendGetWithOkHttp(String address, Callback callback){

        OkHttpClient client=new OkHttpClient();
        Request request=new Request.Builder().url(address).build();
        client.newCall(request).enqueue(callback);

    }
    public static void asynchronizedPost(String address, Map<String,String> data, Callback callback){


        OkHttpClient client=new OkHttpClient();

        FormBody.Builder builder=new FormBody.Builder();
        data.forEach(builder::add);
        Request request=new Request.Builder().url(address).post(builder.build()).build();
        client.newCall(request).enqueue(callback);

    }
    public  static Map<String,String> sendRequestKaptcha(){

        Map<String,String> resParams=new HashMap<>();
        HttpUtil.sendGetWithOkHttp(CommonConfig.URL + "/request/kaptcha", new Callback() {
            private   boolean resStatus = true;
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                resStatus=false;
            }
            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful() && response.body() != null) {
                    try {
                        Message message = JSON.parseObject(response.body().string(), Message.class);
                        if (message != null && message.isSuccess()) {
                            resParams.put("kaptcha_token",message.getParams().get("kaptcha_token"));
                            resParams.put("img",message.getParams().get("img"));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        resStatus = false;
                    }
                } else {
                    resStatus = false;
                }
                //如果获取图形验证码为失败状态
                if(!resStatus){
                    resParams.clear();
                }
            }

        });
        return resParams;
    }

    public void synGetKaptcha() throws IOException {

        OkHttpClient client=new OkHttpClient();

        Request request=new Request.Builder().url(CommonConfig.URL+"/request/kaptcha").build();

        client.newCall(request).execute();

    }

}
