package us.eiyou.demo_camera.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.beardedhen.androidbootstrap.BootstrapButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import us.eiyou.demo_camera.R;
import us.eiyou.demo_camera.adpter.EditSeniorTuwenAdapter;
import us.eiyou.demo_camera.utils.Config;
import us.eiyou.demo_camera.utils.Http;
import us.eiyou.demo_camera.utils.SP;

public class EditSeniorTuwenActivity extends AppCompatActivity {


    public Intent getIntent;
    @Bind(R.id.tev_app_title)
    TextView tevAppTitle;
    @Bind(R.id.tv_user)
    TextView tvUser;
    @Bind(R.id.lv)
    ListView lv;
    @Bind(R.id.b_add)
    BootstrapButton bAdd;

    ArrayList list;
    EditSeniorTuwenAdapter arrayAdapter;
    AppCompatActivity activity;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            JSONObject jsonObject = (JSONObject) msg.obj;
            Log.d("EditSeniorTuwenActivity", jsonObject.toString());
            try {
                int status = jsonObject.getInt("status");
                if (1 == status) {
                    JSONObject dataList=jsonObject.getJSONObject("dataList");
                    String titles=dataList.getString("title");
                    for (String s : titles.split(";")) {
                        list.add(s);
                        Log.d("EditSeniorTuwenActivity","///" +s);
                    }
                    arrayAdapter.notifyDataSetChanged();
                }else {
                    Toast.makeText(EditSeniorTuwenActivity.this,"暂无描述~",Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_senior_tuwen);
        ButterKnife.bind(this);
        activity=this;
        tevAppTitle.setText("高级编辑-图文描述");
        tvUser.setText(SP.getString(getApplicationContext(), "telephone")+"\t");
        getIntent = getIntent();
        list = new ArrayList<>();
        arrayAdapter = new EditSeniorTuwenAdapter(getApplicationContext(), list, getIntent,activity);
        lv.setAdapter(arrayAdapter);
        getTuwenInfo();
    }

    @OnClick({R.id.b_add})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.b_add:
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        String url = Config.urlHead + "uploadfile/PictureServlet?path=" + getIntent().getStringExtra("para") + "&num=" +(list.size()-1) + "&title=" + "title" + "&content=" + "content";
                        Http.submit(url);
                    }
                }).start();
                list.add("图文");
                arrayAdapter.notifyDataSetChanged();
                break;
        }
    }

    public void getTuwenInfo(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                JSONObject jsonObject = new JSONObject();
                JSONObject queryParameters = new JSONObject();
                JSONObject result = null;
                try {
                    jsonObject.put("serviceName", "findMyPicture");
                    queryParameters.put("telphone",SP.getString(getApplicationContext(),"telephone"));
                    queryParameters.put("path", getIntent().getStringExtra("para").split("_")[1]);
                    jsonObject.put("queryParameters", queryParameters);
                    Log.d("EditSeniorTuwenActivity", jsonObject.toString());
                    result = new JSONObject(Http.Result(Config.login_url, jsonObject.toString()));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Message message = new Message();
                message.obj = result;
                handler.sendMessage(message);
            }
        }).start();
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
