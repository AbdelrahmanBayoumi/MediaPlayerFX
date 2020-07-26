package com.bayoumi.util;

import java.io.File;
import java.io.FileReader;
import java.util.*;

public class PropertiesUtils {
    private static final String FILE_NAME = "com/bayoumi/config/details.properties";

    public static Map<String, String> readDetails() {
        Map<String, String> map = new HashMap<>();
        try {
            ClassLoader classLoader = ClassLoader.getSystemClassLoader();
            File file = new File(Objects.requireNonNull(classLoader.getResource(FILE_NAME)).getFile());
            Properties p = new Properties();
            p.load(new FileReader(file));
            map.put("name", p.getProperty("name"));
            map.put("version", p.getProperty("version"));
            map.put("link", p.getProperty("link"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }

    public static List<String> readFormats() {
        List<String> formats = null;
        try {
            ClassLoader classLoader = ClassLoader.getSystemClassLoader();
            File file = new File(Objects.requireNonNull(classLoader.getResource(FILE_NAME)).getFile());
            Properties p = new Properties();
            p.load(new FileReader(file));
            formats = Arrays.asList(p.getProperty("formats").split(","));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return formats;
    }
}
