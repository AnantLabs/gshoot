package com.vinci.gshoot.index;

public class FileDocumentFactory {
    public static FileDocument getFileDocument(String path) {
        String extention = getExtension(path);
        if ("doc".equals(extention) || "docs".equals(extention)) {
            return new WordDocument();
        } else if ("pdf".equals(extention)) {
            return new PdfDocument();
        } else if ("xls".equals(extention)) {
            return new ExcelDocument();
        }
        return null;
    }

    private static String getExtension(String path) {
        String fileName = path.substring(path.lastIndexOf("/") + 1);
        int dotIndex = fileName.lastIndexOf(".");
        if (dotIndex == -1) {
            return "";
        }
        return fileName.substring(dotIndex + 1);
    }
}
