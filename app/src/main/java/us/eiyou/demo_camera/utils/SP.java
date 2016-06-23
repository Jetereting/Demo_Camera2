package us.eiyou.demo_camera.utils;

import android.content.Context;

/**
 * Created by Au on 2016/3/19.
 */
public class SP {
    public static void put(Context context,String key,String value){
        context.getSharedPreferences("user", Context.MODE_PRIVATE).edit().putString(key, value).commit();
    }
    public static void put(Context context,String key,int value){
        context.getSharedPreferences("user", Context.MODE_PRIVATE).edit().putInt(key, value).commit();
    }
    public static int getInt(Context context,String key){
        return context.getSharedPreferences("user", Context.MODE_PRIVATE).getInt(key, 0);
    }
    public static String  getString(Context context,String key){
        return context.getSharedPreferences("user", Context.MODE_PRIVATE).getString(key, "");
    }

}
