package ru.cft.shift;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class MainTest {
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;

    // Test must create 3 files with specified preffix "sample-" (флаг -p) with names sample-integers.txt,
    // sample-floats.txt,
    // sample-strings.txt :
    @Test
    void checkPreffix() throws IOException {
        Path path = Files.createTempDirectory("tmp");

        System.setProperty(Main.PROJECT_BASE_DIR_ENV_NAME, path.toAbsolutePath().toString());
        String rootProjectPath =  path.toAbsolutePath().toString();

        Main.main(new String[]{"-p", "sample-", getTestFile("in1.txt"), getTestFile("in2.txt")});

        File fileStrings = Paths.get(rootProjectPath, "sample-strings.txt").toFile();
        File fileFloats = Paths.get(rootProjectPath,  "sample-floats.txt").toFile();
        File fileIntegers = Paths.get(rootProjectPath, "sample-integers.txt").toFile();

        assertThat(fileStrings).exists();
        assertThat(fileFloats).exists();
        assertThat(fileIntegers).exists();
    }

    //test checks that result files are created in the specified directory
    @Test
    void checkPath() throws IOException {
        Path path = Files.createTempDirectory("tmp");

        System.setProperty(Main.PROJECT_BASE_DIR_ENV_NAME, path.toAbsolutePath().toString());
        String rootProjectPath =  path.toAbsolutePath().toString();
        String customPath = "/some/path";

        Paths.get(rootProjectPath, customPath).toFile().mkdirs();
        Main.main(new String[]{"-o", customPath , getTestFile("in1.txt"), getTestFile("in2.txt")});
        File fileStrings = Paths.get(rootProjectPath, customPath, "strings.txt").toFile();
        File fileFloats = Paths.get(rootProjectPath, customPath,  "floats.txt").toFile();
        File fileIntegers = Paths.get(rootProjectPath, customPath, "integers.txt").toFile();

        assertThat(fileStrings).exists();
        assertThat(fileFloats).exists();
        assertThat(fileIntegers).exists();
    }

    //test checks that created files have the same contents as:
    // src/test/resources/sample-floats.txt,
    // src/test/resources/sample-integers.txt,
    // src/test/resources/sample-strings.txt.
    @Test
    void checkFilesContent() throws IOException {
        Path path = Files.createTempDirectory("tmp");
        System.setProperty(Main.PROJECT_BASE_DIR_ENV_NAME, path.toAbsolutePath().toString());
        String rootProjectPath =  path.toAbsolutePath().toString();

        Main.main(new String[]{getTestFile("in1.txt"), getTestFile("in2.txt")});

        File fileStrings = Paths.get(rootProjectPath, "strings.txt").toFile();
        File fileFloats = Paths.get(rootProjectPath,  "floats.txt").toFile();
        File fileIntegers = Paths.get(rootProjectPath, "integers.txt").toFile();

        assertThat(fileStrings).exists();
        assertThat(fileFloats).exists();
        assertThat(fileIntegers).exists();

        File expectedFileStrings = new File(getTestFile("sample-strings.txt"));
        assertThat(fileStrings).hasSameTextualContentAs(expectedFileStrings);

        File expectedFileFloats = new File(getTestFile("sample-floats.txt"));
        assertThat(fileFloats).hasSameTextualContentAs(expectedFileFloats);

        File expectedFileIntegers = new File(getTestFile("sample-integers.txt"));
        assertThat(fileIntegers).hasSameTextualContentAs(expectedFileIntegers);
    }

    //check append mode (adding new content to existing result files)
    @Test
    void checkAppendFilesContent() throws IOException {
        Path path = Files.createTempDirectory("tmp");
        System.setProperty(Main.PROJECT_BASE_DIR_ENV_NAME, path.toAbsolutePath().toString());
        String rootProjectPath =  path.toAbsolutePath().toString();

        Main.main(new String[]{"-a", getTestFile("in1.txt"), getTestFile("in2.txt")});
        Main.main(new String[]{"-a", getTestFile("in1.txt"), getTestFile("in2.txt")});

        File fileStrings = Paths.get(rootProjectPath, "strings.txt").toFile();
        File fileFloats = Paths.get(rootProjectPath,  "floats.txt").toFile();
        File fileIntegers = Paths.get(rootProjectPath, "integers.txt").toFile();

        assertThat(fileStrings).exists();
        assertThat(fileFloats).exists();
        assertThat(fileIntegers).exists();

        File expectedFileStrings = new File(getTestFile("append_strings.txt"));
        assertThat(fileStrings).hasSameTextualContentAs(expectedFileStrings);

        File expectedFileFloats = new File(getTestFile("append_floats.txt"));
        assertThat(fileFloats).hasSameTextualContentAs(expectedFileFloats);

        File expectedFileIntegers = new File(getTestFile("append_integers.txt"));
        assertThat(fileIntegers).hasSameTextualContentAs(expectedFileIntegers);
    }

    //check that empty "strings.txt" file isn't created
    @Test
    void checkEmptyFile() throws IOException {
        Path path = Files.createTempDirectory("tmp");
        System.setProperty(Main.PROJECT_BASE_DIR_ENV_NAME, path.toAbsolutePath().toString());
        String rootProjectPath =  path.toAbsolutePath().toString();

        Main.main(new String[]{getTestFile("in3.txt"), getTestFile("in4.txt")});

        File fileStrings = Paths.get(rootProjectPath, "strings.txt").toFile();
        File fileFloats = Paths.get(rootProjectPath,  "floats.txt").toFile();
        File fileIntegers = Paths.get(rootProjectPath, "integers.txt").toFile();

        assertThat(fileStrings).doesNotExist();
        assertThat(fileFloats).exists();
        assertThat(fileIntegers).exists();

    }

    //check short statistic
    @Test
    void checkShortStatistic() throws IOException {
        System.setOut(new PrintStream(outContent));
        Path path = Files.createTempDirectory("tmp");
        System.setProperty(Main.PROJECT_BASE_DIR_ENV_NAME, path.toAbsolutePath().toString());

        Main.main(new String[]{"-s", getTestFile("in1.txt"), getTestFile("in2.txt")});

        assertEquals("Short statistic\r\n" +
                "Number of written Integer elements: 3\r\n" +
                "Number of written Float elements: 3\r\n" +
                "Number of written String elements: 6\r\n", outContent.toString());

        System.setOut(originalOut);
    }

    //check full statistic
    @Test
    void checkFullStatistic() throws IOException {
        System.setOut(new PrintStream(outContent));
        Path path = Files.createTempDirectory("tmp");
        System.setProperty(Main.PROJECT_BASE_DIR_ENV_NAME, path.toAbsolutePath().toString());

        Main.main(new String[]{"-f", getTestFile("in1.txt"), getTestFile("in2.txt")});
        assertEquals("Full statistic\r\n" +
                "Number of written Integer elements: 3\r\n" +
                "MIN integer: 45, MAX integer: 1234567890123456789\r\n" +
                "SUM integer: 1234567890123557334, AVERAGE integer: 411522630041185778\r\n" +
                "Number of written Float elements: 3\r\n" +
                "MIN float: -0.001, MAX float: 3.1415\r\n" +
                "SUM float: 3.1405000000000000000000001528535047, AVERAGE float: 1.046833333333\r\n" +
                "Number of written String elements: 6\r\n" +
                "Lenght of the sortest string: 4, lenght of the longest string: 42\r\n", outContent.toString());

        System.setOut(originalOut);
    }

    private String getTestFile(String fileName) {
        return String.format("src\\test\\resources\\" + fileName);
    }
}