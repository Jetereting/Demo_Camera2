package us.eiyou.demo_camera.fragment;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;

import butterknife.ButterKnife;
import butterknife.OnClick;
import me.iwf.photopicker.PhotoPickerActivity;
import me.iwf.photopicker.utils.PhotoPickerIntent;
import us.eiyou.demo_camera.R;
import us.eiyou.demo_camera.activity.MainActivity;
import us.eiyou.demo_camera.utils.Config;
import us.eiyou.demo_camera.utils.CopyFiles;
import us.eiyou.demo_camera.utils.SP;

public class AlbumFragment extends Fragment {

    private ProgressDialog progress;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_album, container, false);

        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @OnClick(R.id.b)
    public void onClick() {
        PhotoPickerIntent intent = new PhotoPickerIntent(getActivity());
        intent.setPhotoCount(7);
        intent.setShowCamera(false);
        startActivityForResult(intent, 1);

        progress = new ProgressDialog(getActivity());
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.setMessage("正在上传。。。。");
        progress.setCancelable(false);
        progress.setIndeterminate(false);
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
                String photo=photos.get(i);
                Date date = new Date();
                long time = date.getTime();
                String dateline = time + "";
                try {
                    String phone = SP.getString(getActivity(),"telephone");
                    String geshi = photo.split("\\.")[1];
                    newImagePath = getActivity().getFilesDir().getAbsolutePath() + "/" + phone + "_" + dateline + "." + geshi;
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
                        if (null != getActivity().getIntent().getStringExtra("para")) {
                            url = new URL(Config.urlHead+"uploadfile/FileUploadServlet2?path=" + getActivity().getIntent().getStringExtra("para"));
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
                        dos.writeBytes("Content-Disposition: form-data; name=\"file\"; filename=\""+ newImagePath.substring(newImagePath.lastIndexOf("/") + 1)
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
                        int progress = (int) ((float) (i+1)/photos.size() * 100);
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
            Toast.makeText(getActivity(), "上传完成！", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(getActivity(),MainActivity.class));
        }
    }

}
