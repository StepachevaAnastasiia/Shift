package ru.cft.shift;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

enum StatisticMode {
    FULL, SHORT, WITHOUT
}
public class Main {
    public static boolean isBigDecimal(String value) {
        return (formatBigDecimal(value) != null);
    }

    public static BigDecimal formatBigDecimal(String value) {
        if (value == null) {
            return null;
        }

        try {
            return new BigDecimal(value);
        } catch (NumberFormatException e) {
            return null;
        }

    }

    public static boolean isBigInteger(String value) {
        return (formatBigInteger(value) != null);
    }

    public static BigInteger formatBigInteger(String value) {
        if (value == null) {
            return null;
        }

        try {
            return new BigInteger(value);
        } catch (NumberFormatException e) {
            return null;
        }

    }


    public static void main(String[] args) throws IOException {
        String rootProjectPath = Paths.get(System.getProperty("PROJECT_BASE_DIR", "")).toAbsolutePath().toString();
        StatisticMode statisticMode = StatisticMode.WITHOUT;
        List<File> inputFiles = new ArrayList<>();
        boolean appendOutputFiles = false; // append (true) or overwrite (false) an existing file.
        String outputFilePrefix = null;
        String outputFilePath = null;
        for (int i = 0; i < args.length; i++) {
            if (args[i].equals("-a")) { // mode to add to existing files (default: rewrite files)
                appendOutputFiles = true;
            } else if (args[i].equals("-p")) { // prefix for output files
                i++;
                outputFilePrefix = args[i];
            } else if (args[i].equals("-o")) { //path for output files
                i++;
                outputFilePath = args[i];

            } else if (args[i].equals("-s")) { //short statistics
                statisticMode = StatisticMode.SHORT;
            } else if (args[i].equals("-f")) { //full statistics
                statisticMode = StatisticMode.FULL;
            } else if (args[i].endsWith(".txt")) { //input file
                inputFiles.add(new File(args[i]));
            } else {
                System.out.println("Unknown parameter: " + args[i]);
            }
        }
        if (outputFilePrefix == null) {
            outputFilePrefix = "";
        }

        if (outputFilePath == null) {
            outputFilePath = "";
        }

        File outputStringFile = Paths.get(rootProjectPath, outputFilePath, outputFilePrefix + "strings.txt").toFile();
        FileWriter writerStringFile = null;
        File outputIntegerFile = Paths.get(rootProjectPath, outputFilePath, outputFilePrefix + "integers.txt").toFile();
        FileWriter writerIntegerFile = null;
        File outputFloatFile = Paths.get(rootProjectPath, outputFilePath, outputFilePrefix + "floats.txt").toFile();
        FileWriter writerFloatFile = null;

        try {
            writerStringFile = new FileWriter(outputStringFile, appendOutputFiles); // overwrites the file
            writerIntegerFile = new FileWriter(outputIntegerFile, appendOutputFiles);
            writerFloatFile = new FileWriter(outputFloatFile, appendOutputFiles);
        } catch (IOException e) {
            System.out.printf("An exception occurred %s", e.getMessage());

            throw e;
        }

        List<Scanner> scanners = new ArrayList<>();
        boolean isEndOfFile = true;
        IntegerStatistic integerStatistic = new IntegerStatistic();
        FloatStatistic floatStatistic = new FloatStatistic();
        StringStatistic stringStatistic = new StringStatistic();

        List<File> nonExistentFiles = new ArrayList<>();
        for (File file : inputFiles) {
            try {
                scanners.add(new Scanner(file, "UTF-8"));
            } catch (FileNotFoundException e) {
                System.out.printf("%s", e.getMessage());
                nonExistentFiles.add(file);
                //throw e;
            }
        }
        for (int i = 0; i < nonExistentFiles.size(); i++) {
            nonExistentFiles.remove(i);
        }
        while (isEndOfFile) {
            isEndOfFile = false;
            for (Scanner scanner : scanners) {
                if (scanner.hasNextLine()) {
                    try {
                        String str = scanner.nextLine();
                        if (isBigInteger(str)) {
                            writerIntegerFile.write(str + "\n");
                            integerStatistic.numberOfLines++;
                            BigInteger currentInteger = formatBigInteger(str);
                            integerStatistic.setSum(currentInteger);
                            if (integerStatistic.max == null || integerStatistic.min == null) {
                                integerStatistic.max = currentInteger;
                                integerStatistic.min = currentInteger;
                            } else if (currentInteger.compareTo(integerStatistic.max) == 1 ) {
                                integerStatistic.max = currentInteger;
                            } else if (currentInteger.compareTo(integerStatistic.min) == -1 ) {
                                integerStatistic.min = currentInteger;
                            }

                        } else if (isBigDecimal(str)) {
                            writerFloatFile.write(str + "\n");
                            floatStatistic.numberOfLines++;
                            BigDecimal currentFloat = formatBigDecimal(str);
                            floatStatistic.setSum(currentFloat);
                            if (floatStatistic.max == null || floatStatistic.min == null) {
                                floatStatistic.max = currentFloat;
                                floatStatistic.min = currentFloat;
                            } else if (currentFloat.compareTo(floatStatistic.max) == 1 ) {
                                floatStatistic.max = currentFloat;
                            } else if (currentFloat.compareTo(floatStatistic.min) == -1 ) {
                                floatStatistic.min = currentFloat;
                            }

                        } else {
                            writerStringFile.write(str + "\n");
                            stringStatistic.numberOfLines++;
                            int currentLength = str.length();
                            if (stringStatistic.maxLength == 0 || stringStatistic.minLength == 0) {
                                stringStatistic.maxLength = currentLength;
                                stringStatistic.minLength = currentLength;
                            } else if (currentLength > stringStatistic.maxLength) {
                                stringStatistic.maxLength = currentLength;
                            } else if (currentLength < stringStatistic.minLength) {
                                stringStatistic.minLength = currentLength;
                            }
                        }
                    } catch (IOException e) {
                        System.out.printf("An exception occurred %s", e.getMessage());

                        throw e;
                    }

                }
                if (scanner.hasNextLine()) {
                    isEndOfFile = true;
                }
            }
        }

        //show statistics
        if (statisticMode.equals(StatisticMode.SHORT)) {
            System.out.println("Short statistic");
            System.out.println("Number of written Integer elements: " + integerStatistic.numberOfLines);
            System.out.println("Number of written Float elements: " + floatStatistic.numberOfLines);
            System.out.println("Number of written String elements: " + stringStatistic.numberOfLines);
        } else if (statisticMode.equals(StatisticMode.FULL)) {
            System.out.println("Full statistic");
            System.out.println("Number of written Integer elements: " + integerStatistic.numberOfLines);
            System.out.println("MIN integer: " + integerStatistic.min + ", MAX integer: " + integerStatistic.max);
            System.out.println("SUM integer: " + integerStatistic.sum + ", AVERAGE integer: "
                    + integerStatistic.calculateAverage());
            System.out.println("Number of written Float elements: " + floatStatistic.numberOfLines);
            System.out.println("MIN float: " + floatStatistic.min + ", MAX float: " + floatStatistic.max);
            System.out.println("SUM float: " + floatStatistic.sum + ", AVERAGE float: "
                    + floatStatistic.calculateAverage());
            System.out.println("Number of written String elements: " + stringStatistic.numberOfLines);
            System.out.println("Lenght of the sortest string: " + stringStatistic.minLength
                    + ", lenght of the longest string: " + stringStatistic.maxLength);

        }


        for (Scanner scanner : scanners) {
            scanner.close();
        }
        try {
            writerFloatFile.close();
            writerIntegerFile.close();
            writerStringFile.close();
        } catch (IOException e) {
            System.out.printf("An exception occurred %s", e.getMessage());

            throw e;
        }

        if (outputStringFile.length() == 0) {
            outputStringFile.delete();
        } else if (outputFloatFile.length() == 0) {
            outputFloatFile.delete();
        } else if (outputIntegerFile.length() == 0) {
            outputIntegerFile.delete();
        }

    }
}