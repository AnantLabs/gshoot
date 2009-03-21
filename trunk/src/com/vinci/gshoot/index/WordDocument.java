package com.vinci.gshoot.index;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.poi.hwpf.extractor.WordExtractor;

import java.io.*;

public class WordDocument extends AbstractFileDocument {

    void addContentToDocument(Document document, File file) throws Exception {
        document.add(new Field(FIELD_CONTENT, getContentReader(file)));
    }

    private Reader getContentReader(File file) throws IOException {
        String content = new WordExtractor(new FileInputStream(file)).getText();
        return new StringReader(content);
    }
}
