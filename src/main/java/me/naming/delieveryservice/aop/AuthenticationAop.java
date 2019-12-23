package me.naming.delieveryservice.aop;

import javax.servlet.http.HttpSession;
import lombok.extern.log4j.Log4j2;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Aspect
@Component
@Log4j2
public class AuthenticationAop {

  @Around("@annotation(OnlyUserIdInterface)")
  public Object userLoginCheckParam1(ProceedingJoinPoint joinPoint) throws Throwable{

    String userId = getSessionId();
    Object resultObj = joinPoint.proceed(new Object[] {userId});
    return resultObj;
  }

  @Around("@annotation(UserInterface)")
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
      throw new IllegalStateException("Session('USER_ID') is not exists");

    return  String.valueOf(data);
  }
}