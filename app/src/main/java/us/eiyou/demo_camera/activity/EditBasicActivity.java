package us.eiyou.demo_camera.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.beardedhen.androidbootstrap.BootstrapButton;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import us.eiyou.demo_camera.R;
import us.eiyou.demo_camera.utils.SP;

public class EditBasicActivity extends AppCompatActivity {

    @Bind(R.id.tev_app_title)
    TextView tevAppTitle;
    @Bind(R.id.tv_user)
    TextView tvUser;
    @Bind(R.id.b_anlimingcheng)
    BootstrapButton bAnlimingcheng;
    @Bind(R.id.b_changjingmingcheng)
    BootstrapButton bChangjingmingcheng;
    @Bind(R.id.b_lianxidianhua)
    BootstrapButton bLianxidianhua;
    @Bind(R.id.b_zhinanzhen)
    BootstrapButton bZhinanzhen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_basic);
        ButterKnife.bind(this);
        tevAppTitle.setText("基本编辑");
        tvUser.setText(SP.getString(getApplicationContext(),"telephone"));
    }

    @OnClick({R.id.b_anlimingcheng, R.id.b_changjingmingcheng, R.id.b_lianxidianhua, R.id.b_zhinanzhen})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.b_anlimingcheng:
                startActivity(new Intent(getApplicationContext(),EditBasicAnlimingchengActivity.class).putExtra("para", getIntent().getStringExtra("para")).putExtra("url", getIntent().getStringExtra("url")).putExtra("num", getIntent().getStringExtra("num")).putExtra("type", getIntent().getStringExtra("type")).putExtra("photoPaths",getIntent().getStringExtra("photoPaths")));
                break;
            case R.id.b_changjingmingcheng:
                startActivity(new Intent(getApplicationContext(),EditBasicChangjingmingchengActivity.class).putExtra("para", getIntent().getStringExtra("para")).putExtra("url", getIntent().getStringExtra("url")).putExtra("num", getIntent().getStringExtra("num")).putExtra("type", getIntent().getStringExtra("type")).putExtra("photoPaths",getIntent().getStringExtra("photoPaths")));
                break;
            case R.id.b_lianxidianhua:
                startActivity(new Intent(getApplicationContext(),EditBasicLianxidianhuaActivity.class).putExtra("para", getIntent().getStringExtra("para")).putExtra("url", getIntent().getStringExtra("url")).putExtra("num", getIntent().getStringExtra("num")).putExtra("type", getIntent().getStringExtra("type")).putExtra("photoPaths",getIntent().getStringExtra("photoPaths")));
                break;
            case R.id.b_zhinanzhen:
                startActivity(new Intent(getApplicationContext(),EditBasicZhinanzhenActivity.class).putExtra("para", getIntent().getStringExtra("para")).putExtra("url", getIntent().getStringExtra("url")).putExtra("num", getIntent().getStringExtra("num")).putExtra("type", getIntent().getStringExtra("type")).putExtra("photoPaths",getIntent().getStringExtra("photoPaths")));
                break;
        }
    }
}
