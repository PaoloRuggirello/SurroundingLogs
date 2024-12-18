package io.github.pruggirello.surrounding.logs.aspect;

import io.github.pruggirello.surrounding.logs.annotation.SurroundingLogs;
import io.github.pruggirello.surrounding.logs.util.JoinPointExecutor;
import io.github.pruggirello.surrounding.logs.util.MessageComposer;
import io.github.pruggirello.surrounding.logs.util.SurroundingLogsProperties;
import io.github.pruggirello.surrounding.logs.value.LogLevel;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.LoggerFactory;
import org.slf4j.event.Level;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.StringJoiner;

@Aspect
@Component
@RequiredArgsConstructor
public class SurroundingLogsAspect {

    private final SurroundingLogsProperties surroundingLogsProperties;
    private final MessageComposer messageComposer;

    @Around("@annotation(io.github.pruggirello.surrounding.logs.annotation.SurroundingLogs)")
    public Object surroundingLogs(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Method method = joinPoint.getTarget().getClass().getMethod(methodSignature.getName(), methodSignature.getParameterTypes());
        SurroundingLogs annotation = method.getAnnotation(SurroundingLogs.class);

        JoinPointExecutor executor = JoinPointExecutor.builder()
                .joinPoint(joinPoint)
                .trackExecutionTime(annotation.includeExecutionTime())
                .build();

        StringJoiner logMessage = messageComposer.startComposingLogMessage(annotation, method, executor);

        try {
            executor.proceed();
        } finally {
            String message = messageComposer.composeLogMessage(annotation, method, executor, logMessage);
            LoggerFactory.getLogger(joinPoint.getTarget().getClass()).atLevel(computeLoggingLevel(executor, annotation)).log(message);
        }

        return executor.getResult();
    }

    private Level computeLoggingLevel(JoinPointExecutor executor, SurroundingLogs annotation) {
        try {
            if (messageComposer.isNotBlank(executor.getErrorName())) {
                return Level.valueOf(surroundingLogsProperties.getErrorLevel());
            }
            if (annotation.logLevel() != LogLevel.DEFAULT) {
                return Level.valueOf(annotation.logLevel().name());
            }
            return Level.valueOf(surroundingLogsProperties.getLevel());
        }catch (Exception e ) {
            return Level.DEBUG;
        }
    }
}
