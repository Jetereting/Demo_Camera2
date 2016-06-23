package us.eiyou.demo_camera.activity;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bmob.v3.update.BmobUpdateAgent;
import us.eiyou.demo_camera.R;
import us.eiyou.demo_camera.fragment.AlbumFragment;
import us.eiyou.demo_camera.fragment.CameraFragment;
import us.eiyou.demo_camera.fragment.EditFragment;
import us.eiyou.demo_camera.fragment.MyFragment;
import us.eiyou.demo_camera.utils.SP;

public class MainActivity extends AppCompatActivity {

    @Bind(R.id.tev_app_title)
    TextView tevAppTitle;
    @Bind(R.id.tv_user)
    TextView tvUser;


    // tab按钮对象数组
    private Button[] tabs;
    // fragment对象数组
    private Fragment[] fragments;
    // fragment界面
    private Fragment fragment_edit, fragment_album, fragment_camera,
            fragment_my;
    // 当前tab下标
    private int currentTabIndex;
    boolean exit=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        BmobUpdateAgent.update(this);
        initTab();
        switch (getIntent().getIntExtra("page", 0)) {
            case 1:
                tabSelect(1);
                break;
            case 2:
                tabSelect(2);
                break;
            case 3:
                tabSelect(3);
                break;
        }
    }

    /**
     * 初始化fragments数据
     */
    private void initTab() {
        tabs = new Button[5];
        tabs[0] = (Button) findViewById(R.id.btn_main_tab_edit);
        tabs[1] = (Button) findViewById(R.id.btn_main_tab_album);
        tabs[2] = (Button) findViewById(R.id.btn_main_tab_camera);
        tabs[3] = (Button) findViewById(R.id.btn_main_tab_my);

        tvUser.setText(SP.getString(getApplicationContext(),"telephone") + "\t");
        fragment_edit = new EditFragment();
        fragment_album = new AlbumFragment();
        fragment_camera = new CameraFragment();
        fragment_my = new MyFragment();
        fragments = new Fragment[] { fragment_edit, fragment_album,
                fragment_camera, fragment_my };
        // 添加显示第一个fragment
        getFragmentManager().beginTransaction().add(R.id.fragment_container, fragment_edit).commit();

    }

    /**
     * 切换fragment
     *
     * @param index
     */
    public void tabSelect(int index) {
        if (currentTabIndex != index) {
            FragmentTransaction transaction = getFragmentManager()
                    .beginTransaction();
            transaction.hide(fragments[currentTabIndex]);
            if (!fragments[index].isAdded()) {
                transaction.add(R.id.fragment_container, fragments[index]);
            }
            transaction.show(fragments[index]).commit();
        }
        tabs[currentTabIndex].setSelected(false);
        tabs[index].setSelected(true);
        currentTabIndex = index;
    }


    @OnClick({R.id.btn_main_tab_edit, R.id.btn_main_tab_album, R.id.btn_main_tab_camera, R.id.btn_main_tab_my})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_main_tab_edit:
                tevAppTitle.setText("首页");
                tabSelect(0);
                break;
            case R.id.btn_main_tab_album:
                tevAppTitle.setText("点击图片上传");
                tabSelect(1);
                break;
            case R.id.btn_main_tab_camera:
                tevAppTitle.setText("相机");
                tabSelect(2);
                break;
            case R.id.btn_main_tab_my:
                tevAppTitle.setText("我的");
                tabSelect(3);
                break;
        }
    }
    @Override
    public void onBackPressed() {
        if(exit){
            finish();
            Intent home = new Intent(Intent.ACTION_MAIN);
            home.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            home.addCategory(Intent.CATEGORY_HOME);
            startActivity(home);
        }else {
            exit=true;
            Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    exit=false;
                }
            },3*1000);
        }
    }
}
