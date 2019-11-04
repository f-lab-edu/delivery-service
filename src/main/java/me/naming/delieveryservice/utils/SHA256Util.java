package me.naming.delieveryservice.utils;

import java.security.MessageDigest;

/**
 * 암호기술 종류
 *      1) 대칭키 암호 : AES
 *          - 암*복호화에 같은 암호 키를 사용하는 알고리즘이며, 송신자와 수신자는 암호 키가 노출되지 않도록 관리해야 한다.
 *          - 대칭키 암호는 연산 속도가 빠르다는 장점이 있다.
 *            But, 송*수신자 간에 동일한 공유해야 하므로 맣은 사람들과의 정보 교환 시 키를 관리해야 하는 어려움이 있다.
 *
 *      2) 비대칭키 암호 : RSA
 *          - 대칭키 암호와 달리 암*복호화에 서로 다른 키를 사용하는 것이다.
 *          - 공개키 암호는 수학적인 난제를 기반으로 설계되어 있고 암복호화에 복잡한 수학 연산을 사용하기 때문에, 대칭키 암호에 비해
 *            효율성이 떨어질 수 있다.
 *
 *      3) 해시 함수 : SHA
 *          - 임의의 길이의 메시지를 입력으로 받아 고정된 길이의 해시 값을 출력하는 함수이다.
 *          - 해시 함수에는 암호 키가 존재하지 않는다.
 *          - 따라서 입력 메시지에 대한 변경할 수 없는 증거 값을 만들어냄으로써, 주로 무결성을 제공하는 목적으로 사용된다.
 *
 *  추가 코드 내용
 *      - StringBuffer는 멀티 스레드에서 안전성이 보장되나 StringBuilder는 보장되지 않는다.
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

