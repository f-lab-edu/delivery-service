package me.naming.delieveryservice.aop;

import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.HandlerMapping;

@Aspect
@Component
public class AuthenticationAop {

  @Before("@annotation(me.naming.delieveryservice.aop.CheckPathVariableUserId)")
  public void checkPathVariableUserId() {
    HttpServletRequest request = ((ServletRequestAttributes) (RequestContextHolder.currentRequestAttributes())).getRequest();
    Object data = request.getSession().getAttribute("USER_ID");

    Map mapPathVariable = (Map) request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
    String userId = mapPathVariable.get("userId").toString();

    if(data == null)
      throw new IllegalStateException("현재 로그인되어있지 않습니다");

    if(!StringUtils.equals(String.valueOf(data), userId))
      throw new IllegalStateException("URI의 파라미터와 세션정보가 일치하지 않습니다.");
  }
}