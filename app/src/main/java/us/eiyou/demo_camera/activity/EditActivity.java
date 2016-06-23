package us.eiyou.demo_camera.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.beardedhen.androidbootstrap.BootstrapEditText;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;

import me.iwf.photopicker.PhotoPickerActivity;
import me.iwf.photopicker.utils.PhotoPickerIntent;
import us.eiyou.demo_camera.R;
import us.eiyou.demo_camera.utils.Config;
import us.eiyou.demo_camera.utils.CopyFiles;
import us.eiyou.demo_camera.utils.SP;

public class EditActivity extends Activity {
	BootstrapEditText et_title,et_telphone,et_comment;
	BootstrapButton b_upload,b_upload_map,b_loc_map,b_arrow;
	String uploadUrl;
	TextView tev_app_title;
	private ProgressDialog progress;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_edit);
		initView();
		b_upload.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				String comment = null;
				String title=null;
				try {
					comment=java.net.URLEncoder.encode(et_comment.getText().toString(),"UTF-8");
					title=java.net.URLEncoder.encode(et_title.getText().toString(),"UTF-8");
				} catch (UnsupportedEncodingException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				uploadUrl=Config.urlHead+"uploadfile/Dom6j?path="+getIntent().getStringExtra("para")+"&telphone="+et_telphone.getText().toString()+"&comment="+comment+"&title="+title;
				Log.e("uploadUrl",uploadUrl);
				try{
					URL url = new URL(uploadUrl);
	                  HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
	                  httpURLConnection.setDoInput(true);
	                  httpURLConnection.setDoOutput(true);
	                  httpURLConnection.setUseCaches(false);
	                  httpURLConnection.setRequestMethod("POST");
	                  httpURLConnection.setRequestProperty("Connection", "Keep-Alive");
	                  httpURLConnection.setRequestProperty("Charset", "GB2312");
	                  Log.e("responseCode ",httpURLConnection.getResponseCode()+"");
	                  httpURLConnection.connect();
	                  Toast.makeText(getApplicationContext(), "处理完毕！", Toast.LENGTH_SHORT).show();
	                  finish();
				}catch(Exception e){
					Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
				}
			}
		});
		b_upload_map.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
