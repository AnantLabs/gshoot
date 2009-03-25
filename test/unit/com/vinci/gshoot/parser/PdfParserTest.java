package com.vinci.gshoot.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

import java.io.File;

public class PdfParserTest {
    @Test
    public void should_convert_to_lucene_document_for_pdf_files() throws Exception {
        String file = "fixtures/pdf/Telecom.pdf";
        String content = new PdfParser().parse(new File(file)).getTextContent();
        assertTrue(content.contains("电信"));
    }
}
