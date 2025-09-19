package org.example.DistributedLogger;

import java.util.List;

public class LogEnricherExample {

    public static void main(String[] args) {
        LoggerConfiguration loggerConfiguration = LoggerConfiguration.builder()
                .namespace("Test namespace")
                .enrichers(List.of(new TimeEnricher()))
                .logAboveOrEqualToLevel(LogLevel.INFO)
                .levelConfigurations(List.of(
                        LoggerConfiguration.LevelConfiguration.builder()
                                .level(LogLevel.INFO)
                                .sink(new ConsoleSink())
                                .build(),
                        LoggerConfiguration.LevelConfiguration.builder()
                                .level(LogLevel.ERROR)
                                .sink(new FileSink("xyz/abc/logs/error"))
                                .build(),
                        LoggerConfiguration.LevelConfiguration.builder()
                                .level(LogLevel.DEBUG)
                                .sink(new FileSink("xyz/abc/log/debug"))
                                .build(),
                        LoggerConfiguration.LevelConfiguration.builder()
                                .level(LogLevel.WARN)
                                .sink(new FileSink("xyz/abc/log/warn"))
                                .build()
                ))
                .build();
        var logService = new LogService(loggerConfiguration);
        logService.sendMessage("This is an info log", LogLevel.INFO);
        logService.sendMessage("This is an error log", LogLevel.DEBUG);
    }
}
