package us.eiyou.demo_camera.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import cn.smssdk.SMSSDK;
import us.eiyou.demo_camera.R;
import us.eiyou.demo_camera.utils.Config;
import us.eiyou.demo_camera.utils.Http;
import us.eiyou.demo_camera.utils.SP;

@SuppressLint("ShowToast")
public class RegisterActivity extends Activity {

    Button btn_title_back;
    TextView tev_title_content;
    EditText edt_username, edt_passwd, edt_passwd2, et_vcode;
    Button btn_register, b_vcode;

    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_register);
        initView();
    }

    private void initView() {
        btn_title_back = (Button) findViewById(R.id.btn_title_back);
        tev_title_content = (TextView) findViewById(R.id.tev_title_content);
        edt_username = (EditText) findViewById(R.id.edt_register_username);
        edt_passwd = (EditText) findViewById(R.id.edt_register_passwd);
        edt_passwd2 = (EditText) findViewById(R.id.edt_register_passwd2);
        et_vcode = (EditText) findViewById(R.id.et_vcode);
        btn_register = (Button) findViewById(R.id.btn_register);
        b_vcode = (Button) findViewById(R.id.b_vcode);
        tev_title_content.setText("注册");

        btn_title_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                finish();
            }
        });

        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                register(edt_username.getText().toString(), edt_passwd
                        .getText().toString(), edt_passwd2.getText().toString(), et_vcode.getText().toString());
            }
        });

        b_vcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edt_username.getText().length() == 11) {
                    new CountDownTimer(45 * 1000, 1000) {
                        @Override
                        public void onTick(long millisUntilFinished) {
                            b_vcode.setClickable(false);
                            b_vcode.setText(millisUntilFinished / 1000 + "秒");
                        }

                        @Override
                        public void onFinish() {
                            b_vcode.setClickable(true);
                            b_vcode.setText("获取验证码");
                        }
                    }.start();
                    SMSSDK.getVerificationCode("86", edt_username.getText().toString());
                } else {
                    Toast.makeText(getApplicationContext(), "请输入正确的手机号", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void register(final String username, String passwd, String passwd2, final String vcode) {
        if (TextUtils.isEmpty(username) || TextUtils.isEmpty(passwd)
                || TextUtils.isEmpty(passwd2) || TextUtils.isEmpty(vcode)) {
            Toast.makeText(this, "请填写完整", Toast.LENGTH_SHORT).show();
            return;
        } else {
            if (passwd.equals(passwd2)) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        JSONObject result = null;
                        try {
                            result = new JSONObject(new String(Http.requestData("https://webapi.sms.mob.com/sms/verify", "appkey=1493b5e881b45&amp;phone=" + username + "&amp;zone=86&amp;&amp;code=" + vcode)));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        Message message = new Message();
                        message.obj = result;
                        handlerVcode.sendMessage(message);
                    }
                }).start();

            } else {
                Toast.makeText(this, "两次密码不一致", Toast.LENGTH_SHORT).show();
            }
        }
    }

    Runnable runnable = new Runnable() {

        @Override
        public void run() {
            // TODO Auto-generated method stub

            JSONObject jsonO = new JSONObject();
            try {
                jsonO.put("serviceName", "commonMemberRegister");
                JSONObject jsonP = new JSONObject();
                jsonP.put("telephone", edt_username.getText().toString());
                jsonP.put("password", edt_passwd2.getText().toString());
                jsonO.put("queryParameters", jsonP);

            } catch (Exception e) {
                e.printStackTrace();
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
            handlerRegister.sendMessage(msg);
        }
    };
    private Handler handlerRegister = new Handler() {
        public void handleMessage(Message msg) {
            try {
                JSONObject getjson = (JSONObject) msg.obj;
                int status = getjson.getInt("status");
                if (status == 1) {
                    String telephone = getjson.getString("telephone");
                    SP.put(getApplicationContext(), "telephone", telephone);
                    RegisterActivity.this.finish();

                    Intent intent = new Intent(RegisterActivity.this,
                            MainActivity.class);
                    intent.putExtra("phone", telephone);
                    startActivity(intent);
                } else {
                    String comment = getjson.getString("comment");
                    Toast.makeText(RegisterActivity.this, comment,
                            Toast.LENGTH_LONG).show();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            if(progressDialog!=null) {
                progressDialog.dismiss();
            }
        }
    };

    private Handler handlerVcode = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            JSONObject jsonObject = (JSONObject) msg.obj;
            try {
                int status = jsonObject.getInt("status");
                if (200 == status) {
                    new Thread(runnable).start();
                } else {
                    progressDialog = new ProgressDialog(
                            RegisterActivity.this);
                    progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                    progressDialog.setMessage("正在提交数据...");
                    progressDialog.setIndeterminate(false);
                    progressDialog.show();
                    Toast.makeText(getApplicationContext(), "验证码错误，status:" + status, Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };

}
