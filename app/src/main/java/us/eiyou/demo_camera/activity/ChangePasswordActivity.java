package us.eiyou.demo_camera.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.beardedhen.androidbootstrap.BootstrapEditText;

import org.json.JSONObject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import us.eiyou.demo_camera.R;
import us.eiyou.demo_camera.utils.Config;
import us.eiyou.demo_camera.utils.Http;
import us.eiyou.demo_camera.utils.SP;

public class ChangePasswordActivity extends AppCompatActivity {

    @Bind(R.id.old_password)
    BootstrapEditText oldPassword;
    @Bind(R.id.new_password)
    BootstrapEditText newPassword;

    ProgressDialog progressDialog;
    @Bind(R.id.tev_title_content)
    TextView tevTitleContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        ButterKnife.bind(this);
        tevTitleContent.setText("修改密码");
    }

    @OnClick({R.id.change, R.id.btn_title_back})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.change:
                if (oldPassword.getText().length() != 0) {
                    if (newPassword.getText().length() != 0) {
                        progressDialog = new ProgressDialog(
                                ChangePasswordActivity.this);
                        progressDialog
                                .setProgressStyle(ProgressDialog.STYLE_SPINNER);
                        progressDialog.setMessage("修改中...");
                        progressDialog.setIndeterminate(false);
                        progressDialog.setCancelable(true);
                        progressDialog.show();
                        new Thread(runnable).start();
                    } else {
                        Toast.makeText(getApplicationContext(), "请输入新密码",
                                Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "请输入旧密码",
                            Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.btn_title_back:
                finish();
                break;
        }
    }

    Runnable runnable = new Runnable() {

        @Override
        public void run() {
            // TODO Auto-generated method stub
            String string_loadpasswd = oldPassword.getText().toString();
            String string_newpasswd = newPassword.getText().toString();

            JSONObject jsonO = new JSONObject();
            try {
                jsonO.put("serviceName", "submitPwd");
                JSONObject jsonP = new JSONObject();
                jsonP.put("telephone", SP.getString(getApplicationContext(), "telephone"));
                jsonP.put("password", string_newpasswd);
                jsonP.put("oldpassword", string_loadpasswd);
                jsonO.put("queryParameters", jsonP);

            } catch (Exception e) {
                Toast.makeText(getApplicationContext(),
                        "runnable:" + e.toString(), Toast.LENGTH_SHORT).show();
            }

            String input = jsonO.toString();
            Http http = new Http();
            String requst = http.Result(Config.login_url, input);
            JSONObject json = null;
            try {
                json = new JSONObject(requst);
            } catch (Exception e) {
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
                    String comment = getjson.getString("comment");
                    Toast.makeText(getApplicationContext(), comment,
                            Toast.LENGTH_LONG).show();
                    finish();
                    SP.put(getApplicationContext(), "telephone", "");
                    startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                } else {
                    String comment = getjson.getString("comment");
                    Toast.makeText(getApplicationContext(), comment,
                            Toast.LENGTH_LONG).show();
                }
            } catch (Exception e) {
                Toast.makeText(getApplicationContext(),
                        "handler:" + e.toString(), Toast.LENGTH_SHORT).show();
            }
            progressDialog.dismiss();
        }

        ;
    };


}
