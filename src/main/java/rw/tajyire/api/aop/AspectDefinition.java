package rw.tajyire.api.aop;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

@Aspect
public class AspectDefinition {
  @Pointcut("target(rw.tajyire.api.controller.AuthController))")
  public void authEndpoints() {}

  @Pointcut("authEndpoints()")
  public void permittedEndpoints() {}

  @Pointcut("!permittedEndpoints()&&within(rw.tajyire.api.controller..*)")
  public void secureEndpoints() {}
}
