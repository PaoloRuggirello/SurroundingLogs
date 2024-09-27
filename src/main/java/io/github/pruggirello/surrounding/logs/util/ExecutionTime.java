package io.github.pruggirello.surrounding.logs.util;

import lombok.Setter;
import lombok.ToString;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Setter
@ToString
public class ExecutionTime {
    private Instant start;
    private Instant end;
    private Long durationInMs;


    public ExecutionTime() {
        this.start = Instant.now();
    }

    public void executionEnded() {
        this.end = Instant.now();
        this.durationInMs = ChronoUnit.MILLIS.between(this.start, this.end);
    }
}
