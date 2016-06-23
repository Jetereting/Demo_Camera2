package us.eiyou.demo_camera.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import us.eiyou.demo_camera.R;
import us.eiyou.demo_camera.utils.Config;

public class ScenesNameEditActivity extends AppCompatActivity {

    String url;
    String et1S, et2S, et3S, et4S, et5S, et6S, et7S;
    @Bind(R.id.et_1)
    EditText et1;
    @Bind(R.id.et_2)
    EditText et2;
    @Bind(R.id.et_3)
    EditText et3;
    @Bind(R.id.et_4)
    EditText et4;
    @Bind(R.id.et_5)
    EditText et5;
    @Bind(R.id.et_6)
    EditText et6;
    @Bind(R.id.et_7)
    EditText et7;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scenes_name_edit);
        ButterKnife.bind(this);
        if (getIntent().getStringExtra("num").equals("1")) {
            et2.setVisibility(View.GONE);
            et3.setVisibility(View.GONE);
            et4.setVisibility(View.GONE);
            et5.setVisibility(View.GONE);
            et6.setVisibility(View.GONE);
            et7.setVisibility(View.GONE);
        } else if (getIntent().getStringExtra("num").equals("2")) {
            et3.setVisibility(View.GONE);
            et4.setVisibility(View.GONE);
            et5.setVisibility(View.GONE);
            et6.setVisibility(View.GONE);
            et7.setVisibility(View.GONE);
        } else if (getIntent().getStringExtra("num").equals("3")) {
            et4.setVisibility(View.GONE);
            et5.setVisibility(View.GONE);
            et6.setVisibility(View.GONE);
            et7.setVisibility(View.GONE);
        } else if (getIntent().getStringExtra("num").equals("4")) {
            et5.setVisibility(View.GONE);
            et6.setVisibility(View.GONE);
            et7.setVisibility(View.GONE);
        } else if (getIntent().getStringExtra("num").equals("5")) {
            et6.setVisibility(View.GONE);
            et7.setVisibility(View.GONE);
        } else if (getIntent().getStringExtra("num").equals("6")) {
            et7.setVisibility(View.GONE);
        }
    }

    @OnClick(R.id.b)
    public void onClick() {
        try {
            et1S = URLEncoder.encode(et1.getText().toString(), "UTF-8");
            et2S = URLEncoder.encode(et2.getText().toString(), "UTF-8");
            et3S = URLEncoder.encode(et3.getText().toString(), "UTF-8");
            et4S = URLEncoder.encode(et4.getText().toString(), "UTF-8");
            et5S = URLEncoder.encode(et5.getText().toString(), "UTF-8");
            et6S = URLEncoder.encode(et6.getText().toString(), "UTF-8");
            et7S = URLEncoder.encode(et7.getText().toString(), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        if (getIntent().getStringExtra("num").equals("1")) {
            if (!TextUtils.isEmpty(et1S)) {
                url = Config.urlHead+"uploadfile/Dom7j?path=" + getIntent().getStringExtra("para") + "&num=1&x0=" + et1S;
                submit(url);
            } else {
                Toast.makeText(this, "请先输入场景名称后再提交", Toast.LENGTH_SHORT).show();
            }
        } else if (getIntent().getStringExtra("num").equals("2")) {
            if (!TextUtils.isEmpty(et1S)) {
                if (!TextUtils.isEmpty(et2S)) {
                    url = Config.urlHead+"uploadfile/Dom7j?path=" + getIntent().getStringExtra("para") + "&num=2&x0=" + et1S + "&x1=" + et2S;
                    submit(url);
                } else {
                    Toast.makeText(this, "请先输入场景2名称后再提交", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "请先输入场景1名称后再提交", Toast.LENGTH_SHORT).show();
            }
        } else if (getIntent().getStringExtra("num").equals("3")) {
            if (!TextUtils.isEmpty(et1S)) {
                if (!TextUtils.isEmpty(et2S)) {
                    if (!TextUtils.isEmpty(et3S)) {
                        url = Config.urlHead+"uploadfile/Dom7j?path=" + getIntent().getStringExtra("para") + "&num=3&x0=" + et1S + "&x1=" + et2S + "&x2=" + et3S;
                        submit(url);
                    } else {
                        Toast.makeText(this, "请先输入场景3名称后再提交", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(this, "请先输入场景2名称后再提交", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "请先输入场景1名称后再提交", Toast.LENGTH_SHORT).show();
            }
        } else if (getIntent().getStringExtra("num").equals("4")) {
            if (!TextUtils.isEmpty(et1S)) {
                if (!TextUtils.isEmpty(et2S)) {
                    if (!TextUtils.isEmpty(et3S)) {
                        if (!TextUtils.isEmpty(et4S)) {
                            url = Config.urlHead+"uploadfile/Dom7j?path=" + getIntent().getStringExtra("para") + "&num=4&x0=" + et1S + "&x1=" + et2S + "&x2=" + et3S + "&x3=" + et4S;
                            submit(url);
                        } else {
                            Toast.makeText(this, "请先输入场景4名称后再提交", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(this, "请先输入场景3名称后再提交", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(this, "请先输入场景2名称后再提交", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "请先输入场景1名称后再提交", Toast.LENGTH_SHORT).show();
            }
        } else if (getIntent().getStringExtra("num").equals("5")) {
            if (!TextUtils.isEmpty(et1S)) {
                if (!TextUtils.isEmpty(et2S)) {
                    if (!TextUtils.isEmpty(et3S)) {
                        if (!TextUtils.isEmpty(et4S)) {
                            if (!TextUtils.isEmpty(et5S)) {
                                url = Config.urlHead+"uploadfile/Dom7j?path=" + getIntent().getStringExtra("para") + "&num=5&x0=" + et1S + "&x1=" + et2S + "&x2=" + et3S + "&x3=" + et4S + "&x4=" + et5S;
                                submit(url);
                            } else {
                                Toast.makeText(this, "请先输入场景5名称后再提交", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(this, "请先输入场景4名称后再提交", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(this, "请先输入场景3名称后再提交", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(this, "请先输入场景2名称后再提交", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "请先输入场景1名称后再提交", Toast.LENGTH_SHORT).show();
            }
        } else if (getIntent().getStringExtra("num").equals("6")) {
            if (!TextUtils.isEmpty(et1S)&&!TextUtils.isEmpty(et2S)&&!TextUtils.isEmpty(et3S)&&!TextUtils.isEmpty(et4S)&&!TextUtils.isEmpty(et5S)&&!TextUtils.isEmpty(et6S)) {
                url = Config.urlHead+"uploadfile/Dom7j?path=" + getIntent().getStringExtra("para") + "&num=5&x0=" + et1S + "&x1=" + et2S + "&x2=" + et3S + "&x3=" + et4S + "&x4=" + et5S+ "&x5=" + et6S;
            } else {
                Toast.makeText(this, "请先输入场景名称后再提交", Toast.LENGTH_SHORT).show();
            }
        } else if (getIntent().getStringExtra("num").equals("7")) {
            if (!TextUtils.isEmpty(et1S)&&!TextUtils.isEmpty(et2S)&&!TextUtils.isEmpty(et3S)&&!TextUtils.isEmpty(et4S)&&!TextUtils.isEmpty(et5S)&&!TextUtils.isEmpty(et6S)&&!TextUtils.isEmpty(et7S)) {
                url = Config.urlHead+"uploadfile/Dom7j?path=" + getIntent().getStringExtra("para") + "&num=5&x0=" + et1S + "&x1=" + et2S + "&x2=" + et3S + "&x3=" + et4S + "&x4=" + et5S+ "&x5=" + et6S+ "&x6=" + et7S;
            } else {
                Toast.makeText(this, "请先输入场景名称后再提交", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void submit(String url) {
        try {
            URL url7 = new URL(url);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url7.openConnection();
            httpURLConnection.setDoInput(true);
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setUseCaches(false);
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setRequestProperty("Connection", "Keep-Alive");
            httpURLConnection.setRequestProperty("Charset", "GB2312");
            Log.e("responseCode ", httpURLConnection.getResponseCode() + "");
            httpURLConnection.connect();
            Toast.makeText(getApplicationContext(), "处理完毕！", Toast.LENGTH_SHORT).show();
            finish();
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
        }

    }
}
