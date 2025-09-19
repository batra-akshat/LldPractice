package org.example.DistributedLogger;

public class ConsoleSinkLogger implements SinkLogger {

    @Override
    public void sendMessageToSink(Message message) {
        System.out.println(message.getFormattedContent());
    }
}
