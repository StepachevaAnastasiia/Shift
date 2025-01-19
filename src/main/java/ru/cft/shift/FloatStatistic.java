package ru.cft.shift;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;


public class FloatStatistic {
    BigDecimal max;
    BigDecimal min;
    BigDecimal sum = BigDecimal.ZERO;
    int numberOfLines;

    public BigDecimal calculateAverage() {
        return sum.divide(BigDecimal.valueOf(numberOfLines),12, RoundingMode.HALF_UP);
    }

    public void addStatistic(BigDecimal currentFloat) {
        numberOfLines++;

        sum = sum.add(currentFloat);

        if (max == null || min == null) {
            max = currentFloat;
            min = currentFloat;
        } else if (currentFloat.compareTo(max) == 1) {
            max = currentFloat;
        } else if (currentFloat.compareTo(min) == -1) {
            min = currentFloat;
        }
    }
}
