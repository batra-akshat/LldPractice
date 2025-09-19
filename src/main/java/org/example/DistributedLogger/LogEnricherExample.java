package org.example.DistributedLogger;

import java.util.List;

public class LogEnricherExample {

    public static void main(String[] args) {
        LoggerConfiguration config = LoggerConfiguration.builder()
                .namespace("MyApplication")
                .enrichers(List.of(
                        new TimeEnricher()
                ))
                .logAboveOrEqualToLevel(LogLevel.INFO)
                .levelConfigurations(List.of(
                        LoggerConfiguration.LevelConfiguration.builder()
                                .level(LogLevel.ERROR)
                                .sink(new FileSink("/var/logs/error.log"))
                                .build(),
                        LoggerConfiguration.LevelConfiguration.builder()
                                .level(LogLevel.WARN)
                                .sink(new ConsoleSink())
                                .build(),
                        LoggerConfiguration.LevelConfiguration.builder()
                                .level(LogLevel.INFO)
                                .sink(new ConsoleSink())
                                .build(),
                        LoggerConfiguration.LevelConfiguration.builder()
                                .level(LogLevel.DEBUG)
                                .build()
                ))
                .build();

        LogService logService = new LogService(config);

        // Test all levels
        logService.sendMessage("An error happened", LogLevel.ERROR);
        logService.sendMessage("Warning: low disk space", LogLevel.WARN);
        logService.sendMessage("Application started successfully", LogLevel.INFO);
        logService.sendMessage("Debug: processing user request", LogLevel.DEBUG);
    }
}
