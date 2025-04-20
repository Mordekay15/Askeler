package com.example.project;

import android.app.Application;
import android.content.Context;
import android.os.StrictMode;

import org.osmdroid.config.Configuration;

import java.io.File;


public class AskelerApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        Context ctx = getApplicationContext();
        Configuration.getInstance().setUserAgentValue(getPackageName());

        File cacheDir = new File(getCacheDir(), "osmdroid");
        Configuration.getInstance().setOsmdroidTileCache(cacheDir);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
    }
}
