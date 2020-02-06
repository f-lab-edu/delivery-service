package me.naming.delieveryservice.utils;

/**
 * Enum 키 관리하는 클래스를 따로 나눈 이유는 로그인 세션(고객, 배달원, 관리자) 키 값 관리와
 * 캐시 키 값을 모두 관리하기 위해 utils 패키지에 따로 클래스를 생성했습니다.
 */
public enum EnumKeyUtil {
  DELIVERY_MAN_SESSION_KEY,
  CUSTOMER_SESSION_KEY
}