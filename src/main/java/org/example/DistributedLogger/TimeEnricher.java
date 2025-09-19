package org.example.DistributedLogger;

import java.time.LocalDateTime;

public class TimeEnricher implements LogEnricher {

    @Override
    public void enrichMessage(Message message) {
        message.setTimeStamp(LocalDateTime.now());
    }
}
