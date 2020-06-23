package rw.tajyire.api.aop.aspect;

import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import rw.tajyire.api.util.JwtUtil;

@Component
@Aspect
@Slf4j
public class UserAspectAccess {
  @Autowired
  HttpServletRequest httpServletRequest;
  @Autowired
  private JwtUtil jwtUtil;

  @Before(value = "rw.tajyire.api.aop.AspectDefinition.secureEndpoints()")
  public void beforeAdvice(JoinPoint joinPoint) {
    final String token = httpServletRequest.getHeader("Authorization").replace("Bearer ", "");
    final String username = jwtUtil.extractUsername(token);
    try {
      log.info("user {} accessed {}", username, joinPoint.getSignature());
    } catch (Exception e) {
      log.warn("Access of user:{} failed", username);
      log.warn("Reason::{}", e.getLocalizedMessage());
    }
  }
}
