package com.vinci.gshoot.document;

import org.apache.lucene.document.Document;
import org.apache.poi.hslf.extractor.PowerPointExtractor;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;

import com.vinci.gshoot.document.ppt.TextHSLFSlideShow;

public class PptDocument extends AbstractFileDocument {

    void addContentToDocument(Document document, File file) throws Exception {
        String contents = getContent(file);

        // Add contents as Reader-valued Text field, so as to get tokenized and indexed.
        addTextField(document, FIELD_CONTENT, new StringReader(contents));

        // Add summary as UnIndexed field, it is stored and returned with hit documents for display.
//        addUnindexedField(document, FIELD_SUMMARY, contents.substring(0, Math.min(contents.length(), SUMMARY_LENGTH)));

        addTextField(document, FIELD_TITLE, file.getName());
    }

    public String getContent(File file) throws Exception {
        return extract(file.getPath());
    }

    private String extract(String filePath) throws IOException {
        return new PowerPointExtractor(new TextHSLFSlideShow(filePath)).getText();
    }
}
