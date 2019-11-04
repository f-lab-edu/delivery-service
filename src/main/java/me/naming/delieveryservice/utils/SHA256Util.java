package me.naming.delieveryservice.utils;

import java.security.MessageDigest;


/**
 * StringBuffer는 멀티 스레드에서 안전성이 보장되나 StringBuilder는 보장되지 않는다.
 */
public class SHA256Util {
    public static String encrypt(String planText) {
        try{
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(planText.getBytes());
            byte byteData[] = md.digest();
            StringBuffer sb = new StringBuffer();

            for (int i = 0; i < byteData.length; i++) {
                sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
            }

            StringBuffer hexString = new StringBuffer();
            for (int i=0;i<byteData.length;i++) {
                String hex=Integer.toHexString(0xff & byteData[i]);
                if(hex.length()==1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }

            return hexString.toString();
        } catch(Exception e){
            e.printStackTrace();
            throw new RuntimeException();
        }
    }
}

