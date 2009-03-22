package com.vinci.gshoot.document;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

import java.io.*;
import java.util.Iterator;

public class ExcelDocument extends AbstractFileDocument {
    private Logger logger = Logger.getLogger(ExcelDocument.class);

    void addContentToDocument(Document document, File file) throws Exception {
        String contents = getContent(file);
        
        // Add contents as Reader-valued Text field, so as to get tokenized and indexed.
        addTextField(document, FIELD_CONTENT, new StringReader(contents));

        // Add summary as UnIndexed field, it is stored and returned with hit documents for display.
        addUnindexedField(document, FIELD_SUMMARY, contents.substring(0, Math.min(contents.length(), SUMMARY_LENGTH)));

        addTextField(document, FIELD_TITLE, file.getName());

    }

    public String getContent(File file) throws Exception {
        return extract(file);
    }

    private String extract(File xlsPath) throws Exception {
        StringBuilder sb = new StringBuilder();
        InputStream inputStream = null;

        try {
            inputStream = new FileInputStream(xlsPath);

            POIFSFileSystem fileSystem = new POIFSFileSystem(inputStream);

            HSSFWorkbook workBook = new HSSFWorkbook(fileSystem);
            int sheetNumber = workBook.getNumberOfSheets();
            for (int i = 0; i < sheetNumber; i++) {
                sb.append("Sheet " + (i + 1) + ": " + workBook.getSheetName(i));
                sb.append("\n");
                sb.append(extractSheet(workBook.getSheetAt(i)));
                sb.append("\n");
            }
        } finally {
            IOUtils.closeQuietly(inputStream);
        }
        return sb.toString();
    }

    private String extractSheet(HSSFSheet sheet) {
        StringBuilder sb = new StringBuilder();
        Iterator<HSSFRow> rows = sheet.rowIterator();

        while (rows.hasNext()) {
            sb.append(extractRow(rows.next()));
            sb.append("\n");
        }

        return sb.toString();
    }

    private String extractRow(HSSFRow row) {
        StringBuilder sb = new StringBuilder();

        Iterator<HSSFCell> cells = row.cellIterator();
        while (cells.hasNext()) {
            HSSFCell cell = cells.next();
            sb.append(extractCell(cell));
            sb.append("\t");
        }
        return sb.toString().trim();
    }

    private String extractCell(HSSFCell cell) {
        String value = "";
        switch (cell.getCellType()) {
            case HSSFCell.CELL_TYPE_NUMERIC:
                value = String.valueOf(cell.getNumericCellValue());
                break;
            case HSSFCell.CELL_TYPE_STRING:
                value = cell.getRichStringCellValue().getString();
                break;
            case HSSFCell.CELL_TYPE_BLANK:
                value = "";
                break;
            case HSSFCell.CELL_TYPE_BOOLEAN:
                value = String.valueOf(cell.getBooleanCellValue());
                break;
            case HSSFCell.CELL_TYPE_FORMULA:
                value = String.valueOf(cell.getCellFormula());
                break;
            case HSSFCell.CELL_TYPE_ERROR:
                value = String.valueOf(cell.getErrorCellValue());
                break;
            default:
                logger.warn("Type not supported: " + cell.getCellType());
                break;
        }
        return value;
    }
}
