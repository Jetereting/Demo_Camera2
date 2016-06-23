package us.eiyou.demo_camera.fragment;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONObject;

import butterknife.ButterKnife;
import butterknife.OnClick;
import us.eiyou.demo_camera.activity.HelpActivity;
import us.eiyou.demo_camera.activity.LoginActivity;
import us.eiyou.demo_camera.R;
import us.eiyou.demo_camera.utils.Config;
import us.eiyou.demo_camera.utils.Http;
import us.eiyou.demo_camera.utils.SP;

public class MyFragment extends Fragment {
    EditText et_loadpasswd, et_newpasswd;
    Button btn_change, btn_back;
    ProgressDialog progressDialog;
    String telephone = "";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my, container, false);
        initView(view);
        ButterKnife.bind(this, view);
        return view;
    }

    private void initView(View view) {
        et_loadpasswd = (EditText) view.findViewById(R.id.edt_leftmenu_myhome_changpasswd_loadpasswd);
        et_newpasswd = (EditText) view.findViewById(R.id.edt_leftmenu_myhome_changpasswd_newpasswd);
        btn_change = (Button) view.findViewById(R.id.btn_leftmenu_myhome_changpasswd_change);
        btn_back = (Button) view.findViewById(R.id.btn_back);

        btn_back.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                SP.put(getActivity(),"telephone","");

                startActivity(new Intent(getActivity(),
                        LoginActivity.class));
            }

        });

        btn_change.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (et_loadpasswd.getText().length() != 0) {
                    if (et_newpasswd.getText().length() != 0) {
                        progressDialog = new ProgressDialog(
                                getActivity());
                        progressDialog
                                .setProgressStyle(ProgressDialog.STYLE_SPINNER);
                        progressDialog.setMessage("修改中...");
                        progressDialog.setIndeterminate(false);
                        progressDialog.setCancelable(true);
                        progressDialog.show();
                        new Thread(runnable).start();
                    } else {
                        Toast.makeText(getActivity(), "请输入新密码",
                                Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(getActivity(), "请输入旧密码",
                            Toast.LENGTH_LONG).show();
                }
            }

        });
    }

    Runnable runnable = new Runnable() {

        @Override
        public void run() {
            // TODO Auto-generated method stub
            String string_loadpasswd = et_loadpasswd.getText().toString();
            String string_newpasswd = et_newpasswd.getText().toString();

            JSONObject jsonO = new JSONObject();
            try {
                jsonO.put("serviceName", "submitPwd");
                JSONObject jsonP = new JSONObject();
                jsonP.put("telephone", telephone);
                jsonP.put("password", string_newpasswd);
                jsonP.put("oldpassword", string_loadpasswd);
                jsonO.put("queryParameters", jsonP);

            } catch (Exception e) {
                Toast.makeText(getActivity(),
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
                    Toast.makeText(getActivity(), comment,
                            Toast.LENGTH_LONG).show();
                    getActivity().finish();
                    SP.put(getActivity(),"telephone","");
                    startActivity(new Intent(getActivity(),LoginActivity.class));
                } else {
                    String comment = getjson.getString("comment");
                    Toast.makeText(getActivity(), comment,
                            Toast.LENGTH_LONG).show();
                }
            } catch (Exception e) {
                Toast.makeText(getActivity(),
                        "handler:" + e.toString(), Toast.LENGTH_SHORT).show();
            }
            progressDialog.dismiss();
        }

        ;
    };

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @OnClick(R.id.btn_help)
    public void onClick() {
        startActivity(new Intent(getActivity(), HelpActivity.class));
    }
}
