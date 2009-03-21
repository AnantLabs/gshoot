package com.vinci.gshoot.file;

import com.vinci.gshoot.utils.Base64Coder;

import java.io.File;
import java.text.SimpleDateFormat;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Date;

public class FileInfo {

    private String path;
    private long fileSize;
    private long lastModified;
    private String name;
    private String extension;
    private FileType fileType;
    private static final DateFormat SIMPLE_DATE_FORMAT = new SimpleDateFormat("yyyy年MM月dd日");
    private String digest;
    private float score;
    private static final NumberFormat PERCENTAGE_FORMAT = NumberFormat.getPercentInstance();

    private FileInfo(String path, String name, String extension, long fileSize, long lastModified, FileType type) {
        this.path = path;
        this.name = name;
        this.extension = extension;
        this.fileSize = fileSize;
        this.lastModified = lastModified;
        this.fileType = type;
    }

    public static FileInfo getFileInfo(String path) {
        return getFileInfo(new File(path));
    }

    public static FileInfo getFileInfo(File file) {
        String extension = getExtension(file.getPath());
        String name = getFileName(file.getPath());
        FileType fileType = FileType.getFileType(extension);
        long lastModified = file.lastModified();
        long fileSize = file.length();
        return new FileInfo(file.getPath(), name, extension, fileSize, lastModified, fileType);
    }

    public String getPath() {
        return path;
    }

    public String getName() {
        return this.name;
    }

    public String getExtension() {
        return extension;
    }

    public long getLastModified() {
        return lastModified;
    }

    public String getFormatedLastModified() {
        if (this.lastModified == 0) {
            return "无法得到文件更新日期，或者文件已经被删除";
        }
        return SIMPLE_DATE_FORMAT.format(new Date(this.lastModified));
    }

    public long getFileSize() {
        return fileSize;
    }

    public String getFormatedFileSize() {
        if (fileSize < 1024) {
            return fileSize + " bytes";
        } else if (fileSize < 1024 * 1024) {
            return fileSize / 1024 + " k";
        } else {
            return fileSize / (1024 * 1024) + " M";
        }
    }

    public FileType getType() {
        return fileType;
    }

    public String getEncodedPath() {
        return Base64Coder.encodeString(this.path);
    }

    private static String getExtension(String path) {
        String fileName = path.substring(path.lastIndexOf("/") + 1);
        int dotIndex = fileName.lastIndexOf(".");
        if (dotIndex == -1) {
            return "";
        }
        return fileName.substring(dotIndex + 1);
    }

    private static String getFileName(String path) {
        String fileName = path.substring(path.lastIndexOf("/") + 1);
        int dotIndex = fileName.lastIndexOf(".");
        return fileName.substring(0, dotIndex);
    }


    public void setDigest(String digest) {
        this.digest = digest;
    }

    public String getDigest() {
        return digest;
    }

    public void setScore(float score) {
        this.score = score;
    }

    public String getScore() {
        return PERCENTAGE_FORMAT.format(this.score);
    }
}
