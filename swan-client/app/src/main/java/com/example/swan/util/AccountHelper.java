package com.example.swan.util;

import android.content.Context;

public class AccountHelper {


    public static boolean isLogin(Context context){
       return !CommonUtil.isNullOrEmpty(readAuthToken(context));
    }
    public static void logout(Context context){
        context.getSharedPreferences("data",Context.MODE_PRIVATE).edit().remove("auth_token").apply();

    }
    public static void writeAuthToken(Context context,String authToken){
        context.getSharedPreferences("data",Context.MODE_PRIVATE).edit().putString("auth_token",authToken).apply();
    }
    public static String  readAuthToken(Context context){
      return   context.getSharedPreferences("data",Context.MODE_PRIVATE).getString("auth_token","");
    }
}
