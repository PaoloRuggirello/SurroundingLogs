package io.github.pruggirello.surrounding.logs.util;

import io.github.pruggirello.surrounding.logs.annotation.SurroundingLogs;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

import static io.github.pruggirello.surrounding.logs.value.SurroundingType.AFTER;
import static io.github.pruggirello.surrounding.logs.value.SurroundingType.ALL;
import static io.github.pruggirello.surrounding.logs.value.SurroundingType.BEFORE;
import static java.util.Arrays.asList;
import static java.util.Objects.nonNull;

@Slf4j
@Component
public class MessageComposer {

    public StringJoiner startComposingLogMessage(SurroundingLogs annotation, Method method, JoinPointExecutor executor) {
        StringJoiner messageJoiner = new StringJoiner("\n")
                .add("SurroundingLogs of method - " + getMethodPath(method));

        messageJoiner.add(formatRow("Method signature", getMethodSignature(method, executor.getJoinPoint())));

        if (asList(ALL, BEFORE).contains(annotation.surroundingType())) {
            String startMessage = createStartMethodLog(method, executor.getJoinPoint());
            messageJoiner.add(formatRow("Method input", startMessage));
        }

        return messageJoiner;
    }

    public String composeLogMessage(SurroundingLogs annotation, Method method, JoinPointExecutor executor, StringJoiner messageJoiner) {
        if (asList(ALL, AFTER).contains(annotation.surroundingType())) {
            String endMessage = createEndMethodLog(method, executor);
            messageJoiner.add(formatRow("Method output", endMessage));
        }

        if (nonNull(executor.getExecutionTime())) {
            messageJoiner.add(formatRow("Execution time", executor.getExecutionTime().toString()));
        }
        return messageJoiner.toString();
    }


    private String formatRow(String prefix, String message) {
        return new StringJoiner(" -> ")
                .add(prefix)
                .add(message)
                .toString();
    }


    private String createStartMethodLog(Method method, JoinPoint joinPoint) {
        int numberOfParameters = joinPoint.getArgs().length;
        List<String> parameters = new ArrayList<>();
        for (int i = 0; i < numberOfParameters; i++) {
            Parameter methodParameter = method.getParameters()[i];
            try {
                Object arg = joinPoint.getArgs()[i];
                parameters.add(arg.getClass().getSimpleName() + " " + method.getParameters()[i].getName() + "=" + arg);
            } catch (Exception e) {
                log.warn("Impossible to convert parameter {}", methodParameter.getName(), e);
            }
        }

        StringJoiner messageJoiner = new StringJoiner(" - ");
        if (!parameters.isEmpty()) {
            parameters.forEach(messageJoiner::add);
        }

        return messageJoiner.toString();
    }

    private String createEndMethodLog(Method method, JoinPointExecutor executor) {
        StringJoiner endMessage = new StringJoiner(" - ");
        if (isNotBlank(executor.getErrorName())) {
            endMessage.add("Error " + executor.getErrorName());
            endMessage.add(executor.getErrorMessage());
        }
        try {
            String resultMsg = method.getReturnType().getSimpleName() + " = " + executor.getResult();
            return endMessage
                    .add(resultMsg)
                    .toString();
        } catch (Exception e) {
            log.warn("Impossible to convert output parameter of method: {}", method.getName(), e);
        }
        return endMessage.toString();
    }

    private String getMethodPath(Method method) {
        return method.getDeclaringClass().getSimpleName() + "." + method.getName();
    }

    private String getMethodSignature(Method method, JoinPoint joinPoint) {
        StringJoiner parametersJoiner = new StringJoiner(", ");
        int numberOfParameters = joinPoint.getArgs().length;
        for (int i = 0; i < numberOfParameters; i++) {
            try {
                parametersJoiner.add(joinPoint.getArgs()[i].getClass().getSimpleName() + " " + method.getParameters()[i].getName());
            } catch (Exception e) {
                log.warn("Impossible to convert method signature parameter for method: {}", method, e);
            }
        }

        String signature = method.getName() + "(" + parametersJoiner + ")";

        return new StringJoiner(" ")
                .add(Modifier.toString(method.getModifiers()))
                .add(method.getReturnType().getSimpleName())
                .add(signature)
                .toString();
    }


    public boolean isNotBlank(String s) {
        return s != null && !s.trim().isEmpty();
    }
}
