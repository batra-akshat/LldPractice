package org.example.DistributedLogger;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class FileSinkLogger implements SinkLogger {
    // File-level locks to prevent concurrent writes to same file
    private static final ConcurrentHashMap<String, ReentrantReadWriteLock> fileLocks =
            new ConcurrentHashMap<>();

    @Override
    public void sendMessageToSink(Message message) {
        FileSink fileSink = (FileSink) message.getSink();
        String location = fileSink.getLocation();

        // Get or create lock for this file
        ReentrantReadWriteLock lock = fileLocks.computeIfAbsent(location,
                k -> new ReentrantReadWriteLock());

        lock.writeLock().lock();
        try {
            Path filePath = Paths.get(location);

            // Ensure directory exists
            Files.createDirectories(filePath.getParent());

            // Atomic write operation
            String content = message.getFormattedContent() + System.lineSeparator();
            Files.write(filePath, content.getBytes(),


                    StandardOpenOption.CREATE,
                    StandardOpenOption.APPEND);

        } catch (IOException e) {
            System.err.println("Failed to write to file: " + location + " - " + e.getMessage());
            // Fallback to console
            System.out.println("FALLBACK: " + message.getFormattedContent());
        } finally {
            lock.writeLock().unlock();
        }
    }
}
