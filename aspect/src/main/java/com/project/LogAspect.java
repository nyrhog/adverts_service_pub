package com.project;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

import java.util.Arrays;
import java.util.stream.Collectors;

@Aspect
@Slf4j
@Component
public class LogAspect {

    @Pointcut("@annotation(Logging)")
    public void callAtService() {
    }

    @Before("callAtService()")
    public void beforeCallAtMethod1(JoinPoint jp) {
        String args = Arrays.stream(jp.getArgs())
                .map(Object::toString)
                .collect(Collectors.joining(","));


        log.info("Starting " + jp.toString());
        constructLogMessage(String.valueOf(jp.getSignature()), args);
    }

    private void constructLogMessage(String message, String args){
        String[] s = message.split(" ");
        String returnType = s[0];

        String substring;
        substring = s[1].substring(s[1].indexOf("(") + 1);
        substring = substring.substring(0, substring.indexOf(")"));

        String[] parametersNames = substring.split(",");

        log.info("Method arguments: {} with value: {}, method return type: {}", parametersNames, args, returnType);
    }

    @After("callAtService()")
    public void afterCallAt(JoinPoint jp) {
        log.info("Method was executed successfully " + jp.toString());
    }

    @Around("callAtService()")
    public Object aroundCallAt(ProceedingJoinPoint call) throws Throwable {
        StopWatch clock = new StopWatch(call.toString());
        try {
            clock.start(call.toShortString());
            return call.proceed();
        } finally {
            String taskName = clock.currentTaskName();
            clock.stop();
            log.info("Method {} worked {} milliseconds", taskName, clock.getTotalTimeMillis());
        }
    }
}

