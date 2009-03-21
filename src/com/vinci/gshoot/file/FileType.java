package com.vinci.gshoot.file;

import com.vinci.gshoot.parser.Parser;
import com.vinci.gshoot.parser.PDFParser;
import com.vinci.gshoot.parser.WordParser;

import java.util.List;
import java.util.Arrays;

public class FileType {
    private int type;
    private String name;
    private List<String> extension;
    private Parser parser;

    public static final FileType FILE_EXCEL = new FileType(1, "EXCEL", null, "xls");
    public static final FileType FILE_WORD = new FileType(1, "WORD", new WordParser(), "doc");
    public static final FileType FILE_PPT = new FileType(1, "PPT", null, "ppt");
    public static final FileType FILE_PDF = new FileType(1, "PDF", new PDFParser(), "pdf");
    public static final FileType FILE_TXT = new FileType(1, "TXT", null, "txt");
    public static final FileType FILE_UNKNOWN = new FileType(-1, "unknow", null);

    private FileType(int type, String name, Parser parser, String... extension) {
        this.type = type;
        this.name = name;
        this.parser = parser;
        this.extension = Arrays.asList(extension);
    }

    public int getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public List<String> getExtension() {
        return extension;
    }

    public Parser getParser() {
        return parser;
    }

    public static boolean isValidFileType(String path) {
        String fileName = path.substring(path.lastIndexOf("/") + 1);
        int dotIndex = fileName.lastIndexOf(".");
        if (dotIndex == -1) {
            return false;
        }
        FileType type = FileType.getFileType(fileName.substring(dotIndex));
        return !FILE_UNKNOWN.equals(type);
    }

    public static FileType getFileType(String ext) {
        if (ext == null || "".equals(ext.trim())) {
            return FILE_UNKNOWN;
        }
        if (FILE_EXCEL.getExtension().contains(ext)) {
            return FILE_EXCEL;
        } else if (FILE_WORD.getExtension().contains(ext)) {
            return FILE_WORD;
        } else if (FILE_PPT.getExtension().contains(ext)) {
            return FILE_PPT;
        } else if (FILE_PDF.getExtension().contains(ext)) {
            return FILE_PDF;
        } else if (FILE_TXT.getExtension().contains(ext)) {
            return FILE_TXT;
        } else {
            return FILE_UNKNOWN;
        }
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FileType fileType = (FileType) o;

        if (type != fileType.type) return false;
        if (extension != null ? !extension.equals(fileType.extension) : fileType.extension != null) return false;
        if (name != null ? !name.equals(fileType.name) : fileType.name != null) return false;

        return true;
    }

    public int hashCode() {
        int result;
        result = type;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (extension != null ? extension.hashCode() : 0);
        return result;
    }
}
