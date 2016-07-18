package us.eiyou.demo_camera.fragment;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;
import butterknife.OnClick;
import us.eiyou.demo_camera.R;
import us.eiyou.demo_camera.activity.AgentActivity;
import us.eiyou.demo_camera.activity.ChangePasswordActivity;
import us.eiyou.demo_camera.activity.HelpActivity;
import us.eiyou.demo_camera.activity.LoginActivity;
import us.eiyou.demo_camera.utils.SP;

// 我的 退出登录 修改密码
public class MyFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @OnClick({R.id.my_info, R.id.help_message, R.id.change_password, R.id.logout})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.my_info:
                startActivity(new Intent(getActivity(), AgentActivity.class));
                break;
            case R.id.help_message:
                startActivity(new Intent(getActivity(), HelpActivity.class));
                break;
            case R.id.change_password:
                startActivity(new Intent(getActivity(), ChangePasswordActivity.class));
                break;
            case R.id.logout:
                SP.put(getActivity(),"telephone","");
                getActivity().finish();
                startActivity(new Intent(getActivity(), LoginActivity.class));
                break;
        }
    }
}
