package com.vinci.gshoot.utils;

import java.io.File;
import java.io.IOException;
import java.io.FileWriter;

public class FileUtils {
    public static void deleteFilesIn(File dir) {
        if (dir.exists() && dir.isDirectory()) {
            for (File file : dir.listFiles()) {
                deleteIfExist(file);
            }
        }
    }

    public static void deleteIfExist(File file) {
        if (file.exists()) {
            if (file.isDirectory()) {
                for (File f : file.listFiles()) {
                    deleteIfExist(f);
                }
            }
            file.delete();
        }
    }

    public static void createFile(String pathName, String fileName, String content) throws IOException {
        File dir = new File(pathName);
        if (!dir.exists()) {
            dir.mkdir();
        }
        createFile(pathName + File.separator + fileName, content);
    }

    public static void createFile(String pathFileName, String content) throws IOException {
        File file = new File(pathFileName);
        FileWriter fw = null;
        try {
            file.createNewFile();
            fw = new FileWriter(file);
            fw.write(content);
        } finally {
            foreClose(fw);
        }
    }

    public static void updateFileContent(String pathFileName, String content) throws IOException {
        File file = new File(pathFileName);
        deleteIfExist(file);
        FileWriter fw = null;
        try {
            file.createNewFile();
            fw = new FileWriter(file);
            fw.write(content);
        } finally {
            foreClose(fw);
        }
    }

    private static void foreClose(FileWriter fw) {
        if (fw != null) {
            try {
                fw.close();
            } catch (IOException e) {
            }
        }
    }
}
