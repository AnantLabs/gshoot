package com.vinci.gshoot.index;

import org.junit.Test;
import static org.junit.Assert.assertTrue;
import org.apache.lucene.document.Document;

import java.io.File;
import java.io.FileNotFoundException;

import com.vinci.gshoot.index.WordDocument;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;

public class FileDocumentTest {
    @Test
    public void should_load_file_as_document() throws Exception {
        File f = new File("fixtures/word/laborlaw.doc");
        Document doc = new WordDocument().toDocument(f);
        assertEquals(f.getPath(), doc.getField("path").stringValue());
        assertNotNull(doc.getField("modified"));
        assertNotNull(doc.getField("contents"));
    }
}
