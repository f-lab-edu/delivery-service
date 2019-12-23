package me.naming.delieveryservice.aop;

import javax.servlet.http.HttpSession;
import lombok.extern.log4j.Log4j2;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Aspect
@Component
@Log4j2
public class AuthenticationAop {

  /**
   * 각 메서드마다 공통적으로 체크하는 로그인 상태를 확인하기 위한 AOP 기능 추가
   * Session에 USER_ID 정보가 존재하는지 체크하고, 존재할 시 파라미터와 동일한 값인지 확인한다.
   * @param httpSession
   */
  @Before(value = "execution(* me.naming.delieveryservice.controller.CustomerController.*UserInfo(javax.servlet.http.HttpSession, ..)) && args(httpSession)")
  public void userLoginCheck(HttpSession httpSession) {

    /**
     * String.valueOf, toString 차이점
     * Object가 null인 경우 toString은 NPE를 발생시킨다.
     * 반면, String.valueOf()는 파라미터가 null일 경우 해당 값을 문자열 "null"을 만들어서 반환한다.
     * 따라서 정확한 null 체크를 위해서는 String.valueOf() 사용X
     */
    Object data = httpSession.getAttribute("USER_ID");
    if(data == null)
      throw new IllegalStateException("Session('USER_ID') is not exists");
  }

  /**
   * Session에 USER_ID 정보가 존재하는지 체크하고, 존재할 경우 타겟 메소드로 userId 정보를 보내준다.
   * @Before 어노테이션을 사용 할 경우 타겟 메소드로 값(userId)을 전달하지 못해 @Around로 변경
   * 장점으로는 AOP에서 Session 정보 확인 후 타겟 메소드로 userId를 전달해줌으로써 Session 정보 한번 호출로 프로세스를 진행시킬 수 있다.
   * 단점으로는 타겟 메서드에 지정되어 있는 매개변수와 AOP에서 전달해주는 매개변수가 일치해야 한다.
   *
   * @param joinPoint
   * @return
   * @throws Throwable
   */
  @Around(value = "execution(* me.naming.delieveryservice.controller.CustomerController.userInfo(..))")
  public Object userLoginCheck(ProceedingJoinPoint joinPoint) throws Throwable{

    HttpSession httpSession = ((ServletRequestAttributes) (RequestContextHolder.currentRequestAttributes())).getRequest().getSession();
    Object data = httpSession.getAttribute("USER_ID");

    if(data == null)
      throw new IllegalStateException("Session('USER_ID') is not exists");

    String userId = String.valueOf(data);
    Object resultObj = joinPoint.proceed(new Object[] {userId});
    return resultObj;
  }
}