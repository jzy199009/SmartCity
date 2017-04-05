package cn.jzy.smartcity.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Md5Utils {
/**
 * md5加密方法
 * @param password
 * @return
 */
	public static String encode(String password){
		try {
			// 得到一个信息摘要器
			MessageDigest digest = MessageDigest.getInstance("MD5");
			byte[] result = digest.digest(password.getBytes());
			StringBuffer sb = new StringBuffer();
			// 把每一个byte 做一个与运算 0xff;
			for(byte b : result){
				// 与运算
				int number = (int)(b & 0xff) ;
				String str = Integer.toHexString(number);
				if(str.length()==1){
					sb.append("0");
				}
				sb.append(str);
			}
			// 标准的md5加密后的结果
			return sb.toString();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			//can't reach
			return "";
		}
	}
}
