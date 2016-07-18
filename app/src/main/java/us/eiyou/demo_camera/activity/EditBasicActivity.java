package us.eiyou.demo_camera.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.beardedhen.androidbootstrap.BootstrapCircleThumbnail;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import us.eiyou.demo_camera.R;

//漫游编辑 原基本编辑
public class EditBasicActivity extends AppCompatActivity {

    @Bind(R.id.b_anlimingcheng)
    BootstrapButton bAnlimingcheng;
    @Bind(R.id.b_changjingmingcheng)
    BootstrapCircleThumbnail bChangjingmingcheng;
    @Bind(R.id.b_lianxidianhua)
    BootstrapButton bLianxidianhua;
    @Bind(R.id.b_zhinanzhen)
    BootstrapButton bZhinanzhen;
    @Bind(R.id.tev_title_content)
    TextView tevTitleContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_basic);
        ButterKnife.bind(this);
        tevTitleContent.setText("漫游编辑");
    }

    @OnClick({R.id.b_anlimingcheng, R.id.b_changjingmingcheng, R.id.b_lianxidianhua, R.id.b_zhinanzhen,R.id.btn_title_back})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.b_anlimingcheng:
                startActivity(new Intent(getApplicationContext(), EditBasicAnlimingchengActivity.class).putExtra("para", getIntent().getStringExtra("para")).putExtra("url", getIntent().getStringExtra("url")).putExtra("num", getIntent().getStringExtra("num")).putExtra("type", getIntent().getStringExtra("type")).putExtra("photoPaths", getIntent().getStringExtra("photoPaths")));
                break;
            case R.id.b_changjingmingcheng:
                startActivity(new Intent(getApplicationContext(), EditBasicChangjingmingchengActivity.class).putExtra("para", getIntent().getStringExtra("para")).putExtra("url", getIntent().getStringExtra("url")).putExtra("num", getIntent().getStringExtra("num")).putExtra("type", getIntent().getStringExtra("type")).putExtra("photoPaths", getIntent().getStringExtra("photoPaths")));
                break;
            case R.id.b_lianxidianhua:
                startActivity(new Intent(getApplicationContext(), EditBasicLianxidianhuaActivity.class).putExtra("para", getIntent().getStringExtra("para")).putExtra("url", getIntent().getStringExtra("url")).putExtra("num", getIntent().getStringExtra("num")).putExtra("type", getIntent().getStringExtra("type")).putExtra("photoPaths", getIntent().getStringExtra("photoPaths")));
                break;
            case R.id.b_zhinanzhen:
                startActivity(new Intent(getApplicationContext(), EditBasicZhinanzhenActivity.class).putExtra("para", getIntent().getStringExtra("para")).putExtra("url", getIntent().getStringExtra("url")).putExtra("num", getIntent().getStringExtra("num")).putExtra("type", getIntent().getStringExtra("type")).putExtra("photoPaths", getIntent().getStringExtra("photoPaths")));
                break;
            case R.id.btn_title_back:finish();break;
        }
    }
}
