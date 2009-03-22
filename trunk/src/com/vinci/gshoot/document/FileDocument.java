package com.vinci.gshoot.index;

import org.apache.lucene.document.Document;

import java.io.File;

public interface FileDocument {
    String FIELD_PATH = "path";
    String FIELD_CONTENT = "contents";
    String FIELD_MODIFIED = "modified";
    String FIELD_SUMMARY = "summary";

    Document toDocument(File file) throws Exception;

    String getContent(File file) throws Exception;
}
