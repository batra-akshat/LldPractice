package org.example.circuitBreaker;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Builder
@Setter
public class RunnableResponse {
    private String statusCode;
    private Long timeStamp;
}
