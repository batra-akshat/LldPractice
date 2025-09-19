package org.example.DistributedLogger;

public class SinkLoggerFactory {
    private final Sink sink;
    SinkLoggerFactory(Sink sink) {
        this.sink = sink;
    }
    public SinkLogger getLogger() {
        if(sink instanceof FileSink) {
            return new FileSinkLogger();
        } else if(sink instanceof  ConsoleSink) {
            return new ConsoleSinkLogger();
        } else {
            throw new IllegalArgumentException("No logger found for the sink");
        }
    }
}
