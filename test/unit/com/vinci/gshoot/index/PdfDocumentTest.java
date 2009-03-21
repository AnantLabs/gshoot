package com.vinci.gshoot.index;

import org.apache.lucene.document.Document;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

import java.io.File;

public class PdfDocumentTest {
    @Test
    public void should_convert_to_lucene_document_for_pdf_files() {
        String file = "fixtures/pdf/Telecom.pdf";
        Document document = new PdfDocument().toDocument(new File(file));
        assertEquals(file, document.get("path"));
    }
}
