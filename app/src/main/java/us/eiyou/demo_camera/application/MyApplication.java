package us.eiyou.demo_camera.application;

import android.app.Application;

import com.beardedhen.androidbootstrap.TypefaceProvider;

import cn.smssdk.SMSSDK;

/**
 * Created by Au on 2016/3/30.
 */
public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        SMSSDK.initSDK(this, "1493b5e881b45", "9998f1584c5b1defbab0d89a7e760cc7");
        TypefaceProvider.registerDefaultIconSets();
    }
}
