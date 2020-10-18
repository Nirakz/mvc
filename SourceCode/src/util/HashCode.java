package util;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class HashCode {
	public static String md5(String input){
		try {
			//md5  parameter 
			   MessageDigest m = MessageDigest.getInstance("MD5");					
			   m.update(input.getBytes(),0,input.length());  	  
			   String hashText = new BigInteger(1,m.digest()).toString(16);
			   // Now zero pad it to get the full 32 chars.
			   while(hashText.length() < 32 ){
			     hashText = "0"+hashText;
			   }
			   return hashText;
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static String sha1(String input){
		try {
			//sha1 for parameter 
			   MessageDigest m = MessageDigest.getInstance("SHA-1");					
			   m.update(input.getBytes(),0,input.length());  	  
			   String hashText = new BigInteger(1,m.digest()).toString(16);
			   // Now zero pad it to get the full 40 chars.
			   while(hashText.length() < 40 ){
			     hashText = "0"+hashText;
			   }
			   return hashText;
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static String makeUniqueName(String input){
	    String prefix = md5(input+System.currentTimeMillis());
	    return prefix+"_"+input;
	}
	public static String removeMD5prefix(String input){
		return input.substring(33);
	}

}
