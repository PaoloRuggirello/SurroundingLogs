package io.github.pruggirello.surrounding.logs.annotation;

import io.github.pruggirello.surrounding.logs.value.SurroundingType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static io.github.pruggirello.surrounding.logs.value.SurroundingType.ALL;

//TODO: add documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface SurroundingLogs {

    String logLevel() default "";

    SurroundingType surroundingType() default ALL;

    boolean includeExecutionTime() default false;
}
