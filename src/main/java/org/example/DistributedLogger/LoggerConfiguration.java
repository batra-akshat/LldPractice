package org.example.DistributedLogger;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@Builder
public class LoggerConfiguration {
    private String namespace;
    List<LevelConfiguration> levelConfigurations;
    List<LogEnricher> enrichers;
    private LogLevel logAboveOrEqualToLevel;
    @Setter
    @Getter
    @Builder
    public static class LevelConfiguration {
        private LogLevel level;
        private Sink sink;
    }
}
