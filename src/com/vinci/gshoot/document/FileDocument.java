package com.vinci.gshoot.document;

import org.apache.lucene.document.Document;

import java.io.File;

public interface FileDocument {
    String FIELD_PATH = "path";
    String FIELD_CONTENT = "contents";
    String FIELD_MODIFIED = "modified";
    String FIELD_SUMMARY = "summary";
    String FIELD_UID = "uid";
    String FIELD_TITLE = "title";

    Document toDocument(File file) throws Exception;

    String getContent(File file) throws Exception;
}
