package com.rewardscards.a1rc;

import android.app.Application;

import com.google.firebase.FirebaseApp;

public class MyappRc extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseApp.initializeApp(this);
    }
}
