package ru.cft.shift;

import java.math.BigInteger;

public class IntegerStatistic {
    BigInteger max;
    BigInteger min;
    BigInteger sum = BigInteger.ZERO;
    int numberOfLines;

    public void addStatistic(BigInteger currentInteger) {
        numberOfLines++;

        sum = sum.add(currentInteger);

        if (max == null || min == null) {
            max = currentInteger;
            min = currentInteger;
        } else if (currentInteger.compareTo(max) == 1) {
            max = currentInteger;
        } else if (currentInteger.compareTo(min) == -1) {
            min = currentInteger;
        }
    }

    public BigInteger calculateAverage() {
        return sum.divide(BigInteger.valueOf(numberOfLines));
    }
}
