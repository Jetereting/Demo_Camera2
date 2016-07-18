package us.eiyou.demo_camera.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import us.eiyou.demo_camera.R;
import us.eiyou.demo_camera.adpter.WaitUploadAdapter;
import us.eiyou.demo_camera.model.WaitUploadModel;
import us.eiyou.demo_camera.utils.Config;
import us.eiyou.demo_camera.utils.GetBitmapFromUrl;
import us.eiyou.demo_camera.utils.Http;
import us.eiyou.demo_camera.utils.IsNetwork;
import us.eiyou.demo_camera.utils.SP;

public class PublishHouseActivity extends Activity implements OnItemClickListener {

    Button btn_title_back;
    TextView tev_title_content;
    ListView listView;

    WaitUploadAdapter adapter;
    List<WaitUploadModel> list;
    String path;
    String selectPath = "";
    String telephone = "";
    String type;

    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e("PublishHouseActivity", "onCreate");
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_waitupload);


        if(!IsNetwork.isNetwork(getApplicationContext())){
            Toast.makeText(this, "网络不可用", Toast.LENGTH_SHORT).show();
        }

        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                .detectDiskReads().detectDiskWrites().detectNetwork()
                .penaltyLog().build());
        StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
                .detectLeakedSqlLiteObjects().detectLeakedClosableObjects()
                .penaltyLog().penaltyDeath().build());

        initView();
        new Thread(runnable).start();
        adapter = new WaitUploadAdapter(this, list);
        listView.setAdapter(adapter);
        type = getIntent().getStringExtra("type");
        switch (type) {
            case "trip_upload":
                tev_title_content.setText("旅游上传");
                break;
            case ("house_upload"):
//                tev_title_content.setText("360全景房产上传");
                tev_title_content.setText("选择房产图片");
                break;
            case ("house_upload7"):
//                tev_title_content.setText("720全景房产上传");
                tev_title_content.setText("选择房产图片");
                break;
        }

        findViewById(R.id.bt_upload).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog = new ProgressDialog(PublishHouseActivity.this);
                progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                progressDialog.setMessage("正在生成案例...");
                progressDialog.setIndeterminate(false);
                progressDialog.setCancelable(true);
                progressDialog.show();
                new Thread(runnableOfUpload).start();
            }
        });
        listView.setOnItemClickListener(this);
    }

    private void initView() {

        list = new ArrayList<>();

        listView = (ListView) findViewById(R.id.listview_waitupload);
        listView.setOnItemClickListener(this);
        btn_title_back = (Button) findViewById(R.id.btn_title_back);
        tev_title_content = (TextView) findViewById(R.id.tev_title_content);
        tev_title_content.setText("我的旅游上传");
        btn_title_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                finish();
            }
        });
    }

    Runnable runnable = new Runnable() {

        @Override
        public void run() {
            // TODO Auto-generated method stub
            JSONObject jsonO = new JSONObject();
            try {
                telephone = SP.getString(getApplicationContext(),"telephone");
                jsonO.put("serviceName", "findMyGoodsOrder");
                JSONObject jsonP = new JSONObject();
                jsonP.put("telphone", telephone);
                jsonO.put("queryParameters", jsonP);

            } catch (Exception e) {
                Toast.makeText(getApplicationContext(),
                        "获取数据失败:" + e.toString(), Toast.LENGTH_SHORT).show();
            }

            String input = jsonO.toString();
            Http http = new Http();
            String requst = http.Result(Config.login_url, input);
            JSONObject json = null;
            try {
                json = new JSONObject(requst);
            } catch (Exception e) {
                e.printStackTrace();
            }
            Message msg = new Message();
            msg.obj = json;
            handler.sendMessage(msg);
        }
    };
    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            try {
                JSONObject getjson = (JSONObject) msg.obj;
                int status = getjson.getInt("status");
                if (status == 1) {
                    JSONObject j = getjson.getJSONObject("dataList");
                    String url = j.getString("url");
                    path = j.getString("path");
                    String[] urls = url.split(",");
                    String[] paths = path.split(",");
                    for (int i = 0; i < urls.length; i++) {
                        Bitmap bitmap = GetBitmapFromUrl.getBitmap(urls[i]);
                        list.add(new WaitUploadModel(paths[i] + "", bitmap));
                        adapter.notifyDataSetChanged();
                    }
                } else {
                    String comment = getjson.getString("comment");
                    Toast.makeText(getApplicationContext(), comment,
                            Toast.LENGTH_LONG).show();
                }
            } catch (Exception e) {
                Toast.makeText(getApplicationContext(),
                        "遇到了些错误:" + e.toString(), Toast.LENGTH_SHORT).show();
            }
        }
    };

    @Override
    public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
        WaitUploadAdapter.ViewHolder holder = (WaitUploadAdapter.ViewHolder) arg1.getTag();
        holder.checkBox.toggle();
        WaitUploadAdapter.getIsSelected().put(position, holder.checkBox.isChecked());
    }

    Runnable runnableOfUpload = new Runnable() {

        @Override
        public void run() {
            JSONObject jsonO = new JSONObject();
            //			start get selectPath
            HashMap<Integer, Boolean> map = WaitUploadAdapter.getIsSelected();
            int flag=0;
            for (int i = 0; i < map.size(); i++) {
                if (map.get(i)) {
                    selectPath += list.get(i).getWaitupload_name() + ",";
                    flag=1;
                }
            }
            if(flag==1) {
                WaitUploadAdapter.getIsSelected().get("");
                //			end get path
                try {
                    jsonO.put("serviceName", "onloadProcess");
                    JSONObject jsonP = new JSONObject();
                    jsonP.put("telephone", telephone);
                    jsonP.put("photo", selectPath);
                    switch (type) {
                        case "trip_upload":
                            jsonP.put("type", 1);
                            break;
                        case ("house_upload"):
                            jsonP.put("type", 2);
                            break;
                        case ("house_upload7"):
                            jsonP.put("type", 3);
                            break;
                    }
                    jsonO.put("queryParameters", jsonP);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                String input = jsonO.toString();
                Http http = new Http();
                String requst = http.Result(Config.login_url, input);
                Log.d("PublishHouseActivity", "requst:"+requst.toString());
                JSONObject json = null;
                try {
                    json = new JSONObject(requst);
                    Log.d("PublishHouseActivity", "我是上传后的反馈" + json);
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.d("PublishHouseActivity", "我是上传后的反馈我没收到任何东西" + e.toString());
                }
                Message msg = new Message();
                msg.obj = json;
                handler1.sendMessage(msg);
            }else {
                Message msg = new Message();
                msg.obj = "0";
                handler1.sendMessage(msg);
                progressDialog.dismiss();
            }
        }
    };
    private Handler handler1 = new Handler() {
        public void handleMessage(Message msg) {
            Log.d("PublishHouseActivity", "msg.obj:" + msg.obj + "sssss");
            try {
                JSONObject getjson = (JSONObject) msg.obj;
                int status = getjson.getInt("status");
                if (status == 1) {
                    Toast.makeText(getApplicationContext(), "发布成功！", Toast.LENGTH_LONG).show();
                    PublishHouseActivity.this.finish();
                    Intent intent = new Intent(getApplicationContext(), MyHousePropertyActivity.class);
                    intent.putExtra("phone", telephone);
                    intent.putExtra("type", getIntent().getStringExtra("type"));
                    startActivity(intent);
                } else {
                    Toast.makeText(PublishHouseActivity.this, "发布失败 请检查图片", Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                Toast.makeText(getApplicationContext(), "发布成功！", Toast.LENGTH_LONG).show();
                PublishHouseActivity.this.finish();
                Intent intent = new Intent(getApplicationContext(), MyHousePropertyActivity.class);
                intent.putExtra("phone", telephone);
                intent.putExtra("type", getIntent().getStringExtra("type"));
                startActivity(intent);
            }
            progressDialog.dismiss();
        }
    };

}
