package org.example.DistributedLogger;

import java.util.concurrent.ConcurrentHashMap;

public class SinkLoggerFactory {
    // Cache loggers to avoid creating new instances repeatedly
    private static final ConcurrentHashMap<Class<? extends Sink>, SinkLogger> loggerCache =
            new ConcurrentHashMap<>();

    private final Sink sink;

    SinkLoggerFactory(Sink sink) {
        this.sink = sink;
    }

    public SinkLogger getLogger() {
        return loggerCache.computeIfAbsent(sink.getClass(), sinkClass -> {
            if (sink instanceof FileSink) {
                return new FileSinkLogger();
            } else if (sink instanceof ConsoleSink) {
                return new ConsoleSinkLogger();
            } else {
                throw new IllegalArgumentException("No logger found for the sink: " + sinkClass);
            }
        });
    }
}