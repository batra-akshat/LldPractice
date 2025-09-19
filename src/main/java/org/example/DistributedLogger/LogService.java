package org.example.DistributedLogger;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class LogService {
    private final LoggerConfiguration loggerConfiguration;

    LogService(LoggerConfiguration configuration) {
        this.loggerConfiguration = configuration;
    }

    public synchronized void sendMessage(String content, LogLevel level) {
        var sink = loggerConfiguration.levelConfigurations.stream().filter(levelConfiguration ->
                levelConfiguration.getLevel().equals(level)).findFirst();
        if(sink.isEmpty()) {
            throw new IllegalArgumentException("No sink defined for this level of message");
        }
        var message = new Message(content, level, loggerConfiguration.getNamespace(), sink.get().getSink());
        if(sink == null) {
            throw new IllegalArgumentException("sink can't be null");
        }
        if(!message.getLevel().hasHigherOrEqualPriority(loggerConfiguration.getLogAboveOrEqualToLevel())) {
            System.out.println("Doing nothing since priority is less");
            return;
        }
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
