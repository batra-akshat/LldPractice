package org.example.DistributedLogger;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class LogService {
    private final LoggerConfiguration loggerConfiguration;

    LogService(LoggerConfiguration configuration) {
        this.loggerConfiguration = configuration;
    }

    public synchronized void sendMessage(String content, LogLevel level) {
        // Check if we should log this level
        if (!level.hasHigherOrEqualPriority(loggerConfiguration.getLogAboveOrEqualToLevel())) {
            System.out.println("Skipping message - priority too low");
            return;
        }

        // Find sink for this level
        var levelConfig = loggerConfiguration.getLevelConfigurations().stream()
                .filter(config -> config.getLevel().equals(level))
                .findFirst();

        if (levelConfig.isEmpty()) {
            throw new IllegalArgumentException("No sink defined for level: " + level);
        }

        var message = new Message(content, level, loggerConfiguration.getNamespace(),
                levelConfig.get().getSink());

        enrichMessages(message);
        sendMessageToSink(message);
    }

    private void enrichMessages(Message message) {
        var enrichers = loggerConfiguration.getEnrichers();
        for (LogEnricher enricher : enrichers) {
            enricher.enrichMessage(message);
        }
    }

    private void sendMessageToSink(Message message) {
        SinkLogger logger = new SinkLoggerFactory(message.getSink()).getLogger();
        logger.sendMessageToSink(message);
    }

}
