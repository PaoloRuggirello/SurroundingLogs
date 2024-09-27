package io.github.pruggirello.surrounding.logs.config;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@Slf4j
@Configuration
@EnableAspectJAutoProxy
@ComponentScan(basePackages = "io.github.pruggirello.surrounding.logs.*")
@ConditionalOnProperty(
        value = "surrounding-logs.enabled",
        havingValue = "true",
        matchIfMissing = false)
public class SurroundingLogsConfiguration {

    @PostConstruct
    private void postConstruct() {
        log.info("SurroundingLogs loaded");
    }
}
