package us.eiyou.demo_camera.utils;

import android.os.CountDownTimer;
import android.widget.Button;

public class TimeCount extends CountDownTimer{
	Button button;
	
	public TimeCount(long millisInFuture, long countDownInterval,Button button) {
		super(millisInFuture, countDownInterval);
		this.button=button;
	}

	@Override
	public void onTick(long millisUntilFinished) {
		button.setClickable(false);
		button.setText(millisUntilFinished/1000+"��");
	}

	@Override
	public void onFinish() {
		button.setText("��ȡ��̬��֤��");
		button.setClickable(true);
	}
	
}
