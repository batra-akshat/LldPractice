package org.example.DistributedLogger;

import lombok.Getter;

@Getter
public class FileSink extends Sink {
    private String location;
    FileSink(String location) {
        super(SinkType.FILE);
        this.location = location;
    }
}
