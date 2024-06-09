package com.example.libraryapp;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

public class SessionManager {

    public static String getAccessToken() {
        return sharedPreferences.getString(ACCESS_TOKEN_KEY, null);
    }

    private static final String PREF_NAME = "MyPrefs";
    private static final String ACCESS_TOKEN_KEY = "access_token";

    private static SharedPreferences sharedPreferences;

    public static void init(Application application) {
        sharedPreferences = application.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    public static void saveAccessToken(String token) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(ACCESS_TOKEN_KEY, token);
        editor.apply();
    }

    public static boolean isLoggedIn() {
        String accessToken = sharedPreferences.getString(ACCESS_TOKEN_KEY, null);
        return accessToken != null;
    }

    public static void logout() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(ACCESS_TOKEN_KEY);
        editor.apply();
    }
}
