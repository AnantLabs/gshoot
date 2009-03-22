package com.vinci.gshoot.document;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.DateTools;
import org.apache.velocity.app.VelocityEngine;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.Reader;
import java.util.Date;

public abstract class AbstractFileDocument implements FileDocument {

    private DateTools.Resolution dateTimeResolution = DateTools.Resolution.SECOND;
    private static final char FILE_SEPARATOR = System.getProperty("file.separator").charAt(0);
    protected static final int SUMMARY_LENGTH = 200;

    public Document toDocument(File file) throws Exception {
        Document document = new Document();

        // Use an UnIndexed field, path is stored with the document, but is not searchable.
        addUnindexedField(document, FIELD_PATH, file.getPath());

        // Add the last modified date of the file a field named "modified".  Use a
        // Keyword field, so that it's searchable, but so that no attempt is made
        // to tokenize the field into words.
        addKeywordField(document, FIELD_MODIFIED, timeToString(file.lastModified()));

        // Add the uid as a field, so that index can be incrementally maintained.
        // This field is not stored with document, it is indexed, but it is not
        // tokenized prior to indexing.
        addUnstoredKeywordField(document, FIELD_UID, uid(file));

        addContentToDocument(document, file);

        return document;
    }

    private String uid(File file) {
        return file.getPath().replace(FILE_SEPARATOR, '\u0000') + "\u0000" + timeToString(file.lastModified());
    }

    abstract void addContentToDocument(Document document, File file) throws Exception;

    protected String timeToString(long time) {
        return DateTools.timeToString(time, dateTimeResolution);
    }

    protected void addTextField(Document document, String name, Reader value) {
        if (value != null) {
            document.add(new Field(name, value));
        }
    }

    protected void addTextField(Document document, String name, Date value) {
        if (value != null) {
            addTextField(document, name, DateTools.dateToString(value, dateTimeResolution));
        }
    }

    protected void addTextField(Document document, String name, String value) {
        if (value != null) {
            document.add(new Field(name, value, Field.Store.YES, Field.Index.ANALYZED));
        }
    }

    protected static void addUnindexedField(Document document, String name, String value) {
        if (value != null) {
            document.add(new Field(name, value, Field.Store.YES, Field.Index.NOT_ANALYZED));
        }
    }

    protected void addKeywordField(Document document, String name, String value) {
        if (value != null) {
            document.add(new Field(name, value, Field.Store.YES, Field.Index.NOT_ANALYZED));
        }
    }

    protected void addUnstoredKeywordField(Document document, String name, String value) {
        if (value != null) {
            document.add(new Field(name, value, Field.Store.NO, Field.Index.NOT_ANALYZED));
        }
    }
}
