/**
 * @author 关小羽的刀
 * @github https://github.com/FlowerBirds/
 * @date 2017年7月28日
 * 
 */
package com.eliteams.quick4j.web.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * @author Administrator
 *
 */
public class IOUtil {
	/**
	 * 将字节数组写入文件中
	 * @param file
	 * @param contents
	 */
	public static void save(File file, byte[] contents) {
		FileOutputStream fs = null;
		try {
			fs = new FileOutputStream(file);
			fs.write(contents);
		} catch (FileNotFoundException e) {
			// Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// Auto-generated catch block
			e.printStackTrace();
		} finally {
			if(fs != null) {
				try {
					fs.close();
				} catch (IOException e) {
					// Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
	
	/**
	 * @param file
	 * @param contents
	 */
	public static void save(File file, String contents) {
		byte[] bytes = contents.getBytes();
		save(file, bytes);
	}
	
	/**
	 * 读取指定文件为字节数组
	 * @param path
	 * @return
	 */
	public static byte[] read(String path) {
		File f = new File(path);
		FileInputStream fi = null;
		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			fi = new FileInputStream(f);
			byte[] buffer = new byte[1024*1024];
			int len = 0 ;
			while((len = fi.read(buffer)) > 0) {
				baos.write(buffer, 0, len);
			}
			return baos.toByteArray();
		} catch (FileNotFoundException e) {
			//  Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			//  Auto-generated catch block
			e.printStackTrace();
		} finally {
			if(fi != null) {
				try {
					fi.close();
				} catch (IOException e) {
					// Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return null;
	}
}
