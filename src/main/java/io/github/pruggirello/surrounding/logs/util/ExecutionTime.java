package io.github.pruggirello.surrounding.logs.util;

import lombok.Setter;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.StringJoiner;

@Setter
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


    @Override
    public String toString() {
        return new StringJoiner(" - ")
                .add("start: " + start)
                .add("end:" + end)
                .add("duration: " + durationInMs + " ms")
                .toString();
    }
}
