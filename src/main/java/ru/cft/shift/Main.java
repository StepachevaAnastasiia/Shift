package ru.cft.shift;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
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
    public static boolean isInteger(String strNum) {
        if (strNum == null) {
            return false;
        }
        try {
            int d = Integer.parseInt(strNum);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }

    public static boolean isFloat(String strNum) {
        if (strNum == null) {
            return false;
        }
        try {
            float d = Float.parseFloat(strNum);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }


    public static void main(String[] args) {
        StatisticMode statisticMode = StatisticMode.WITHOUT;
        List<File> inputFiles = new ArrayList<>();
        boolean appendOutputFiles = false; // append (true) or overwrite (false) an existing file.
        String outputFilePrefix = null;
        String outputFilePath = null;
        for (int i = 0; i < args.length; i++) {
            System.out.println(args[i]);
            if (args[i].equals("-a")) { // mode to add to existing files (default: rewrite files)
                appendOutputFiles = true;
            } else if (args[i].equals("-p")) { // prefix for output files
                i++;
                outputFilePrefix = args[i];
            } else if (args[i].equals("-o")) { //path for output files
                i++;
                outputFilePath = args[i];
                if (!outputFilePath.endsWith("\\")) {
                    outputFilePath += "\\";
                }
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

        File outputStringFile = new File(outputFilePath + outputFilePrefix + "strings.txt");
        FileWriter writerStringFile = null;
        try {
            writerStringFile = new FileWriter(outputStringFile, appendOutputFiles); // overwrites the file
        } catch (IOException e) {
            System.out.printf("An exception occurred %s", e.getMessage());
        }

        File outputIntegerFile = new File(outputFilePath + outputFilePrefix + "integers.txt");
        FileWriter writerIntegerFile = null;
        try {
            writerIntegerFile = new FileWriter(outputIntegerFile, appendOutputFiles);
        } catch (IOException e) {
            System.out.printf("An exception occurred %s", e.getMessage());
        }

        File outputFloatFile = new File(outputFilePath + outputFilePrefix + "floats.txt");
        FileWriter writerFloatFile = null;
        try {
            writerFloatFile = new FileWriter(outputFloatFile, appendOutputFiles);
        } catch (IOException e) {
            System.out.printf("An exception occurred %s", e.getMessage());
        }

        List<Scanner> scanners = new ArrayList<>();
        boolean isEndOfFile = true;
        for (File file : inputFiles) {
            try {
                scanners.add(new Scanner(file, "UTF-8"));
            } catch (FileNotFoundException e) {
                System.out.printf("An exception occurred %s", e.getMessage());
            }
        }
        while (isEndOfFile) {
            isEndOfFile = false;
            for (Scanner scanner : scanners) {
                if (scanner.hasNextLine()) {
                    try{
                        String str = scanner.nextLine();
                        if (isBigInteger(str)) {
                            writerIntegerFile.write(str + "\n");
                        } else if (isBigDecimal(str)) {
                            writerFloatFile.write(str + "\n");
                        } else {
                            writerStringFile.write(str + "\n");
                        }
                    } catch (IOException e) {
                        System.out.printf("An exception occurred %s", e.getMessage());
                    }

                }
                if (scanner.hasNextLine()) {
                    isEndOfFile = true;
                }
            }
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
        }

    }
}