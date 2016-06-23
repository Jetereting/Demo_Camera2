package us.eiyou.demo_camera.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;

public class CopyFiles {
	public static void copy(String oldPath, String newPath) throws Exception{
		int bytesum = 0;   
		int byteread = 0;
		File oldfile = new File(oldPath);
		if (oldfile.exists()) {
			InputStream inStream = new FileInputStream(oldPath);
			FileOutputStream fs = new FileOutputStream(newPath);   
			byte[] buffer = new byte[1444];   
			while ( (byteread = inStream.read(buffer)) != -1) {
				bytesum += byteread;
				System.out.println(bytesum);   
				fs.write(buffer, 0, byteread);   
			}
			fs.close();
			inStream.close();   
		}   
	}   
}
