package ru.cft.shift;

public class StringStatistic {
    int numberOfLines = 0;
    int maxLength = 0;
    int minLength = 0;

    public void addStatistic(String str) {
        numberOfLines++;

        int currentLength = str.length();

        if (maxLength == 0 || minLength == 0) {
            maxLength = currentLength;
            minLength = currentLength;
        } else if (currentLength > maxLength) {
            maxLength = currentLength;
        } else if (currentLength < minLength) {
            minLength = currentLength;
        }
    }
}
