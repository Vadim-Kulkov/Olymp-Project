package com.olymp_project.aspects;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
@Aspect
public class HttpHandlerAspect {

    @Around("execution(public Object findById(Long))")
    public Object aroundFindByIdAdvice(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        Object targetMethodResult;
        try {
            targetMethodResult = proceedingJoinPoint.proceed();
        } catch (NumberFormatException e) {
            targetMethodResult = new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return targetMethodResult;
    }

}
