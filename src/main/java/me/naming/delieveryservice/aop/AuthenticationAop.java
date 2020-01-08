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
   * 로그인 체크
   */
  @Before("@annotation(CheckSessionUserId)")
  public void checkSessionUserId() {
    getSessionId();
  }

  /**
   * 매개변수가 1개(userId)인 경우. 로그인 체크 후 userId 전달
   * @param joinPoint
   * @return
   * @throws Throwable
   */
  @Around("@annotation(UserIdParam)")
  public Object userLoginCheckParam1(ProceedingJoinPoint joinPoint) throws Throwable{

    String userId = getSessionId();
    Object resultObj = joinPoint.proceed(new Object[] {userId});
    return resultObj;
  }

  /**
   * 매개변수가 2개(userId, Object)인 경우. 로그인 체크 후 userId, Object 전달
   * @param joinPoint
   * @return
   * @throws Throwable
   */
  @Around("@annotation(UserIdObjParam)")
  public Object userLoginCheckParam2(ProceedingJoinPoint joinPoint) throws Throwable{

    String userId = getSessionId();
    Object[] objList = joinPoint.getArgs();
    Object getDTO = objList[1];

    Object resultObj = joinPoint.proceed(new Object[] {userId, getDTO});
    return resultObj;

  }

  private String getSessionId() {
    HttpSession httpSession = ((ServletRequestAttributes) (RequestContextHolder.currentRequestAttributes())).getRequest().getSession();
    Object data = httpSession.getAttribute("USER_ID");

    if(data == null)
      throw new IllegalStateException("현재 사용자가 로그인된 상태가 아닙니다.");

    return  String.valueOf(data);
  }
}