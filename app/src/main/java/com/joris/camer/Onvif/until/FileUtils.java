package com.joris.camer.Onvif.until;

import android.os.Environment;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by Spring on 2015/11/7.
 */
public class FileUtils {
	private String SDPATH;

	public FileUtils() {

	}

		public String getSDPATH() {
		return SDPATH;
	}

	public FileUtils(String SDPATH){
		//得到外部存储设备的目录（/SDCARD）
		SDPATH = Environment.getExternalStorageDirectory() + "/" ;
	}

	/**
	 * 在SD卡上创建文件
	 * @param fileName
	 * @return
	 * @throws java.io.IOException
	 */
	public File createSDFile(String fileName) throws IOException {
		File file = new File(SDPATH + fileName);
		file.createNewFile();
		return file;
	}

	/**
	 * 在SD卡上创建目录
	 * @param dirName 目录名字
	 * @return 文件目录
	 */
	public File createDir(String dirName){
		File dir = new File(SDPATH + dirName);
		dir.mkdir();
		return dir;
	}

	/**
	 * 判断文件是否存在
	 * @param fileName
	 * @return
	 */
	public boolean isFileExist(String fileName){
		File file = new File(SDPATH + fileName);
		return file.exists();
	}

	public File write2SDFromInput(String path,String fileName,InputStream input){
		File file = null;
		OutputStream output = null;

		try {
			createDir(path);
			file =createSDFile(path + fileName);
			output = new FileOutputStream(file);
			byte [] buffer = new byte[4 * 1024];
			while(input.read(buffer) != -1){
				output.write(buffer);
				output.flush();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		finally {
			try {
				output.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return file;
	}



	/**
	 * 将二进制文件写入到制定的文件夹中
	 *
	 * @param parentPath
	 * @param fileName
	 * @param content
	 * @return boolean
	 */

	public static String writeResoursToSDCard(String parentPath, String fileName,
											  byte[] content) {
//		Log.v("123", "parentPath :"+parentPath);
		File parentFile = new File(parentPath);

		if (!parentFile.mkdirs()) {
			parentFile.mkdirs();
//			Log.v("123", "parentFile.mkdirs:"+parentFile.mkdirs());
		}
		String path = parentPath + fileName;
//		Log.v("123", "path :"+path);
		FileOutputStream fo = null;
		try {
			fo = new FileOutputStream(path);

			fo.write(content, 0, content.length);
			return path;
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				if (fo != null) {
					fo.close();
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return null;
	}

	/**
	 * 获取SDCard的抽象路径
	 *
	 * @return File
	 */
	public static File getSDCardFile() {
		if (getSDCardState()) {
			return Environment.getExternalStorageDirectory();
		}
		return null;
	}

	/**
	 * 获取SDCard状态
	 *
	 * @return boolean
	 */
	public static boolean getSDCardState() {
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			return true;
		}
		return false;
	}
}