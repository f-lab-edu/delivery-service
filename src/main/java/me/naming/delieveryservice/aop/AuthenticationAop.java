package me.naming.delieveryservice.aop;

import java.util.Arrays;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import me.naming.delieveryservice.dto.UserInfoDTO;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Aspect
@Component
public class AuthenticationAop {

  /**
   * execution 내용중 첫번째 '*'은 리턴타입을 나타내며 두번째부터 시작하는 '*(.., @UserInfo (*), ..)'은 @UserInfo 애노테이션이 선언된 부분 양옆의 다른 파라미터 0개 이상을 허용하겠다는 패턴이다.
   * 참고 사이트
   *  - https://haviyj.tistory.com/m/36?category=692364
   *  - https://jojoldu.tistory.com/71
   * @param joinPoint
   * @return
   * @throws Throwable
   */
  @Around("execution(* *(.., @UserInfo (*), ..))")
  public Object convertUser(ProceedingJoinPoint joinPoint) throws Throwable {

    HttpServletRequest request = ((ServletRequestAttributes) (RequestContextHolder.currentRequestAttributes())).getRequest();
    UserInfoDTO userInfoDTO = (UserInfoDTO) request.getSession().getAttribute("UserInfo");

    Object[] args =
        Arrays.stream(joinPoint.getArgs())
            .map(
                data -> {
                  if (data instanceof UserInfoDTO) {
                    data = userInfoDTO;
                  }
                  return data;
                })
            .toArray();

    return joinPoint.proceed(args);
  }

  @Before("@annotation(LoginCheck)")
  public void checkSessionUserId() {
    HttpSession httpSession = ((ServletRequestAttributes) (RequestContextHolder.currentRequestAttributes())).getRequest().getSession();
    Object data = httpSession.getAttribute("UserInfo");

    if(data == null)
      throw new IllegalStateException("현재 사용자가 로그인된 상태가 아닙니다.");

    return  String.valueOf(data);
  }
}