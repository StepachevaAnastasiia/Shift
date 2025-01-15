package ru.cft.shift;

import java.math.BigInteger;

public class IntegerStatistic {
    BigInteger max;
    BigInteger min;
    BigInteger sum = BigInteger.ZERO;
    int numberOfLines;

    public BigInteger calculateAverage() {
        return sum.divide(BigInteger.valueOf(numberOfLines));
    }
    public void setSum(BigInteger number) {
        sum = sum.add(number);
    }

}
