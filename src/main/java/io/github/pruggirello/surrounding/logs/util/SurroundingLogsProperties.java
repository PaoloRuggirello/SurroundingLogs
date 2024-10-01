package io.github.pruggirello.surrounding.logs.util;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.slf4j.event.Level;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ConfigurationProperties(prefix = "surrounding-logs")
public class SurroundingLogsProperties {

    /**
     * Properties that define if surroundings logs have to be enabled or not
     */
    private boolean enabled;

    /**
     * Default logging level of @SurroundingLogs logs annotation
     */
    private String level = "DEBUG";
}