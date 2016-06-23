package us.eiyou.demo_camera.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import us.eiyou.demo_camera.R;
import us.eiyou.demo_camera.utils.Config;
import us.eiyou.demo_camera.utils.Http;
import us.eiyou.demo_camera.utils.SP;

@SuppressLint("ShowToast")
public class RegisterActivity extends Activity {

    Button btn_title_back;
    TextView tev_title_content;
    EditText edt_username, edt_passwd, edt_passwd2;
    Button btn_register;

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
        btn_register = (Button) findViewById(R.id.btn_register);
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
                        .getText().toString(), edt_passwd2.getText().toString());
            }
        });
    }

    private void register(String username, String passwd, String passwd2) {
        if (TextUtils.isEmpty(username) || TextUtils.isEmpty(passwd)
                || TextUtils.isEmpty(passwd2)) {
            Toast.makeText(this, "请填写完整", Toast.LENGTH_SHORT).show();
            return;
        } else {
            if (passwd.equals(passwd2)) {
                progressDialog = new ProgressDialog(
                        RegisterActivity.this);
                progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                progressDialog.setMessage("正在提交数据...");
                progressDialog.setIndeterminate(false);
                progressDialog.show();
                new Thread(runnable).start();
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
            handler.sendMessage(msg);
        }
    };
    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            try {
                JSONObject getjson = (JSONObject) msg.obj;
                int status = getjson.getInt("status");
                if (status == 1) {
                    String telephone = getjson.getString("telephone");
                    SP.put(getApplicationContext(),"telephone",telephone);
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
            progressDialog.dismiss();
        }
    };
}
