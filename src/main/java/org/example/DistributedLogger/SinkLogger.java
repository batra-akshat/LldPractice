package org.example.DistributedLogger;

public interface SinkLogger {
    void sendMessageToSink(Message message);
}
