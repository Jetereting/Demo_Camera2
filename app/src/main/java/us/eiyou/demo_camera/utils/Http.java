package us.eiyou.demo_camera.utils;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class Http{
	public static String Result(String url,String params){
		URL u = null;
    	HttpURLConnection con = null;

    	try {
	    	u = new URL(url);	
	    	con = (HttpURLConnection)u.openConnection();	
	    	con.setRequestMethod("POST");	
	    	con.setDoOutput(true);	
	    	con.setDoInput(true);
	    	con.setUseCaches(false);
	    	con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");	
	    	OutputStreamWriter osw = new OutputStreamWriter(con.getOutputStream(), "UTF-8");	
	    	osw.write(params);	
	    	osw.flush();	
	    	osw.close();
    	} catch (Exception e) {
    		e.printStackTrace();
    	} finally {
	    	if (con != null) {
	    	}
    	}
    	
    	StringBuffer buffer = new StringBuffer();
    	try {

	    	BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream(), "UTF-8"));
	    	String temp;
	    	while ((temp = br.readLine()) != null) {
	    		buffer.append(temp);
	    	}
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    	String response=buffer.toString();  	
    	return response;
	}
	public static void submit(String url) {
		try {
			URL url7 = new URL(url);
			HttpURLConnection httpURLConnection = (HttpURLConnection) url7.openConnection();
			httpURLConnection.setDoInput(true);
			httpURLConnection.setDoOutput(true);
			httpURLConnection.setUseCaches(false);
			httpURLConnection.setRequestMethod("POST");
			httpURLConnection.setRequestProperty("Connection", "Keep-Alive");
//			httpURLConnection.setRequestProperty("Charset", "GB2312");
			httpURLConnection.setRequestProperty("Charset", "UTF-8");
			Log.e("responseCode ", httpURLConnection.getResponseCode() + "");
			httpURLConnection.connect();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
