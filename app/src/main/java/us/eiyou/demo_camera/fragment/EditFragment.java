package us.eiyou.demo_camera.fragment;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

import us.eiyou.demo_camera.activity.LookShareActivity;
import us.eiyou.demo_camera.R;
import us.eiyou.demo_camera.activity.WaitUploadActivity;

public class EditFragment extends Fragment implements OnClickListener {

	Button trip_share, trip_upload, house_share,house_upload, house_share7,house_upload7;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_edit, container, false);
		initView(view);
		return view;
	}

	private void initView(View view) {
		trip_share = (Button) view.findViewById(R.id.trip_share);
		trip_upload = (Button) view.findViewById(R.id.trip_upload);
		house_share = (Button) view.findViewById(R.id.house_share);
		house_upload = (Button) view.findViewById(R.id.house_upload);
		house_share7 = (Button) view.findViewById(R.id.house_share7);
		house_upload7 = (Button) view.findViewById(R.id.house_upload7);
		trip_share.setOnClickListener(this);
		trip_upload.setOnClickListener(this);
		house_share.setOnClickListener(this);
		house_upload.setOnClickListener(this);
		house_share7.setOnClickListener(this);
		house_upload7.setOnClickListener(this);
	}

	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.trip_share:
			startActivity(new Intent(getActivity(),LookShareActivity.class).putExtra("type", "trip_share"));
			break;
		case R.id.trip_upload:
			startActivity(new Intent(getActivity(),WaitUploadActivity.class).putExtra("type", "trip_upload"));
			break;
		case R.id.house_share:
			startActivity(new Intent(getActivity(),LookShareActivity.class).putExtra("type", "house_share"));
			break;
		case R.id.house_upload:
			startActivity(new Intent(getActivity(),WaitUploadActivity.class).putExtra("type", "house_upload"));
			break;
		case R.id.house_share7:
			startActivity(new Intent(getActivity(),LookShareActivity.class).putExtra("type", "house_share7"));
			break;
		case R.id.house_upload7:
			startActivity(new Intent(getActivity(),WaitUploadActivity.class).putExtra("type", "house_upload7"));
			break;
		}
	}

}
