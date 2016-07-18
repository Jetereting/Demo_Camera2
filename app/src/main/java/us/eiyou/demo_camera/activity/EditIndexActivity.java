package us.eiyou.demo_camera.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.beardedhen.androidbootstrap.BootstrapButton;

import org.json.JSONObject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;
import us.eiyou.demo_camera.R;
import us.eiyou.demo_camera.utils.Config;
import us.eiyou.demo_camera.utils.Http;
import us.eiyou.demo_camera.utils.SP;

//我的房产库里的 编辑
public class EditIndexActivity extends AppCompatActivity {

    @Bind(R.id.b_basic)
    BootstrapButton bBasic;
    @Bind(R.id.b_senior)
    BootstrapButton bSenior;
    @Bind(R.id.tev_title_content)
    TextView tevTitleContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_index);
        ButterKnife.bind(this);

        Log.d("EditIndexActivity", getIntent().getStringExtra("imgUrl"));

        tevTitleContent.setText("编辑");
    }

    @OnClick({R.id.b_basic, R.id.b_senior, R.id.b_share, R.id.b_house_resources,R.id.btn_title_back})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.b_basic:
                startActivity(new Intent(getApplicationContext(), EditBasicActivity.class).putExtra("para", getIntent().getStringExtra("para")).putExtra("url", getIntent().getStringExtra("url")).putExtra("num", getIntent().getStringExtra("num")).putExtra("type", getIntent().getStringExtra("type")).putExtra("photoPaths", getIntent().getStringExtra("photoPaths")));
                break;
            case R.id.b_senior:
                startActivity(new Intent(getApplicationContext(), EditSeniorActivity.class).putExtra("para", getIntent().getStringExtra("para")).putExtra("url", getIntent().getStringExtra("url")).putExtra("num", getIntent().getStringExtra("num")).putExtra("type", getIntent().getStringExtra("type")).putExtra("photoPaths", getIntent().getStringExtra("photoPaths")));
                break;
            case R.id.b_house_resources:
                startActivity(new Intent(getApplicationContext(), HouseResourcesActivity.class).putExtra("para", getIntent().getStringExtra("para")).putExtra("url", getIntent().getStringExtra("url")).putExtra("num", getIntent().getStringExtra("num")).putExtra("type", getIntent().getStringExtra("type")).putExtra("photoPaths", getIntent().getStringExtra("photoPaths")));
                break;
            case R.id.b_share:
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        JSONObject jsonObject = new JSONObject();
                        JSONObject queryParameters = new JSONObject();
                        JSONObject result = null;
                        try {
                            jsonObject.put("serviceName", "findMyGoodsOrder3");
                            queryParameters.put("telphone", SP.getString(getApplicationContext(), "telephone"));
                            queryParameters.put("path", getIntent().getStringExtra("para").split("_")[1]);
                            jsonObject.put("queryParameters", queryParameters);
                            result = new JSONObject(Http.Result(Config.login_url, jsonObject.toString()));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        Message message = new Message();
                        message.obj = result;
                        handler.sendMessage(message);
                    }
                }).start();
                break;
            case R.id.btn_title_back:finish();break;
        }
    }

    private void showShare(String shareTitle) {
        ShareSDK.initSDK(this);
        OnekeyShare oks = new OnekeyShare();
        oks.disableSSOWhenAuthorize();
        oks.setTitle(shareTitle);
        oks.setTitleUrl(getIntent().getStringExtra("urlShare"));
        oks.setText(getResources().getString(R.string.app_introduction));
        oks.setImageUrl(getIntent().getStringExtra("imgUrl"));
        oks.setUrl(getIntent().getStringExtra("urlShare"));
        oks.setComment(getIntent().getStringExtra("name"));
        oks.setSite(getString(R.string.app_name));
        oks.setSiteUrl(getIntent().getStringExtra("urlShare"));
        oks.show(this);
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    protected void onDestroy() {
        ShareSDK.stopSDK(this);
        super.onDestroy();
    }
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            String shareTitle="";
            Log.d("HouseResourcesActivity", msg.toString());
            try {
                JSONObject jsonObject = (JSONObject) msg.obj;
                int status = jsonObject.getInt("status");
                if (1 == status) {
                    JSONObject dataList = jsonObject.getJSONObject("dataList");
                    if (dataList.getInt("type") == 1) {
                        shareTitle+="租房 ";
                    } else if (dataList.getInt("type") == 2) {
                        shareTitle+="售房 ";
                    }
                    shareTitle+=dataList.getString("name");
                    shareTitle+=(" "+dataList.getString("room") + "室" + dataList.getString("office") + "厅 ");
                    shareTitle+=(dataList.getString("area")+"平米 ");
                    if (dataList.getInt("type") == 1) {
                        shareTitle+=(dataList.getString("unitprice")+"元/月");
                    } else if (dataList.getInt("type") == 2) {
                        shareTitle+=("售价"+dataList.getString("unitprice")+"万");
                    }

                    showShare(shareTitle);
                } else {
                    Toast.makeText(getApplicationContext(), "请先编辑房源信息后再分享~", Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };


}
