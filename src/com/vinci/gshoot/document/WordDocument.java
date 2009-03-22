package com.vinci.gshoot.document;

import org.apache.lucene.document.Document;
import org.apache.poi.hwpf.extractor.WordExtractor;

import java.io.*;

public class WordDocument extends AbstractFileDocument {

    void addContentToDocument(Document document, File file) throws Exception {
        String contents = getContent(file);
        // Add contents as Reader-valued Text field, so as to get tokenized and indexed.
        addTextField(document, FIELD_CONTENT, new StringReader(contents));

        // Add summary as UnIndexed field, it is stored and returned with hit documents for display.
        addUnindexedField(document, FIELD_SUMMARY, contents.substring(0, Math.min(contents.length(), SUMMARY_LENGTH)));

        addTextField(document, FIELD_TITLE, file.getName());
    }

    public String getContent(File file) throws Exception {
        return new WordExtractor(new FileInputStream(file)).getText();
    }
}
