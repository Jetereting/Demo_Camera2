package us.eiyou.demo_camera.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.beardedhen.androidbootstrap.BootstrapEditText;
import com.zhy.android.percent.support.PercentLinearLayout;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
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

public class EditSeniorTuwenEditActivity extends AppCompatActivity {

    @Bind(R.id.tev_app_title)
    TextView tevAppTitle;
    @Bind(R.id.tv_user)
    TextView tvUser;

    @Bind(R.id.et_title)
    BootstrapEditText etTitle;
    @Bind(R.id.et_content)
    BootstrapEditText etContent;
    @Bind(R.id.b_photo)
    BootstrapButton bPhoto;
    @Bind(R.id.et_music)
    BootstrapEditText etMusic;
    @Bind(R.id.et_link)
    BootstrapEditText etLink;
    @Bind(R.id.layout)
    PercentLinearLayout layout;
    private ProgressDialog progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_senior_tuwen_edit);
        ButterKnife.bind(this);
        tevAppTitle.setText("高级编辑-图文描述");
        tvUser.setText(SP.getString(getApplicationContext(), "telephone")+"\t");
    }

    @OnClick({ R.id.b_confirm, R.id.b_photo})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.b_confirm:
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        String content = null;
                        String title = null;
                        try {
                            content = URLEncoder.encode(etContent.getText().toString(), "UTF-8");
                            title = URLEncoder.encode(etTitle.getText().toString(), "UTF-8");
                        } catch (UnsupportedEncodingException e1) {
                            // TODO Auto-generated catch block
                            e1.printStackTrace();
                        }
                        String url = Config.urlHead + "uploadfile/PictureUpdateServlet?path=" + getIntent().getStringExtra("para") + "&num=" +getIntent().getIntExtra("position",0) + "&oper=update&title=" + title + "&content=" + content;
                        Log.d("EditSeniorTuwenEditActi", url);
                        Http.submit(url);
                    }
                }).start();
                Toast.makeText(this, "修改成功~", Toast.LENGTH_SHORT).show();
                finish();
                startActivity(new Intent(getApplicationContext(),EditSeniorTuwenActivity.class).putExtra("para",getIntent().getStringExtra("para")).putExtra("position",getIntent().getIntExtra("position",0)).putExtra("title",etTitle.getText().toString()).putExtra("list",getIntent().getSerializableExtra("list")));
                break;
            case R.id.b_photo:
                PhotoPickerIntent intent = new PhotoPickerIntent(getApplicationContext());
                intent.setPhotoCount(1);
                intent.setShowCamera(false);
                startActivityForResult(intent, 1);
                progress = new ProgressDialog(EditSeniorTuwenEditActivity.this);
                progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                progress.setMessage("正在上传。。。。");
                progress.setCancelable(false);
                progress.setIndeterminate(false);
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
                    newImagePath = getFilesDir().getAbsolutePath() + "/" + phone + "_" + dateline + "." + geshi;
                    Log.e("newImagePath", newImagePath);
                    CopyFiles.copy(photo, newImagePath);
                    //						上传
                    final String uploadUrl = Config.urlHead + "uploadfile/FileUploadServlet";
                    Log.e("图片上传", "相册图片上传");
                    final String end = "\r\n";
                    final String twoHyphens = "--";        // 两个连字符
                    final String boundary = "******";        // 分界符的字符串
                    try {
                        URL url;
                        if (null != getIntent().getStringExtra("para")) {
                            url = new URL(Config.urlHead + "uploadfile/FileUploadServlet2?path=" + getIntent().getStringExtra("para") + "_" + getIntent().getIntExtra("position",0));
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
            progress.setMessage("已上传" + values[0] + "%");
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            progress.dismiss();
            Toast.makeText(getApplicationContext(), "上传完成！", Toast.LENGTH_SHORT).show();
            bPhoto.setText("上传完成！");
        }
    }
}
