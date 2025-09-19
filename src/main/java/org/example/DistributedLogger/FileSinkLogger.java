package org.example.DistributedLogger;

public class FileSinkLogger implements SinkLogger {

    @Override
    public void sendMessageToSink(Message message) {
        var fileSync = (FileSink) message.getSink();
        System.out.println(message.getTime() + " Sending message to file sync with location " + fileSync.getLocation());
    }
}
