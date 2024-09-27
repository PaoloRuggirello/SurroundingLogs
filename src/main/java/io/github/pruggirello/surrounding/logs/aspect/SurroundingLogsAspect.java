package io.github.pruggirello.surrounding.logs.aspect;

import io.github.pruggirello.surrounding.logs.annotation.SurroundingLogs;
import io.github.pruggirello.surrounding.logs.util.JoinPointExecutor;
import io.github.pruggirello.surrounding.logs.util.MessageComposer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.EnumUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.event.Level;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

import static java.util.Objects.nonNull;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class SurroundingLogsAspect {

    @Value("${surrounding-logs.level:DEBUG}")
    private String loggingLevel;

    private final MessageComposer messageComposer;

    @Around("@annotation(io.github.pruggirello.surrounding.logs.annotation.SurroundingLogs)")
    public Object surroundingLogs(ProceedingJoinPoint joinPoint) throws Throwable {
        System.out.println("Inside aspect");
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Method method = joinPoint.getTarget().getClass().getMethod(methodSignature.getName(), methodSignature.getParameterTypes());
        SurroundingLogs annotation = method.getAnnotation(SurroundingLogs.class);

        JoinPointExecutor executor = JoinPointExecutor.builder()
                .joinPoint(joinPoint)
                .trackExecutionTime(annotation.includeExecutionTime())
                .build();

        try {
            executor.proceed();
        } finally {
            String message = messageComposer.composeLogMessage(annotation, method, executor);
            log.atLevel(computeLoggingLevel(executor, annotation)).log(message);
        }

        return executor.getResult();
    }

    private Level computeLoggingLevel(JoinPointExecutor executor, SurroundingLogs annotation) {
        if (isNotBlank(executor.getErrorName())) {
            return Level.ERROR;
        }
        if (nonNull(annotation.logLevel()) && EnumUtils.isValidEnum(Level.class, annotation.logLevel())) {
            return Level.valueOf(annotation.logLevel());
        }
        return Level.valueOf(loggingLevel);
    }
}
