package me.naming.delieveryservice.aop;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

/**
 * 커스텀 어노테이션 생성
 * 사용이유 : 로그인 후 사용자 정보를 받기 위한 메소드를 구분시키기 위해 사용
 * 컨트롤러의 매개변수에서 사용하기 위하여 @Target 범위 지정
 * 참고 : https://elfinlas.github.io/2017/12/14/java-custom-anotation-01/
 */
@Target(ElementType.PARAMETER)
public @interface UserInfo {

}