package com.noplayer.rumahsakit;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import java.util.HashMap;

public class SessionManager {

    SharedPreferences sharedPreferences;
    public SharedPreferences.Editor editor;
    public Context context;
    int PRIVATE_MODE = 0;

    private static final String PREF_NAME = "LOGIN";
    private static final String LOGIN = "IS_LOGIN";
    public static final String NAMA = "NAMA";
    public static final String USERNAME = "USERNAME";
    public static final String TGL_LAHIR = "TGL_LAHIR";
    public static final String NO_TELP = "NO_TELP";
    public static final String ALAMAT = "ALAMAT";
    public static final String ID = "ID";
    public static final String TOKEN = "TOKEN";

    public SessionManager(Context context) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = sharedPreferences.edit();
    }

    public void createSession(String nama, String username, String id, String token) {
        editor.putBoolean(LOGIN, true);
        editor.putString(NAMA, nama);
        editor.putString(USERNAME, username);
        editor.putString(ID, id);
        editor.putString(TOKEN, token);
        editor.apply();
    }

    public boolean isLoggin() {
        return sharedPreferences.getBoolean(LOGIN, false);
    }

    public void checkLogin() {
        if (!this.isLoggin()){
            Intent i = new Intent(context, LoginActivity.class);
            context.startActivity(i);
            ((Activity) context).finish();
        }
    }

    public HashMap<String, String> getUserDetail() {

        HashMap<String, String> user = new HashMap<>();
        user.put(NAMA, sharedPreferences.getString(NAMA, null));
        user.put(USERNAME, sharedPreferences.getString(USERNAME, null));
        user.put(ID, sharedPreferences.getString(ID, null));
        user.put(TOKEN, sharedPreferences.getString(TOKEN, null));

        return user;
    }

    public void logout() {
        editor.clear();
        editor.commit();
        Intent i = new Intent(context, LoginActivity.class);
        context.startActivity(i);
        ((Activity) context).finish();
    }

}
