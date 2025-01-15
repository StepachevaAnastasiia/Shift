package ru.cft.shift;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class MainTest {

    // тест должен создать три файла с заданным префиксом "sample-" (флаг -p) с именами sample-integers.txt,
    // sample-floats.txt,
    // sample-strings.txt с содержимым как в файлах:
    // src/test/resources/sample-floats.txt,
    // src/test/resources/sample-integers.txt,
    // src/test/resources/sample-strings.txt.
    @Test
    void checkPreffix() throws IOException {
        Path path = Files.createTempDirectory("tmp");

        System.setProperty("PROJECT_BASE_DIR", path.toAbsolutePath().toString());
        String rootProjectPath =  path.toAbsolutePath().toString();

        Main.main(new String[]{"-p", "sample-", "in1.txt", "in2.txt"});

        File fileStrings = Paths.get(rootProjectPath, "sample-strings.txt").toFile();
        File fileFloats = Paths.get(rootProjectPath,  "sample-floats.txt").toFile();
        File fileIntegers = Paths.get(rootProjectPath, "sample-integers.txt").toFile();

        assertThat(fileStrings).exists();
        assertThat(fileFloats).exists();
        assertThat(fileIntegers).exists();
    }

    //тест должен проверять, что файлы с результатами создаются в указанной с помощью флага -o директории
    @Test
    void checkPath() throws IOException {
        Path path = Files.createTempDirectory("tmp");

        System.setProperty("PROJECT_BASE_DIR", path.toAbsolutePath().toString());
        String rootProjectPath =  path.toAbsolutePath().toString();
        String customPath = "/some/path";

        Paths.get(rootProjectPath, customPath).toFile().mkdirs();
        Main.main(new String[]{"-o", customPath , "in1.txt", "in2.txt"});
        File fileStrings = Paths.get(rootProjectPath, customPath, "strings.txt").toFile();
        File fileFloats = Paths.get(rootProjectPath, customPath,  "floats.txt").toFile();
        File fileIntegers = Paths.get(rootProjectPath, customPath, "integers.txt").toFile();

        assertThat(fileStrings).exists();
        assertThat(fileFloats).exists();
        assertThat(fileIntegers).exists();
    }

    //тест проверяет, что файлы создаются с ожидаемым содержимым
    @Test
    void checkFilesContent() throws IOException {
        Path path = Files.createTempDirectory("tmp");
        System.setProperty("PROJECT_BASE_DIR", path.toAbsolutePath().toString());
        String rootProjectPath =  path.toAbsolutePath().toString();

        Main.main(new String[]{"in1.txt", "in2.txt"});

        File fileStrings = Paths.get(rootProjectPath, "strings.txt").toFile();
        File fileFloats = Paths.get(rootProjectPath,  "floats.txt").toFile();
        File fileIntegers = Paths.get(rootProjectPath, "integers.txt").toFile();

        assertThat(fileStrings).exists();
        assertThat(fileFloats).exists();
        assertThat(fileIntegers).exists();

        File expectedFileStrings = new File("src\\test\\resources\\" + "sample-strings.txt");
        assertThat(fileStrings).hasSameTextualContentAs(expectedFileStrings);

        File expectedFileFloats = new File("src\\test\\resources\\" + "sample-floats.txt");
        assertThat(fileFloats).hasSameTextualContentAs(expectedFileFloats);

        File expectedFileIntegers = new File("src\\test\\resources\\" + "sample-integers.txt");
        assertThat(fileIntegers).hasSameTextualContentAs(expectedFileIntegers);
    }
}