package com.test.anton.windforecast;

import android.app.Application;
import android.content.Context;

import timber.log.Timber;

public class CustomApplication extends Application {

    private Context mAppContext;
    private static CustomApplication sCustomApplication;

    @Override
    public void onCreate() {
        super.onCreate();

        sCustomApplication = this;
        mAppContext = this;

        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        }

        Timber.d("Application Created");
    }

    public static CustomApplication getInstance() {
        return sCustomApplication;
    }

    public Context getAppContext() {
        return mAppContext;
    }
}
