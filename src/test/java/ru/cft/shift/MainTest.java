package ru.cft.shift;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class MainTest {

    // тест должен создать три файла с именами sample-integers.txt, sample-floats.txt,
    // sample-strings.txt с содержимым как в файлах:
    // src/test/resources/sample-floats.txt,
    // src/test/resources/sample-integers.txt,
    // src/test/resources/sample-strings.txt.
    @Test
    void test() throws IOException {
        Path path = Files.createTempDirectory("tmp");
        Main.main(new String[]{"-o", path.toAbsolutePath().toString(), "-p", "sample-", "in1.txt", "in2.txt"});

        File fileStrings = new File(path + "\\sample-strings.txt");
        File fileFloats = new File(path + "\\sample-floats.txt");
        File fileIntegers = new File(path + "\\sample-integers.txt");

        assertThat(fileStrings).exists();
        assertThat(fileFloats).exists();
        assertThat(fileIntegers).exists();

        File expectedFileStrings = new File("src\\test\\resources\\" + "sample-strings.txt");
        assertThat(fileStrings).hasSameTextualContentAs(expectedFileStrings);

        File expectedFileFloats = new File("src\\test\\resources\\" + "sample-floats.txt");
        assertThat(fileStrings).hasSameTextualContentAs(expectedFileFloats);

        File expectedFileIntegers = new File("src\\test\\resources\\" + "sample-integers.txt");
        assertThat(fileStrings).hasSameTextualContentAs(expectedFileIntegers);
    }
}