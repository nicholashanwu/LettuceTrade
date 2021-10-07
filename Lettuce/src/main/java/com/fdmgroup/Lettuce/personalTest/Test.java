package com.fdmgroup.Lettuce.personalTest;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.springframework.util.DigestUtils;

public class Test {
	
	public static void main(String[] args) throws Exception {
		MessageDigest md = MessageDigest.getInstance("SHA-1");
		String passwd = "Hello";
		System.out.println("passwd: "+passwd);
		System.out.println("passwd.getByte(\"UTF-8\"): "+passwd.getBytes("UTF-8"));
		md.update(passwd.getBytes("UTF-8"));
		byte[] result = md.digest();
		System.out.println(new BigInteger(1,result).toString(16));
		System.out.println(String.format("%040x",new BigInteger(1,result)));
		System.out.println(encryptPassword("hello"));

	}
	
	private static String encryptPassword(String originPassword) {
		String encryptedPassword = "";
		try {
			MessageDigest md = MessageDigest.getInstance("SHA-1");
			md.update(originPassword.getBytes("UTF-8"));
			byte[] result = md.digest();
			encryptedPassword=String.format("%040x",new BigInteger(1,result));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return encryptedPassword;
	}
}
