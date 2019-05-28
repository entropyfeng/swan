package com.example.swan.swanserver.model;

import java.io.Serializable;
import java.util.HashMap;

/**
 * 转为json 返回客户端
 * @author feng
 */
public class Message implements Serializable {
    private boolean success;
    private String msg;
    private HashMap<String,String> params=new HashMap<>();

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public HashMap<String, String> getParams() {
        return params;
    }

    public void setParams(HashMap<String, String> params) {
        this.params = params;
    }
    public void addParams(String key,String value){
        this.params.put(key,value);
    }
}
