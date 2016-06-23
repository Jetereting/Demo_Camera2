package us.eiyou.demo_camera.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.beardedhen.androidbootstrap.api.defaults.DefaultBootstrapBrand;
import com.beardedhen.androidbootstrap.api.defaults.DefaultBootstrapSize;

import java.io.UnsupportedEncodingException;

import butterknife.Bind;
import butterknife.ButterKnife;
import us.eiyou.demo_camera.R;
import us.eiyou.demo_camera.utils.Config;
import us.eiyou.demo_camera.utils.Http;
import us.eiyou.demo_camera.utils.SP;

public class EditBasicChangjingmingchengActivity extends AppCompatActivity {

    @Bind(R.id.tev_app_title)
    TextView tevAppTitle;
    @Bind(R.id.tv_user)
    TextView tvUser;
    @Bind(R.id.ll)
    LinearLayout ll;
    String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_basic_changjingmingcheng);
        ButterKnife.bind(this);
        tevAppTitle.setText("基本编辑-场景名称");
        tvUser.setText(SP.getString(getApplicationContext(),"telephone"));

        url = Config.urlHead+"uploadfile/Dom7j?path="+ getIntent().getStringExtra("para") + "&num="+Integer.parseInt(getIntent().getStringExtra("num"));
        for (int i = 0; i < Integer.parseInt(getIntent().getStringExtra("num")); i++) {
            EditText et=new EditText(getApplicationContext());
            et.setGravity(Gravity.CENTER);
            et.setHint("场景"+(i+1)+"名称");
            et.setMaxLines(1);
            et.setTextColor(Color.parseColor("#0637fa"));
            LinearLayout.LayoutParams layout = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            layout.setMargins(44,44,44,44);
            et.setLayoutParams(layout);
            ll.addView(et);
        }
        BootstrapButton b=new BootstrapButton(getApplicationContext());
        b.setBootstrapBrand(DefaultBootstrapBrand.SUCCESS);
        b.setText("提交");
        b.setBootstrapSize(DefaultBootstrapSize.LG);
        b.setRounded(true);
        LinearLayout.LayoutParams layout = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        layout.setMargins(0, 20, 0, 0);
        b.setLayoutParams(layout);
        b.setGravity(Gravity.CENTER);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = 0; i < Integer.parseInt(getIntent().getStringExtra("num")); i++) {
                    EditText et= (EditText) ll.getChildAt(i);
                    String title;
                    try {
                        title=java.net.URLEncoder.encode(et.getText().toString(),"UTF-8");
                    } catch (UnsupportedEncodingException e) {
                        title=et.getText().toString();
                    }
                    url+="&x"+i+"="+title;
                }
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Http.submit(url);
                    }
                }).start();
                Toast.makeText(getApplicationContext(), "编辑成功！", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
        ll.addView(b);
    }
}
