package me.naming.delieveryservice.aop;

import javax.servlet.http.HttpSession;
import lombok.NonNull;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Log4j2
public class AuthenticationAop {

  /**
   * 각 메서드마다 공통적으로 체크하는 로그인 상태를 확인하기 위한 AOP 기능 추가
   * Session에 USER_ID 정보가 존재하는지 체크하고, 존재할 시 파라미터와 동일한 값인지 확인한다.
   * @param id
   * @param httpSession
   */
  @Before(value = "execution(* me.naming.delieveryservice.controller.CustomerController.*UserInfo(String, javax.servlet.http.HttpSession, ..)) && args(id, httpSession)")
  public void checkUserId(@NonNull String id, HttpSession httpSession) {
    Object data = httpSession.getAttribute("USER_ID");

    /**
     * String.valueOf, toString 차이점
     * Object가 null인 경우
     * - toString은 NPE를 발생시킨다.
     * - 반면, String.valueOf()는 파라미터가 null일 경우 해당 값을 문자열 "null"을 만들어서 반환한다.
     * 따라서 정확한 null 체크를 위해서는 String.valueOf() 사용X
     */
    if(data == null)
      throw new IllegalStateException("Session('USER_ID') is not exists");

    if(!StringUtils.equals(id, String.valueOf(data)))
      throw new IllegalArgumentException("Session('USER_ID') PathVariable Id is not same");
  }
}