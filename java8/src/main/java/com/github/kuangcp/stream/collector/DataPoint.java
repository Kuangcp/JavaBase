
package com.github.kuangcp.stream.collector;

import java.time.LocalDateTime;

public class DataPoint {
    private LocalDateTime timestamp;
    private Integer value;

    public DataPoint(LocalDateTime timestamp, Integer value) {
        this.timestamp = timestamp;
        this.value = value;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public Integer getValue() {
        return value;
    }
}
