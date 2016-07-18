package us.eiyou.demo_camera.utils;

import android.util.Log;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

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


	public  static String requestData(String address ,String params){

		HttpURLConnection conn = null;
		try {
			// Create a trust manager that does not validate certificate chains
			TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager(){
				public X509Certificate[] getAcceptedIssuers(){return null;}
				public void checkClientTrusted(X509Certificate[] certs, String authType){}
				public void checkServerTrusted(X509Certificate[] certs, String authType){}
			}};

			// Install the all-trusting trust manager
			SSLContext sc = SSLContext.getInstance("TLS");
			sc.init(null, trustAllCerts, new SecureRandom());

			//ip host verify
			HostnameVerifier hv = new HostnameVerifier() {
				public boolean verify(String urlHostName, SSLSession session) {
					return urlHostName.equals(session.getPeerHost());
				}
			};

			//set ip host verify
			HttpsURLConnection.setDefaultHostnameVerifier(hv);

			HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

			URL url = new URL(address);
			conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("POST");// POST
			conn.setConnectTimeout(3000);
			conn.setReadTimeout(3000);
			// set params ;post params
			if (params!=null) {
				conn.setDoOutput(true);
				DataOutputStream out = new DataOutputStream(conn.getOutputStream());
				out.write(params.getBytes(Charset.forName("UTF-8")));
				out.flush();
				out.close();
			}
			conn.connect();
			//get result
			if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
				String result = parsRtn(conn.getInputStream());
				return result;
			} else {
				System.out.println(conn.getResponseCode() + " "+ conn.getResponseMessage());
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (conn != null)
				conn.disconnect();
		}
		return null;
	}
	/**
	 * 获取返回数据
	 * @param is
	 * @return
	 * @throws IOException
	 */
	private static String parsRtn(InputStream is) throws IOException{

		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		StringBuffer buffer = new StringBuffer();
		String line = null;
		boolean first = true;
		while ((line = reader.readLine()) != null) {
			if(first){
				first = false;
			}else{
				buffer.append("\n");
			}
			buffer.append(line);
		}
		return buffer.toString();
	}
}
