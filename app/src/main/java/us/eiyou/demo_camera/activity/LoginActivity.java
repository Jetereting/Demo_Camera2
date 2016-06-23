package us.eiyou.demo_camera.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bmob.v3.Bmob;
import cn.bmob.v3.update.BmobUpdateAgent;
import us.eiyou.demo_camera.R;
import us.eiyou.demo_camera.utils.Config;
import us.eiyou.demo_camera.utils.Http;
import us.eiyou.demo_camera.utils.IsNetwork;
import us.eiyou.demo_camera.utils.SP;

public class LoginActivity extends AppCompatActivity {

    @Bind(R.id.et_name)
    EditText etName;
    @Bind(R.id.et_password)
    EditText etPassword;

    private String telephone;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            JSONObject jsonObject = (JSONObject) msg.obj;
            try {
                int status = jsonObject.getInt("status");
                if (1 == status) {
                    Toast.makeText(LoginActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
                    JSONObject j = jsonObject.getJSONObject("dataList");
                    telephone = j.getString("telephone");
                    SP.put(getApplicationContext(),"telephone",telephone);
                    LoginActivity.this.finish();

                    Intent intent = new Intent(LoginActivity.this,
                            MainActivity.class);
                    intent.putExtra("phone", telephone);
                    startActivity(intent);
                }else {
                    Toast.makeText(LoginActivity.this, jsonObject.getString("comment"),Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        Bmob.initialize(this, "9d80b49a5519550639a757981f20cf72");
        BmobUpdateAgent.update(this);
        haveUser();
    }

    private void haveUser() {
        if("" != SP.getString(getApplicationContext(), "telephone")){
            startActivity(new Intent(LoginActivity.this,MainActivity.class));
        }
    }

    @OnClick({R.id.b_login, R.id.b_register})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.b_login:
                if (0 != etName.getText().toString().length()) {
                    if (0 != etPassword.getText().toString().length()) {
                        if (IsNetwork.isNetwork(getApplicationContext())) {
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    JSONObject jsonObject = new JSONObject();
                                    JSONObject queryParameters = new JSONObject();
                                    JSONObject result = null;
                                    try {
                                        jsonObject.put("serviceName", "login");
                                        queryParameters.put("loginName", etName.getText().toString());
                                        queryParameters.put("password", etPassword.getText().toString());
                                        jsonObject.put("queryParameters", queryParameters);
                                        result = new JSONObject(Http.Result(Config.login_url, jsonObject.toString()));
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                    Message message = new Message();
                                    message.obj = result;
                                    handler.sendMessage(message);
                                }
                            }).start();
                        } else {
                            Toast.makeText(this, "木有网络哦", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(this, "密码不能为空", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(this, "用户名不能为空", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.b_register:
                startActivity(new Intent(getApplicationContext(),RegisterActivity.class));
                break;
        }
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK){
            Intent home = new Intent(Intent.ACTION_MAIN);
            home.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            home.addCategory(Intent.CATEGORY_HOME);
            startActivity(home);
        }
        return true;
    }
}
