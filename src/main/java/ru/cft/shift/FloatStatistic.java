package ru.cft.shift;

import java.math.BigDecimal;
import java.math.RoundingMode;


public class FloatStatistic {
    BigDecimal max;
    BigDecimal min;
    BigDecimal sum = BigDecimal.ZERO;
    int numberOfLines;

    public BigDecimal calculateAverage() {
        return sum.divide(BigDecimal.valueOf(numberOfLines),12, RoundingMode.HALF_UP);
    }
    public void setSum(BigDecimal number) {
        sum = sum.add(number);
    }
}
