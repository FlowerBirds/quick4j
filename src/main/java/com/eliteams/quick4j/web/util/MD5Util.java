/**
 * @author 关小羽的刀
 * @github https://github.com/FlowerBirds/
 * @date 2017年7月29日
 * 
 */
package com.eliteams.quick4j.web.util;

import org.apache.commons.codec.digest.DigestUtils;

/**
 * @author Administrator
 *
 */
public class MD5Util {

	/**
	 * 使用MD5加密
	 * @param text
	 * @return
	 */
	public static String encode(String text) {
		return DigestUtils.md5Hex(text);
	}
	
	/**
	 * 对字节数组摘要
	 * @param data
	 * @return
	 */
	public static String digest(byte[] data) {
		return DigestUtils.sha256Hex(data);
	}
	
	/**
	 * 对文件进行摘要
	 * @param file
	 * @return
	 */
	public static String digest(String file) {
		byte[] data = IOUtil.read(file);
		return digest(data);
	}
	
	/**
	 * 摘要验证
	 * @param sign
	 * @param file
	 * @return
	 */
	public static boolean verify(String sign, String file) {
		String d = digest(file);
		return sign != null && sign.equals(d);
	}
}
