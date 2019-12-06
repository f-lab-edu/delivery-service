package me.naming.delieveryservice.aop;

import javax.servlet.http.HttpSession;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class AuthenticationAop {

  /**
   * AOP 기능 추가
   * @param id
   * @param httpSession
   */
  @Before(value = "execution(* me.naming.delieveryservice.controller.CustomerController.*UserInfo(String, javax.servlet.http.HttpSession, ..)) && args(id, httpSession)")
  public void checkUserId(String id, HttpSession httpSession) {
    Object data = httpSession.getAttribute("USER_ID");

    if(String.valueOf(data) == null)
      throw new IllegalStateException("Session('USER_ID') is not exists");

    if(id == null)
      throw new NullPointerException("PathVariable Id is not exists");

    if(!StringUtils.equals(id, String.valueOf(data)))
      throw new IllegalArgumentException("Session('USER_ID') PathVariable Id is not same");
  }
}