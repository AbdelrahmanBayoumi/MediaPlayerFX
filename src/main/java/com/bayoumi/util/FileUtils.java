package com.bayoumi.util;

import java.io.File;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

public class FileUtils {
    public static List<Path> convertListFileToListPath(List<File> listOfFile) {
        return listOfFile.stream().map(File::toPath).collect(Collectors.toList());
    }
}