//				startActivity(new Intent(getApplicationContext(),com.king.photo.activity.MainActivity.class).putExtra("para", getIntent().getStringExtra("para")).putExtra("type", getIntent().getStringExtra("type")));
				PhotoPickerIntent intent = new PhotoPickerIntent(getApplicationContext());
				intent.setPhotoCount(1);
				intent.setShowCamera(false);
				startActivityForResult(intent, 1);

				progress = new ProgressDialog(EditActivity.this);
				progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
				progress.setMessage("正在上传。。。。");
				progress.setCancelable(false);
				progress.setIndeterminate(false);

			}
		});

		b_loc_map.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				startActivity(new Intent(getApplicationContext(),ScenesNameEditActivity.class).putExtra("para", getIntent().getStringExtra("para")).putExtra("url", getIntent().getStringExtra("url")).putExtra("num", getIntent().getStringExtra("num")).putExtra("type", getIntent().getStringExtra("type")));
			}
		});
		b_arrow.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				startActivity(new Intent(getApplicationContext(),MapArrowActivity.class).putExtra("para",getIntent().getStringExtra("para"))
						.putExtra("photoPaths",getIntent().getStringExtra("photoPaths")));
			}
		});
	}
	@Override
	public void onActivityResult(int requestCode, int resultCode, final Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == -1 && requestCode == 1) {
			if (data != null) {
				progress.show();
                UploadPhotoTask task=new UploadPhotoTask();
				task.execute(data);
			}
		}
	}
	class UploadPhotoTask extends AsyncTask<Intent,Integer,Void> {
		@Override
		protected Void doInBackground(Intent... params) {
            ArrayList<String> photos = params[0].getStringArrayListExtra(PhotoPickerActivity.KEY_SELECTED_PHOTOS);
            String newImagePath = "";
            for (int i = 0; i < photos.size(); i++) {
                String photo = photos.get(i);
                Date date = new Date();
                long time = date.getTime();
                String dateline = time + "";
                try {
                    String phone = SP.getString(getApplicationContext(),"telephone");
                    String geshi = photo.split("\\.")[1];
                    newImagePath = getFilesDir().getAbsolutePath() + "/" + phone + "_" + dateline + "." + geshi;
                    Log.e("newImagePath", newImagePath);
                    CopyFiles.copy(photo, newImagePath);
                    //						上传
                    final String uploadUrl = Config.urlHead+"uploadfile/FileUploadServlet";
                    Log.e("图片上传", "相册图片上传");
                    final String end = "\r\n";
                    final String twoHyphens = "--";        // 两个连字符
                    final String boundary = "******";        // 分界符的字符串
                    try {
                        URL url;
                        if (null != getIntent().getStringExtra("para")) {
                            url = new URL(Config.urlHead+"uploadfile/FileUploadServlet2?path=" + getIntent().getStringExtra("para"));
                        } else {
                            url = new URL(uploadUrl);
                        }

                        Log.d("UploadPhotoTask", "url:" + url);

                        HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                        httpURLConnection.setDoInput(true);
                        httpURLConnection.setDoOutput(true);
                        httpURLConnection.setUseCaches(false);
                        httpURLConnection.setRequestMethod("POST");
                        // 设置Http请求头
                        httpURLConnection.setRequestProperty("Connection", "Keep-Alive");
                        httpURLConnection.setRequestProperty("Charset", "UTF-8");
                        //  必须在Content-Type 请求头中指定分界符中的任意字符串
                        httpURLConnection.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);

                        //定义数据写入流，准备上传文件
                        DataOutputStream dos = new DataOutputStream(httpURLConnection.getOutputStream());
                        dos.writeBytes(twoHyphens + boundary + end);
                        //设置与上传文件相关的信息
                        dos.writeBytes("Content-Disposition: form-data; name=\"file\"; filename=\"" + newImagePath.substring(newImagePath.lastIndexOf("/") + 1)
                                + "\"" + end);
                        dos.writeBytes(end);

                        FileInputStream fis = new FileInputStream(newImagePath);
                        byte[] buffer = new byte[8192]; // 8k
                        int count = 0;
                        // 读取文件夹内容，并写入OutputStream对象
                        while ((count = fis.read(buffer)) != -1) {
                            dos.write(buffer, 0, count);
                        }
                        fis.close();
                        int progress = (int) ((float) (i + 1) / photos.size() * 100);
                        // 通知更新进度
                        publishProgress(progress);
                        dos.writeBytes(end);
                        dos.writeBytes(twoHyphens + boundary + twoHyphens + end);
                        dos.flush();
                        // 开始读取从服务器传过来的信息
                        InputStream is = httpURLConnection.getInputStream();
                        InputStreamReader isr = new InputStreamReader(is, "utf-8");
                        BufferedReader br = new BufferedReader(isr);
                        String result = br.readLine();
                        dos.close();
                        is.close();
                    } catch (Exception e) {
                        Log.e("myUpload", e.toString());
                    }
//						上传 end
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

		@Override
		protected void onProgressUpdate(Integer... values) {
			super.onProgressUpdate(values);
			progress.setMessage("已上传"+values[0]+"%");
		}

		@Override
		protected void onPostExecute(Void aVoid) {
			progress.dismiss();
			Toast.makeText(getApplicationContext(), "上传完成！", Toast.LENGTH_SHORT).show();
			finish();
		}
	}

	public void initView(){
		et_title=(BootstrapEditText)findViewById(R.id.et_title);
		et_telphone=(BootstrapEditText)findViewById(R.id.et_telphone);
		et_comment=(BootstrapEditText)findViewById(R.id.et_comment);
		b_upload=(BootstrapButton)findViewById(R.id.b_upload);
		b_arrow=(BootstrapButton)findViewById(R.id.b_arrow);
		b_upload_map=(BootstrapButton)findViewById(R.id.b_upload_map);
		b_loc_map=(BootstrapButton)findViewById(R.id.b_loc_map);
		tev_app_title=(TextView)findViewById(R.id.tev_app_title);
		if("house_share".equals(getIntent().getStringExtra("type"))){
			tev_app_title.setText("360房产编辑");
		}else if("house_share7".equals(getIntent().getStringExtra("type"))){
			tev_app_title.setText("720房产编辑");
		}
	}
	

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
        }
        return true;
    }
}
