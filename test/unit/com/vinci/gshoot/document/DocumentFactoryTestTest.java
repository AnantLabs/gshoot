package com.vinci.gshoot.document;

import org.junit.Test;
import static org.junit.Assert.assertTrue;

public class DocumentFactoryTestTest {
    @Test
    public void should_return_corrent_document() {
        assertTrue(DocumentFactory.getFileDocument("file.pdf") instanceof PdfDocument);
        assertTrue(DocumentFactory.getFileDocument("file.doc") instanceof WordDocument);
        assertTrue(DocumentFactory.getFileDocument("file.xls") instanceof ExcelDocument);
    }
}
