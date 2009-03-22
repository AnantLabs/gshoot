package com.vinci.gshoot.document;

import static junit.framework.Assert.assertEquals;
import org.apache.lucene.document.Document;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

import java.io.File;

public class HtmlDocumentTest {
    @Test
    public void should_parse_html_document() throws Exception {
        File f = new File("fixtures/html/jquery.html");
        Document doc = new HtmlDocument().toDocument(f);
        assertEquals(f.getPath(), doc.getField(FileDocument.FIELD_PATH).stringValue());
        String content = doc.get(FileDocument.FIELD_CONTENT);
        assertTrue(content.contains("教程"));
    }

    @Test
    public void should_parse_content_of_html() throws Exception {
        File f = new File("fixtures/html/jquery.html");
        String contents = new HtmlDocument().getContent(f);
        System.out.println(contents);
        assertTrue(contents.contains("教程"));

    }
}