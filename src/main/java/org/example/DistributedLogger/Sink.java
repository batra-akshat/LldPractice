package org.example.DistributedLogger;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Sink {
    private SinkType sinkType;
    Sink(SinkType type) {
        this.sinkType = type;
    }
}
