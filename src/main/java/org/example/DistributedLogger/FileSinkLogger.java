package org.example.DistributedLogger;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class FileSinkLogger implements SinkLogger {
    @Override
    public void sendMessageToSink(Message message) {
        var fileSink = (FileSink) message.getSink();
        String location = fileSink.getLocation();

        try {
            // Ensure directory exists
            Files.createDirectories(Paths.get(location).getParent());

            try (FileWriter writer = new FileWriter(location, true)) {
                writer.write(message.getFormattedContent() + System.lineSeparator());
            }
        } catch (IOException e) {
            System.err.println("Failed to write to file: " + location + " - " + e.getMessage());
            // Fallback to console
            System.out.println("FALLBACK: " + message.getFormattedContent());
        }
    }
}
