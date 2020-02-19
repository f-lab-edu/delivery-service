package me.naming.delieveryservice.aop;

import javax.servlet.http.HttpSession;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Aspect
@Component
public class AuthenticationAop {

  @Before("@annotation(LoginCheck)")
  public void checkSessionUserId() {
    HttpSession httpSession = ((ServletRequestAttributes) (RequestContextHolder.currentRequestAttributes())).getRequest().getSession();
    Object data = httpSession.getAttribute("UserInfo");

    if(data == null)
      throw new IllegalStateException("현재 사용자가 로그인된 상태가 아닙니다.");
  }
}