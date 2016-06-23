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

public class EditSeniorActivity extends AppCompatActivity {

    @Bind(R.id.tev_app_title)
    TextView tevAppTitle;
    @Bind(R.id.tv_user)
    TextView tvUser;
    @Bind(R.id.b_jiantou)
    BootstrapButton bJiantou;
    @Bind(R.id.b_tuwen)
    BootstrapButton bTuwen;
    @Bind(R.id.b_shapan)
    BootstrapButton bShapan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_senior);
        ButterKnife.bind(this);
        tevAppTitle.setText("高级编辑");
        tvUser.setText(SP.getString(getApplicationContext(),"telephone"));
    }

    @OnClick({R.id.b_jiantou, R.id.b_tuwen, R.id.b_shapan})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.b_jiantou:
                startActivity(new Intent(getApplicationContext(),EditSeniorJiantouActivity.class).putExtra("para", getIntent().getStringExtra("para")).putExtra("url", getIntent().getStringExtra("url")).putExtra("num", getIntent().getStringExtra("num")).putExtra("type", getIntent().getStringExtra("type")).putExtra("photoPaths",getIntent().getStringExtra("photoPaths")));
                break;
            case R.id.b_tuwen:
                startActivity(new Intent(getApplicationContext(),EditSeniorTuwenActivity.class).putExtra("para", getIntent().getStringExtra("para")).putExtra("url", getIntent().getStringExtra("url")).putExtra("num", getIntent().getStringExtra("num")).putExtra("type", getIntent().getStringExtra("type")).putExtra("photoPaths",getIntent().getStringExtra("photoPaths")));
                break;
            case R.id.b_shapan:
                startActivity(new Intent(getApplicationContext(),EditSeniorShapanActivity.class).putExtra("para", getIntent().getStringExtra("para")).putExtra("url", getIntent().getStringExtra("url")).putExtra("num", getIntent().getStringExtra("num")).putExtra("type", getIntent().getStringExtra("type")).putExtra("photoPaths",getIntent().getStringExtra("photoPaths")));
                break;
        }
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
