package us.eiyou.demo_camera.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import android.widget.Toast;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.beardedhen.androidbootstrap.BootstrapEditText;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import us.eiyou.demo_camera.R;
import us.eiyou.demo_camera.utils.Config;
import us.eiyou.demo_camera.utils.Http;
import us.eiyou.demo_camera.utils.SP;

public class EditBasicLianxidianhuaActivity extends AppCompatActivity {

    @Bind(R.id.tev_app_title)
    TextView tevAppTitle;
    @Bind(R.id.tv_user)
    TextView tvUser;

    @Bind(R.id.b_submit)
    BootstrapButton bSubmit;
    @Bind(R.id.et_telephone)
    BootstrapEditText etTelephone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_basic_lianxidianhua);
        ButterKnife.bind(this);
        tevAppTitle.setText("基本编辑-联系电话");
        tvUser.setText(SP.getString(getApplicationContext(), "telephone"));
    }

    @OnClick(R.id.b_submit)
    public void onClick() {
        String phone = "";
        phone = etTelephone.getText().toString();
        final String url = Config.urlHead+"uploadfile/TelServlet?path=" + getIntent().getStringExtra("para") + "&telphone=" + phone;
        new Thread(new Runnable() {
            @Override
            public void run() {
                Http.submit(url);
            }
        }).start();
        Toast.makeText(this, "编辑成功！", Toast.LENGTH_SHORT).show();
        finish();
    }
}
