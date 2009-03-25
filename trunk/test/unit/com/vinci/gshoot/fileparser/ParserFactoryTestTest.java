package com.vinci.gshoot.fileparser;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNull;
import org.junit.Test;

public class ParserFactoryTestTest {
    @Test
    public void should_return_corrent_document() {
        assertTrue(ParserFactory.getParser("file.pdf") instanceof PdfParser);
        assertTrue(ParserFactory.getParser("file.doc") instanceof WordParser);
        assertTrue(ParserFactory.getParser("file.xls") instanceof ExcelParser);
        assertTrue(ParserFactory.getParser("file.ppt") instanceof PptParser);
        assertTrue(ParserFactory.getParser("file.htm") instanceof HtmlParser);
        assertTrue(ParserFactory.getParser("file.html") instanceof HtmlParser);
        assertNull(ParserFactory.getParser("file.js"));
    }
}
