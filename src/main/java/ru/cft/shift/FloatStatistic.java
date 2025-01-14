package ru.cft.shift;

import java.math.BigDecimal;


public class FloatStatistic {
    BigDecimal max;
    BigDecimal min;
    BigDecimal sum = BigDecimal.ZERO;
    int numberOfLines;

    public BigDecimal calculateAverage() {
        return sum.divide(BigDecimal.valueOf(numberOfLines));
    }
    public void setSum(BigDecimal number) {
        sum.add(number);
    }
}
