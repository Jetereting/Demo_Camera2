package us.eiyou.demo_camera.utils;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;

public class CombineBitmap {
	public static Bitmap combine(Bitmap bitmap1,Bitmap bitmap2,int x,int y){
		Bitmap bitmap3 = Bitmap.createBitmap(170*7,77*4, bitmap1.getConfig());
		Canvas canvas = new Canvas(bitmap3);
		canvas.drawBitmap(bitmap1, new Matrix(), null);
		canvas.drawBitmap(bitmap2, x, y, null);
		return bitmap3;
	}
}
