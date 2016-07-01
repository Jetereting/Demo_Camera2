package us.eiyou.demo_camera.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.Volley;
import com.beardedhen.androidbootstrap.BootstrapButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.iwf.photopicker.PhotoPickerActivity;
import me.iwf.photopicker.utils.PhotoPickerIntent;
import us.eiyou.demo_camera.R;
import us.eiyou.demo_camera.utils.Config;
import us.eiyou.demo_camera.utils.CopyFiles;
import us.eiyou.demo_camera.utils.Http;
import us.eiyou.demo_camera.utils.SP;

//经纪人信息
public class AgentActivity extends AppCompatActivity {

    @Bind(R.id.photo)
    NetworkImageView niv_photo;
    @Bind(R.id.change_photo)
    TextView changePhoto;
    @Bind(R.id.phone)
    EditText phone;
    @Bind(R.id.company)
    EditText company;
    @Bind(R.id.b_submit)
    BootstrapButton bSubmit;
    @Bind(R.id.tev_app_title)
    TextView tevAppTitle;
    private ProgressDialog progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agent);
        ButterKnife.bind(this);
        tevAppTitle.setText("经纪人");
        getData();
    }

    private void getData() {
        niv_photo.setDefaultImageResId(R.mipmap.agent);
        RequestQueue mQueue = Volley.newRequestQueue(getApplicationContext());
        ImageLoader imageLoader = new ImageLoader(mQueue, new ImageLoader.ImageCache() {
            @Override
            public Bitmap getBitmap(String s) {
                return null;
            }

            @Override
            public void putBitmap(String s, Bitmap bitmap) {
            }
        });
        niv_photo.setImageUrl("http://115.159.41.95/uploadfile/ImagesUploaded/personphoto/" + SP.getString(getApplicationContext(), "telephone") + ".jpg", imageLoader);

        new Thread(new Runnable() {
            @Override
            public void run() {
                JSONObject jsonObject = new JSONObject();
                JSONObject queryParameters = new JSONObject();
                JSONObject result = null;
                try {
                    jsonObject.put("serviceName", "findByUserTelphone");
                    queryParameters.put("telphone", SP.getString(getApplicationContext(), "telephone"));
                    jsonObject.put("queryParameters", queryParameters);
                    result = new JSONObject(Http.Result(Config.login_url, jsonObject.toString()));
                    Log.d("AgentActivity", "result:" + result);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Message message = new Message();
                message.obj = result;
                handlerQuery.sendMessage(message);
            }
        }).start();
    }

    @OnClick({R.id.change_photo, R.id.b_submit})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.change_photo:
                PhotoPickerIntent intent = new PhotoPickerIntent(getApplicationContext());
                intent.setPhotoCount(1);
                intent.setShowCamera(false);
                startActivityForResult(intent, 1);

                progress = new ProgressDialog(AgentActivity.this);
                progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                progress.setMessage("正在上传。。。。");
                progress.setCancelable(false);
                progress.setIndeterminate(false);
                break;
            case R.id.b_submit:
                if (phone.getText().toString().length() != 0 && company.getText().toString().length() != 0) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            JSONObject jsonObject = new JSONObject();
                            JSONObject queryParameters = new JSONObject();
                            JSONObject result = null;
                            try {
                                jsonObject.put("serviceName", "personUpdate");
                                queryParameters.put("telephone", SP.getString(getApplicationContext(), "telephone"));
                                queryParameters.put("contacttelphone", phone.getText().toString());
                                queryParameters.put("company", company.getText().toString());
                                jsonObject.put("queryParameters", queryParameters);
                                result = new JSONObject(Http.Result(Config.login_url, jsonObject.toString()));
                                Message message = new Message();
                                message.obj = result;
                                handlerChange.sendMessage(message);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }).start();
                } else {
                    Toast.makeText(this, "请完善信息", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == -1 && requestCode == 1) {
            if (data != null) {
                progress.show();
                UploadPhotoTask task = new UploadPhotoTask();
                task.execute(data);
//                ArrayList<String> photos = data.getStringArrayListExtra(PhotoPickerActivity.KEY_SELECTED_PHOTOS);
//                for (String photo : photos) {
//                    niv_photo.setImageBitmap(ImageUtil.getImageFromLocal(photo));
//                }
            }
        }
    }

    class UploadPhotoTask extends AsyncTask<Intent, Integer, Void> {
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
                    String phone = SP.getString(getApplicationContext(), "telephone");
                    String geshi = photo.split("\\.")[1];
                    newImagePath = getApplicationContext().getFilesDir().getAbsolutePath() + "/" + phone + "_" + dateline + "." + geshi;
                    Log.e("newImagePath", newImagePath);
                    CopyFiles.copy(photo, newImagePath);
                    //						上传
                    final String uploadUrl = Config.urlHead + "uploadfile/PersonPhotoServlet";
                    Log.e("图片上传", "相册图片上传");
                    final String end = "\r\n";
                    final String twoHyphens = "--";        // 两个连字符
                    final String boundary = "******";        // 分界符的字符串
                    try {
                        URL url;
                        if (null != SP.getString(getApplicationContext(), "telephone")) {
                            url = new URL(Config.urlHead + "uploadfile/PersonPhotoServlet?telphone=" + SP.getString(getApplicationContext(), "telephone"));
                        } else {
                            url = new URL(uploadUrl);
                        }
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
            progress.setMessage("已上传" + values[0] + "%");
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            progress.dismiss();
            Toast.makeText(getApplicationContext(), "上传完成！", Toast.LENGTH_SHORT).show();
            finish();
            startActivity(new Intent(getApplicationContext(),AgentActivity.class));
        }
    }

    private Handler handlerQuery = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            JSONObject jsonObject = (JSONObject) msg.obj;
            try {
                int status = jsonObject.getInt("status");
                if (1 == status) {
                    JSONObject j = jsonObject.getJSONObject("dataList");
                    company.setText(j.getString("company"));
                    phone.setText(j.getString("contacttelphone"));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };
    private Handler handlerChange = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            JSONObject jsonObject = (JSONObject) msg.obj;
            try {
                int status = jsonObject.getInt("status");
                if (1 == status) {
                    Toast.makeText(AgentActivity.this, "修改成功！", Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };
}
