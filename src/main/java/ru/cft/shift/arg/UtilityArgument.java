package ru.cft.shift.arg;

import java.io.File;
import java.nio.file.Path;
import java.util.List;

public class UtilityArgument {
    private final String workdir;
    private final String filePrefix;
    private final boolean append;
    private final StatisticMode statisticMode;
    private final List<File> inputs;

    public UtilityArgument(
            String workdir,
            String filePrefix,
            boolean append,
            StatisticMode statisticMode,
            List<File> inputs
    ) {
        this.workdir = workdir;
        this.filePrefix = filePrefix;
        this.append = append;
        this.statisticMode = statisticMode;
        this.inputs = inputs;
    }

    public String getWorkdir() {
        return workdir;
    }

    public String getFilePrefix() {
        return filePrefix;
    }

    public boolean isAppend() {
        return append;
    }

    public StatisticMode getStatisticMode() {
        return statisticMode;
    }

    public List<File> getInputs() {
        return inputs;
    }
}
