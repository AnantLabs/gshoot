package com.vinci.gshoot.index;

import org.apache.lucene.document.Document;

import java.io.File;

public interface FileDocument {
    public String FIELD_PATH = "path";
    public String FIELD_CONTENT = "contents";
    String FIELD_MODIFIED = "modified";

    Document toDocument(File f);
}
