package io.github.pruggirello.surrounding.logs.config;

import io.github.pruggirello.surrounding.logs.util.SurroundingLogsProperties;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@Slf4j
@Configuration
@EnableAspectJAutoProxy
@ComponentScan(basePackages = "io.github.pruggirello.surrounding.logs.*")
@EnableConfigurationProperties(SurroundingLogsProperties.class)
@ConditionalOnProperty(
        value = "surrounding-logs.enabled",
        havingValue = "true")
public class SurroundingLogsConfiguration {

    @PostConstruct
    private void postConstruct() {
        log.info("SurroundingLogs loaded - Paperino");
    }
}
