package ru.cft.shift;

import java.io.File;
import java.nio.charset.Charset;

public class Shift {
    public static void main(String[] args) {
        System.out.println("\u0416");
        System.out.println(String.format("file.encoding: %s", System.getProperty("file.encoding")));
        System.out.println(String.format("defaultCharset: %s", Charset.defaultCharset().name()));
    }
}
