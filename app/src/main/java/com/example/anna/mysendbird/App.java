package com.example.anna.mysendbird;

import android.app.Application;

import com.sendbird.android.SendBird;

public class App extends Application {

    public static String APP_ID = "00FA0A28-0965-484B-B21C-467078E781BE";

    @Override
    public void onCreate() {
        super.onCreate();

        SendBird.init(APP_ID, this);

    }
}
