package us.eiyou.demo_camera.utils;

import android.content.Context;
import android.net.ConnectivityManager;

public final class IsNetwork {
	
    public static boolean isNetwork(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if(connectivityManager == null) {
            return false;
        } 
        if( connectivityManager.getActiveNetworkInfo()==null){
            return false;
        }
         return     connectivityManager.getActiveNetworkInfo().isAvailable();
    }
}
