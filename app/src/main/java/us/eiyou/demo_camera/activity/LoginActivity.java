package us.eiyou.demo_camera.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.beardedhen.androidbootstrap.BootstrapEditText;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bmob.v3.Bmob;
import cn.bmob.v3.update.BmobUpdateAgent;
import cn.smssdk.SMSSDK;
import us.eiyou.demo_camera.R;
import us.eiyou.demo_camera.utils.Config;
import us.eiyou.demo_camera.utils.Http;
import us.eiyou.demo_camera.utils.IsNetwork;
import us.eiyou.demo_camera.utils.SP;

public class LoginActivity extends AppCompatActivity {

    @Bind(R.id.bt_check_code)
    Button btCheckCode;
    @Bind(R.id.et_name)
    BootstrapEditText etName;
    @Bind(R.id.et_password)
    BootstrapEditText etPassword;

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
                    SP.put(getApplicationContext(), "telephone", telephone);
                    LoginActivity.this.finish();

                    Intent intent = new Intent(LoginActivity.this,
                            MainActivity.class);
                    intent.putExtra("phone", telephone);
                    startActivity(intent);
                } else if (200 == status) {
                    Toast.makeText(LoginActivity.this, "登录成功！", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                } else {
                    Toast.makeText(LoginActivity.this, "密码或验证码错误，status:" + status, Toast.LENGTH_SHORT).show();
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
        if ("" != SP.getString(getApplicationContext(), "telephone")) {
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
        }
    }

    @OnClick({R.id.b_login, R.id.b_register, R.id.bt_check_code})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.b_login:
                if (0 != etName.getText().toString().length()) {
                    if (0 != etPassword.getText().toString().length()) {
                        if (IsNetwork.isNetwork(getApplicationContext())) {
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    JSONObject result = null;
                                    if (etPassword.getHint().toString().equals("密码")) {
                                        JSONObject jsonObject = new JSONObject();
                                        JSONObject queryParameters = new JSONObject();
                                        try {
                                            jsonObject.put("serviceName", "login");
                                            queryParameters.put("loginName", etName.getText().toString());
                                            queryParameters.put("password", etPassword.getText().toString());
                                            jsonObject.put("queryParameters", queryParameters);
                                            result = new JSONObject(Http.Result(Config.login_url, jsonObject.toString()));
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    } else {
                                        try {
                                            result = new JSONObject(new String(Http.requestData("https://webapi.sms.mob.com/sms/verify", "appkey=1493b5e881b45&amp;phone=" + etName.getText().toString() + "&amp;zone=86&amp;&amp;code=" + etPassword.getText().toString())));
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                        Log.d("LoginActivity", "result" + result);
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
                startActivity(new Intent(getApplicationContext(), RegisterActivity.class));
                break;
            case R.id.bt_check_code:
                if (etName.getText().length() == 11) {
                    etPassword.setHint("验证码");
                    etPassword.setInputType(InputType.TYPE_CLASS_NUMBER);
                    new CountDownTimer(30 * 1000, 1000) {
                        @Override
                        public void onTick(long millisUntilFinished) {
                            btCheckCode.setClickable(false);
                            btCheckCode.setText(millisUntilFinished / 1000 + "秒");
                        }

                        @Override
                        public void onFinish() {
                            btCheckCode.setClickable(true);
                            btCheckCode.setText("|  获取验证码登录");
                        }
                    }.start();
                    SMSSDK.getVerificationCode("86", etName.getText().toString());
                } else {
                    Toast.makeText(this, "请输入正确的手机号", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent home = new Intent(Intent.ACTION_MAIN);
            home.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            home.addCategory(Intent.CATEGORY_HOME);
            startActivity(home);
        }
        return true;
    }

    @Override
    protected void onRestart() {
        if (SP.getString(getApplicationContext(), "telephone").length() != 0) {
            finish();
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
        }
        super.onRestart();
    }
}
