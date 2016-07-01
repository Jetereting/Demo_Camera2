package us.eiyou.demo_camera.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import us.eiyou.demo_camera.R;
import us.eiyou.demo_camera.adpter.LookShareAdpter;
import us.eiyou.demo_camera.model.WaitUploadModel;
import us.eiyou.demo_camera.utils.Config;
import us.eiyou.demo_camera.utils.GetBitmapFromUrl;
import us.eiyou.demo_camera.utils.Http;
import us.eiyou.demo_camera.utils.IsNetwork;
import us.eiyou.demo_camera.utils.SP;


public class LookShareActivity extends Activity implements LookShareAdpter.Callback {

    Button btn_title_back;
    TextView tev_title_content;
    ListView listView;
    LookShareAdpter mAdapter;
    List<WaitUploadModel> list = new ArrayList<WaitUploadModel>();

    String type;
    String path;
    String telephone = "";
    String[] url;
    ArrayList<Integer> num = new ArrayList<Integer>();
    String urlList[];
    String paraList[];
    ProgressDialog progressDialog;



    @SuppressWarnings({"unchecked", "rawtypes"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e("LookShareActivity", "onCreate");
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_lookshare);

        if (!IsNetwork.isNetwork(getApplicationContext())) {
            Toast.makeText(this, "网络不可用", Toast.LENGTH_SHORT).show();
        }

        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                .detectDiskReads().detectDiskWrites().detectNetwork()
                .penaltyLog().build());
        StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
                .detectLeakedSqlLiteObjects().detectLeakedClosableObjects()
                .penaltyLog().penaltyDeath().build());

        initView();
        type = getIntent().getStringExtra("type");

        if (type.equals("trip_share")) {
            mAdapter = new LookShareAdpter(this, list, 1, this);
            listView.setAdapter(mAdapter);
            tev_title_content.setText("旅游分享");
        } else if (("house_share").equals(type)) {
            mAdapter = new LookShareAdpter(this, list, 2, this);
            listView.setAdapter(mAdapter);
//            tev_title_content.setText("360全景房产分享");
            tev_title_content.setText("我的房产库");
        } else if (("house_share7").equals(type)) {
            mAdapter = new LookShareAdpter(this, list, 2, this);
            listView.setAdapter(mAdapter);
//            tev_title_content.setText("720全景房产分享");
            tev_title_content.setText("我的房产库");
        }
        progressDialog = new ProgressDialog(LookShareActivity.this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setMessage("正在获取数据...");
        progressDialog.setIndeterminate(false);
        progressDialog.setCancelable(true);
        progressDialog.show();
        LookShareTask task = new LookShareTask();
        task.execute();
    }

    private void initView() {
        listView = (ListView) findViewById(R.id.listview_lookshare);
        btn_title_back = (Button) findViewById(R.id.btn_title_back);
        tev_title_content = (TextView) findViewById(R.id.tev_title_content);
        tev_title_content.setText("查看分享");
        btn_title_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                finish();
            }
        });
    }

    class LookShareTask extends AsyncTask<Void, Integer, List<WaitUploadModel>> {
        @Override
        protected List<WaitUploadModel> doInBackground(Void... params) {
            JSONObject jsonO = new JSONObject();
            try {
                telephone = SP.getString(getApplicationContext(), "telephone");
                jsonO.put("serviceName", "findMyGoodsOrder2");
                JSONObject jsonP = new JSONObject();
                jsonP.put("telphone", telephone);
                switch (type) {
                    case "trip_share":
                        jsonP.put("type", 1);
                        break;
                    case ("house_share"):
                        jsonP.put("type", 2);
                        break;
                    case ("house_share7"):
                        jsonP.put("type", 3);
                        break;
                }
                jsonO.put("queryParameters", jsonP);
            } catch (Exception e) {
                e.printStackTrace();
            }
            String input = jsonO.toString();
            Log.d("LookShareTask", input);
            String requst = Http.Result(Config.login_url, input);
            JSONObject json = null;
            try {
                json = new JSONObject(requst);
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                JSONObject getjson = json;
                Log.d("LookShareTask", "json:" + json);
                JSONObject j = getjson.getJSONObject("dataList");
                path = j.getString("path");
                String url11 = j.getString("url");
                urlList = url11.split(";");
                String para = j.getString("para");
                paraList = para.split(";");

                String addtime = j.getString("addtime");
                String name = j.getString("name");

                url = path.split(";");
                for (int i = 0; i < url.length; i++) {
                    String photoPath[] = url[i].split(",");
                    num.add(i, photoPath.length);
                    for (int j1 = 0; j1 < photoPath.length; j1++) {
                        int progress = (int) ((float) (i + 1) / url.length * 100);
                        // 通知更新进度
                        publishProgress(progress);
                    }
                    list.add(new WaitUploadModel(url[i] + "", GetBitmapFromUrl.getBitmap(photoPath[0]),name.split(";")[i],addtime.split(";")[i]));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return list;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            progressDialog.setMessage("已加载 " + values[0] + "%");
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(List<WaitUploadModel> list) {
            mAdapter.notifyDataSetChanged();
            progressDialog.dismiss();
            super.onPostExecute(list);
        }
    }


    @Override
    public void click(final View v) {
        switch (v.getId()) {
            //		查看分享
            case R.id.button:
                Intent intent = new Intent();
                intent.setClass(getApplicationContext(), ShowShareActivity.class);
                intent.putExtra("url", urlList[(Integer) v.getTag()]);
                startActivity(intent);
                break;
            //			edit
            case R.id.button1:
                Intent intent1 = new Intent();
                String str[] = paraList[(Integer) v.getTag()].split("_");
                String url = Config.urlHead + "uploadfile/ImagesUploaded/" + str[0] + "_3/" + str[1] + "/vtour/vr-go/1.jpg";
                if (("house_share").equals(type)) {
                    intent1.putExtra("type", "house_share");
                } else if (("house_share7").equals(type)) {
                    intent1.putExtra("type", "house_share7");
                }
                intent1.putExtra("urlShare", urlList[(Integer) v.getTag()]);
                intent1.putExtra("url", url);
                intent1.putExtra("num", num.get((Integer) v.getTag()) + "");
                intent1.putExtra("para", paraList[(Integer) v.getTag()]);
                Log.d("LookShareActivity", v.getTag().toString());
                intent1.putExtra("photoPaths", path.split(";")[(Integer) v.getTag()]);
                intent1.setClass(getApplicationContext(), EditIndexActivity.class);
                startActivity(intent1);
                break;
//           delete
            case R.id.button2:
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        JSONObject jsonObject = new JSONObject();
                        JSONObject queryParameters = new JSONObject();
                        JSONObject result = null;
                        try {
                            jsonObject.put("serviceName", "deleteAddress");
                            queryParameters.put("telphone", SP.getString(getApplicationContext(), "telephone"));
                            queryParameters.put("path", paraList[(Integer) v.getTag()].split("_")[1]);
                            type = getIntent().getStringExtra("type");
                            switch (type) {
                                case "trip_share":
                                    queryParameters.put("type", 1);
                                    break;
                                case ("house_share"):
                                    queryParameters.put("type", 2);
                                    break;
                                case ("house_share7"):
                                    queryParameters.put("type", 3);
                                    break;
                            }
                            jsonObject.put("queryParameters", queryParameters);
                            result = new JSONObject(Http.Result(Config.login_url, jsonObject.toString()));
                            Log.d("LookShareActivity", " result.toString()" + result.toString());
                        } catch (Exception e) {
                            Log.d("LookShareActivity", "e:" + e.toString());
                            e.printStackTrace();
                        }
                        Message message = new Message();
                        message.obj = result;
                        handlerDelete.sendMessage(message);
                    }
                }).start();
                break;
        }
    }

    private Handler handlerDelete = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            try {
                JSONObject jsonObject = (JSONObject) msg.obj;
                int status = jsonObject.getInt("status");
                if (1 == status) {
                    Toast.makeText(getApplicationContext(), "删除成功！", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getApplicationContext(), LookShareActivity.class).putExtra("type", getIntent().getStringExtra("type")));
                    finish();
                } else {
                    Toast.makeText(getApplicationContext(), jsonObject.getString("comment"), Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                e.printStackTrace();
                Log.d("LookShareActivity", e.toString());
            }
        }
    };


    @Override
    protected void onRestart() {
        list.clear();
        LookShareTask task = new LookShareTask();
        task.execute();
        super.onRestart();
    }


    @Override
    public void onBackPressed() {
        finish();
    }
}
