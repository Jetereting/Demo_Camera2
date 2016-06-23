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

public class EditIndexActivity extends AppCompatActivity {

    @Bind(R.id.tev_app_title)
    TextView tevAppTitle;
    @Bind(R.id.tv_user)
    TextView tvUser;
    @Bind(R.id.b_basic)
    BootstrapButton bBasic;
    @Bind(R.id.b_senior)
    BootstrapButton bSenior;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_index);
        ButterKnife.bind(this);

        tevAppTitle.setText("编辑");
        tvUser.setText(SP.getString(getApplicationContext(),"telephone"));
    }

    @OnClick({R.id.b_basic, R.id.b_senior,R.id.b_share,R.id.b_house_resources})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.b_basic:
                startActivity(new Intent(getApplicationContext(),EditBasicActivity.class).putExtra("para", getIntent().getStringExtra("para")).putExtra("url", getIntent().getStringExtra("url")).putExtra("num", getIntent().getStringExtra("num")).putExtra("type", getIntent().getStringExtra("type")).putExtra("photoPaths",getIntent().getStringExtra("photoPaths")));
                break;
            case R.id.b_senior:
                startActivity(new Intent(getApplicationContext(),EditSeniorActivity.class).putExtra("para", getIntent().getStringExtra("para")).putExtra("url", getIntent().getStringExtra("url")).putExtra("num", getIntent().getStringExtra("num")).putExtra("type", getIntent().getStringExtra("type")).putExtra("photoPaths",getIntent().getStringExtra("photoPaths")));
                break;
            case R.id.b_house_resources:
                startActivity(new Intent(getApplicationContext(),HouseResourcesActivity.class).putExtra("para", getIntent().getStringExtra("para")).putExtra("url", getIntent().getStringExtra("url")).putExtra("num", getIntent().getStringExtra("num")).putExtra("type", getIntent().getStringExtra("type")).putExtra("photoPaths",getIntent().getStringExtra("photoPaths")));
                break;
            case R.id.b_share:
                Intent shareIntent = new Intent();
                shareIntent.setAction(Intent.ACTION_SEND);
                shareIntent.putExtra(Intent.EXTRA_TEXT,getIntent().getStringExtra("urlShare"));
                shareIntent.setType("text/plain");
                startActivity(Intent.createChooser(shareIntent, "分享到"));
                break;
        }
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
