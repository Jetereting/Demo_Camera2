package us.eiyou.demo_camera.model;

import android.graphics.Bitmap;
import android.widget.ImageView;

public class WaitUploadModel {

	String waitupload_name;
	Bitmap bitmap;
	public WaitUploadModel(String waitupload_name,Bitmap bitmap) {
		super();
		this.waitupload_name = waitupload_name;
		this.bitmap = bitmap;
	}

	public String getWaitupload_name() {
		return waitupload_name;
	}

	public void setWaitupload_name(String waitupload_name) {
		this.waitupload_name = waitupload_name;
	}
	
	public void setBitmap(Bitmap bitmap) {
		this.bitmap = bitmap;
	}
	public Bitmap getBitmap() {
		return bitmap;
	}
}
