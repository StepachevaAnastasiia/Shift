package ru.cft.shift;

import ru.cft.shift.arg.StatisticMode;
import ru.cft.shift.arg.UtilityArgument;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


public class Main {
    final static String PROJECT_BASE_DIR_ENV_NAME = "PROJECT_BASE_DIR";
    final static String TEXT_CHARSET = "UTF-8";
    final static String STRINGS_FILE_NAME = "strings.txt";
    final static String INTEGERS_FILE_NAME = "integers.txt";
    final static String FLOATS_FILE_NAME = "floats.txt";

    public static void main(String[] args) throws IOException {
        UtilityArgument utilityArgument = Main.parseCommandLineArguments(args);

        CustomFileWriter writerStringFile = new CustomFileWriter(
                Paths.get(utilityArgument.getWorkdir(), utilityArgument.getFilePrefix() + STRINGS_FILE_NAME).toFile(),
                utilityArgument.isAppend()
        );

        CustomFileWriter writerIntegerFile = new CustomFileWriter(
                Paths.get(utilityArgument.getWorkdir(), utilityArgument.getFilePrefix() + INTEGERS_FILE_NAME).toFile(),
                utilityArgument.isAppend()
        );

        CustomFileWriter writerFloatFile = new CustomFileWriter(
                Paths.get(utilityArgument.getWorkdir(), utilityArgument.getFilePrefix() + FLOATS_FILE_NAME).toFile(),
                utilityArgument.isAppend()
        );

        List<Scanner> scanners = new ArrayList<>();

        for (File file : utilityArgument.getInputs()) {
            try {
                scanners.add(new Scanner(file, TEXT_CHARSET));
            } catch (FileNotFoundException e) {
                System.out.printf("%s", e.getMessage());
            }
        }

        IntegerStatistic integerStatistic = new IntegerStatistic();
        FloatStatistic floatStatistic = new FloatStatistic();
        StringStatistic stringStatistic = new StringStatistic();

        boolean isEndOfFile = true;

        while (isEndOfFile) {
            isEndOfFile = false;

            for (Scanner scanner : scanners) {
                if (scanner.hasNextLine()) {
                    try {
                        String str = scanner.nextLine();

                        if (isBigInteger(str)) {
                            writerIntegerFile.writeNewLine(str);
                            integerStatistic.addStatistic(formatBigInteger(str));
                        } else if (isBigDecimal(str)) {
                            writerFloatFile.writeNewLine(str);
                            floatStatistic.addStatistic(formatBigDecimal(str));
                        } else {
                            writerStringFile.writeNewLine(str);
                            stringStatistic.addStatistic(str);
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
        if (utilityArgument.getStatisticMode().equals(StatisticMode.SHORT)) {
            printShortStatistic(integerStatistic, floatStatistic, stringStatistic);
        } else if (utilityArgument.getStatisticMode().equals(StatisticMode.FULL)) {
            printFullStatistic(integerStatistic, floatStatistic, stringStatistic);
        }

        try {
            for (Scanner scanner : scanners) {
                scanner.close();
            }

            writerFloatFile.close();
            writerIntegerFile.close();
            writerStringFile.close();
        } catch (IOException e) {
            System.out.printf("An exception occurred %s", e.getMessage());
        }
    }

    private static void printFullStatistic(
            IntegerStatistic integerStatistic,
            FloatStatistic floatStatistic,
            StringStatistic stringStatistic
    ) {
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

    private static void printShortStatistic(
            IntegerStatistic integerStatistic,
            FloatStatistic floatStatistic,
            StringStatistic stringStatistic
    ) {
        System.out.println("Short statistic");
        System.out.println("Number of written Integer elements: " + integerStatistic.numberOfLines);
        System.out.println("Number of written Float elements: " + floatStatistic.numberOfLines);
        System.out.println("Number of written String elements: " + stringStatistic.numberOfLines);
    }

    public static UtilityArgument parseCommandLineArguments(String[] args) {
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
                File candidate = new File(args[i]);

                if (candidate.exists()) {
                    inputFiles.add(new File(args[i]));
                } else {
                    System.out.println(String.format("File %s doesn't exist.", args[i]));
                }
            } else {
                System.out.println("Unknown parameter: " + args[i]);
            }
        }

        return new UtilityArgument(
                outputFilePath == null ? rootProjectPath() : Paths.get(rootProjectPath(), outputFilePath).toString(),
                outputFilePrefix == null ? "" : outputFilePrefix,
                appendOutputFiles,
                statisticMode,
                inputFiles
        );
    }

    public static String rootProjectPath() {
        return Paths.get(System.getProperty(PROJECT_BASE_DIR_ENV_NAME, ""))
                .toAbsolutePath()
                .toString();
    }

    public static boolean isBigDecimal(String value) {
        return (formatBigDecimal(value) != null);
    }

    public static BigDecimal formatBigDecimal(String value) {
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
        try {
            return new BigInteger(value);
        } catch (NumberFormatException e) {
            return null;
        }

    }
}