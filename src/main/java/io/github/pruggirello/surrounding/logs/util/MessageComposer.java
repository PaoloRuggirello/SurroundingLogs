package io.github.pruggirello.surrounding.logs.util;

import io.github.pruggirello.surrounding.logs.annotation.SurroundingLogs;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

import static io.github.pruggirello.surrounding.logs.value.SurroundingType.AFTER;
import static io.github.pruggirello.surrounding.logs.value.SurroundingType.ALL;
import static io.github.pruggirello.surrounding.logs.value.SurroundingType.BEFORE;
import static java.util.Arrays.asList;
import static java.util.Objects.isNull;
import static org.apache.commons.lang3.ObjectUtils.isNotEmpty;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

@Slf4j
@Component
public class MessageComposer {

    public String composeLogMessage(SurroundingLogs annotation, Method method, JoinPointExecutor executor) {
        StringJoiner messageJoiner = new StringJoiner("\n")
                .add("SurroundingLogs of method - " + getMethodSignature(method, executor.getJoinPoint()) + " - in class " + getMethodPath(method));

        if (asList(ALL, BEFORE).contains(annotation.surroundingType())) {
            messageJoiner.add(createInitMethodLog(method, executor.getJoinPoint()));
        }

        if (asList(ALL, AFTER).contains(annotation.surroundingType())) {
            messageJoiner.add(createEndMethodLog(method, executor));
        }

        messageJoiner.add(executionTimeMessage(method, executor.getExecutionTime()));
        return messageJoiner.toString();
    }

    private String createInitMethodLog(Method method, JoinPoint joinPoint) {
        String methodPath = getMethodPath(method);
        int numberOfParameters = joinPoint.getArgs().length;
        List<String> parameters = new ArrayList<>();
        for (int i = 0; i < numberOfParameters; i++) {
            Parameter methodParameter = method.getParameters()[i];
            try {
                Object arg = joinPoint.getArgs()[i];
                parameters.add(arg.getClass().getSimpleName() + " " + method.getParameters()[i].getName() + "=" + arg);
            } catch (Exception e) {
                log.warn("Impossible to convert parameter {}", methodParameter.getName());
            }
        }

        StringJoiner messageJoiner = new StringJoiner(" - ")
                .add("Init execution of method")
                .add(methodPath);
        if (isNotEmpty(parameters)) {
            messageJoiner.add("with parameters");
            parameters.forEach(messageJoiner::add);
        }

        return messageJoiner.toString();
    }

    private String createEndMethodLog(Method method, JoinPointExecutor executor) {
        String methodPath = getMethodPath(method);
        StringJoiner endMessage = new StringJoiner(" - ")
                .add("Ended execution of method")
                .add(methodPath);
        if (isNotBlank(executor.getErrorName())) {
            endMessage.add("with error " + executor.getErrorName());
            endMessage.add(executor.getErrorMessage());
        }
        try {
            String resultMsg = "Response type " + method.getReturnType().getSimpleName() + " = " + executor.getResult();
            return endMessage
                    .add(resultMsg)
                    .toString();
        } catch (Exception e) {
            log.warn("Impossible to convert output parameter of method: {}", method.getName());
        }
        return endMessage.toString();
    }

    private String executionTimeMessage(Method method, ExecutionTime executionTime) {
        if (isNull(executionTime)) {
            return "";
        }
        return new StringJoiner(" ")
                .add("Execution Time info of method:")
                .add(getMethodPath(method))
                .add(executionTime.toString())
                .toString();
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
                log.warn("Impossible to convert method signature parameter for method: {}", method);
            }
        }

        String signature = method.getName() + "(" + parametersJoiner + ")";

        return new StringJoiner(" ")
                .add(method.getReturnType().getSimpleName())
                .add(signature)
                .toString();

    }

}
