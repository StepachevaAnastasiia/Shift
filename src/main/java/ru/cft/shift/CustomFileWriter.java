package ru.cft.shift;

import java.io.Closeable;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class CustomFileWriter implements Closeable {
    private final static String NEW_LINE = "\n";
    private final File file;
    private final boolean append;
    private FileWriter fileWriter;

    public CustomFileWriter(File file, boolean append) {
        this.file = file;
        this.append = append;
    }

    public void writeNewLine(String str) throws IOException {
        // Creates file on the first write only.
        if (fileWriter == null) {
            fileWriter = new FileWriter(file, append);
        }

        fileWriter.write(str + NEW_LINE);
    }

    @Override
    public void close() throws IOException {
        if (fileWriter != null) {
            fileWriter.close();
        }
    }
}
