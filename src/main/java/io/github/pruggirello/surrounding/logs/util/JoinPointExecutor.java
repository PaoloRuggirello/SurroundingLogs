package io.github.pruggirello.surrounding.logs.util;

import lombok.Builder;
import lombok.Getter;
import org.aspectj.lang.ProceedingJoinPoint;


@Getter
@Builder
public class JoinPointExecutor {
    private ProceedingJoinPoint joinPoint;
    private boolean trackExecutionTime;
    private ExecutionTime executionTime;
    private String errorName;
    private String errorMessage;
    private Object result;


    private void startExecutionTime() {
        if (!trackExecutionTime) {
            return;
        }
        executionTime = new ExecutionTime();
    }

    private void endExecutionTime() {
        if (!trackExecutionTime || executionTime == null) {
            return;
        }
        executionTime.executionEnded();
    }

    public void proceed() throws Throwable {
        startExecutionTime();
        try {
            this.result = joinPoint.proceed();
        } catch (Exception e) {
            this.errorName = e.getClass().getSimpleName();
            this.errorMessage = e.getMessage();
            throw e;
        } finally {
            endExecutionTime();
        }
    }
}
