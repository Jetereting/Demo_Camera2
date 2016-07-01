package us.eiyou.demo_camera.model;

import android.graphics.Bitmap;

public class WaitUploadModel {

	String waitupload_name;
	Bitmap bitmap;
	String name;

	public String getAddtime() {
		return addtime;
	}

	public void setAddtime(String addtime) {
		this.addtime = addtime;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	String addtime;
	public WaitUploadModel(String waitupload_name,Bitmap bitmap,String name,String addtime) {
		super();
		this.waitupload_name = waitupload_name;
		this.bitmap = bitmap;
		this.name = name;
		this.addtime = addtime;
	}
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
