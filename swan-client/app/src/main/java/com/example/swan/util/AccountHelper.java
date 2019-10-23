package com.example.swan.util;

import android.content.Context;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.Set;

public class AccountHelper {

    private static final String HISTORY_SEARCH = "history_search";

    public static boolean isLogin(Context context) {
        return !CommonUtil.isNullOrEmpty(readAuthToken(context));
    }

    public static void logout(Context context) {
        context.getSharedPreferences("data", Context.MODE_PRIVATE).edit().remove("auth_token").apply();

    }

    public static void writeAuthToken(Context context, String authToken) {
        context.getSharedPreferences("data", Context.MODE_PRIVATE).edit().putString("auth_token", authToken).apply();
    }

    public static String readAuthToken(Context context) {
        return context.getSharedPreferences("data", Context.MODE_PRIVATE).getString("auth_token", "");
    }

    public static void clearHistorySearch(Context context) {
        context.getSharedPreferences("data", Context.MODE_PRIVATE).edit().remove(HISTORY_SEARCH).apply();
    }

    public static void writeHistorySearch(Context context, String history) {


        Set<String> tempSet = context.getSharedPreferences("data", Context.MODE_PRIVATE).getStringSet(HISTORY_SEARCH, null);
        LinkedHashSet<String> stringSet;
        if (tempSet == null) {
            stringSet = new LinkedHashSet<>();
        } else {
            stringSet = new LinkedHashSet<>(tempSet);
        }


        context.getSharedPreferences("data", Context.MODE_PRIVATE).edit().remove(HISTORY_SEARCH).apply();

        stringSet.add(history);
        context.getSharedPreferences("data", Context.MODE_PRIVATE).edit().putStringSet(HISTORY_SEARCH, stringSet).apply();
    }

    public static LinkedList<String> getHistorySearch(Context context) {
        Set<String> stringSet = context.getSharedPreferences("data", Context.MODE_PRIVATE).getStringSet(HISTORY_SEARCH, null);
        if (stringSet == null) {
            return new LinkedList<>();
        } else {
            return new LinkedList<>(stringSet);
        }
    }

}
