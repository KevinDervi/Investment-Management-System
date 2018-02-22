package main.java.util;

import java.math.BigDecimal;
import java.time.Instant;

public class StockData {
    private BigDecimal open;
    private BigDecimal high;
    private BigDecimal low;
    private BigDecimal close;
    private Long volume;
    private Instant dateTime;

    public StockData(BigDecimal open, BigDecimal high, BigDecimal low, BigDecimal close, Long volume, Instant dateTime) {
        this.open = open;
        this.high = high;
        this.low = low;
        this.close = close;
        this.volume = volume;
        this.dateTime = dateTime;
    }

    public BigDecimal getOpen() {
        return open;
    }

    public BigDecimal getHigh() {
        return high;
    }

    public BigDecimal getLow() {
        return low;
    }

    public BigDecimal getClose() {
        return close;
    }

    public Long getVolume() {
        return volume;
    }

    public Instant getDateTime() {
        return dateTime;
    }
}
