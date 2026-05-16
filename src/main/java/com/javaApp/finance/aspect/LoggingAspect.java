package com.javaApp.finance.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Aspect
@Component
public class LoggingAspect {

    private static final Logger log = LoggerFactory.getLogger(LoggingAspect.class);

    // Логируем все методы в контроллерах
    @Around("execution(* com.javaApp.finance.web..*.*(..))")
    public Object logController(ProceedingJoinPoint joinPoint) throws Throwable {
        String className = joinPoint.getSignature().getDeclaringType().getSimpleName();
        String methodName = joinPoint.getSignature().getName();

        String userInfo = getCurrentUserInfo();

        log.info(">>> {} | {}.{}() | args={}",
                userInfo, className, methodName, Arrays.toString(joinPoint.getArgs()));

        long startTime = System.currentTimeMillis();
        try {
            Object result = joinPoint.proceed();
            long duration = System.currentTimeMillis() - startTime;
            log.info("<<< {} | {}.{}() | duration={}ms",
                    userInfo, className, methodName, duration);
            return result;
        } catch (Exception e) {
            log.error("!!! {} | {}.{}() | error={}",
                    userInfo, className, methodName, e.getMessage());
            throw e;
        }
    }

    // Логируем все методы в сервисах которые меняют данные
    @Around("execution(* com.javaApp.finance.service.*.*(..)) && " +
            "(execution(* *..create*(..)) || " +
            " execution(* *..update*(..)) || " +
            " execution(* *..delete*(..)) || " +
            " execution(* *..register*(..)))")
    public Object logService(ProceedingJoinPoint joinPoint) throws Throwable {
        String className = joinPoint.getSignature().getDeclaringType().getSimpleName();
        String methodName = joinPoint.getSignature().getName();
        String userInfo = getCurrentUserInfo();

        log.info("--- {} | SERVICE | {}.{}() | args={}",
                userInfo, className, methodName, Arrays.toString(joinPoint.getArgs()));

        return joinPoint.proceed();
    }

    private String getCurrentUserInfo() {
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            if (auth != null && auth.isAuthenticated() && !"anonymousUser".equals(auth.getName())) {
                return "user=" + auth.getName();
            }
        } catch (Exception ignored) {
        }
        return "user=anonymous";
    }
}
