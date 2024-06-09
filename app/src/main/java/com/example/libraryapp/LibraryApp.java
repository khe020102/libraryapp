package com.example.libraryapp;

import android.app.Application;

public class LibraryApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        SessionManager.init(this);
    }
}

