package io.github.pruggirello.surrounding.logs.annotation;

import io.github.pruggirello.surrounding.logs.value.LogLevel;
import io.github.pruggirello.surrounding.logs.value.SurroundingType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static io.github.pruggirello.surrounding.logs.value.SurroundingType.ALL;

/**
 * Annotation for logging the input and output of the annotated method.
 * This annotation provides flexibility in logging by allowing configuration
 * of log levels, the type of logging, and whether to include execution time.
 * The available fields are:
 * <ul>
 *   <li><strong>logLevel</strong>: Specifies the logging level to be used (e.g., DEBUG, INFO).</li>
 *   <li><strong>surroundingType</strong>: Defines the type of logging to be performed:
 *       <ul>
 *           <li><strong>ALL</strong>: Logs both the input and output of the method.</li>
 *           <li><strong>BEFORE</strong>: Logs only the input of the method.</li>
 *           <li><strong>AFTER</strong>: Logs only the output of the method.</li>
 *       </ul>
 *   </li>
 *   <li><strong>includeExecutionTime</strong>: Indicates whether to calculate and log the execution time of the method.
 *       When set to <strong>true</strong>, the execution time will be measured and logged; otherwise, it will not.</li>
 * </ul>
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface SurroundingLogs {

    /**
     * Logging level to be used for the method to which the annotation is applied.
     */
    LogLevel logLevel() default LogLevel.DEFAULT;

    /**
     * Defines the type of logging for the method to which the annotation is applied.
     * The possible values are:
     * <ul>
     *   <li><strong>ALL</strong>: Logs both the input and output of the method.</li>
     *   <li><strong>BEFORE</strong>: Logs only the input of the method.</li>
     *   <li><strong>AFTER</strong>: Logs only the output of the method.</li>
     * </ul>
     */
    SurroundingType surroundingType() default ALL;

    /**
     * Indicates whether to calculate and log the execution time of the method to which the annotation is applied.
     * When set to <strong>true</strong>, the execution time will be measured and logged.
     * When set to <strong>false</strong>, the execution time will not be recorded.
     */
    boolean includeExecutionTime() default false;
}
