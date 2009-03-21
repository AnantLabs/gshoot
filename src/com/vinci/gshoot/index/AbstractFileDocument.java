package com.vinci.gshoot.index;

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
    private Logger logger = Logger.getLogger(AbstractFileDocument.class);

    public Document toDocument(File file) {
        Document document = new Document();

        // Use an UnIndexed field, path is stored with the document, but is not searchable.
        addUnindexedField(document, FIELD_PATH, file.getPath());

        // Add the last modified date of the file a field named "modified".  Use a
        // Keyword field, so that it's searchable, but so that no attempt is made
        // to tokenize the field into words.
        addKeywordField(document, FIELD_MODIFIED, timeToString(file.lastModified()));

        try {
            addContentToDocument(document, file);
        } catch (Exception e) {
            logger.warn("Failed to index file " + file.getPath(), e);
        }

        return document;
    }

    abstract void addContentToDocument(Document document, File file) throws Exception;

    protected String timeToString(long time) {
        return DateTools.timeToString(time, dateTimeResolution);
    }

    protected void addKeywordField(Document document, String name, String value) {
        if (value != null) {
            document.add(new Field(name, value, Field.Store.YES, Field.Index.UN_TOKENIZED));
        }
    }

    protected void addTextField(Document document, String name, Reader value) {
        if (value != null) {
            document.add(new Field(name, value));
        }
    }

    protected void addTextField(Document document, String name, String value) {
        if (value != null) {
            document.add(new Field(name, value, Field.Store.YES, Field.Index.TOKENIZED));
        }
    }

    protected void addTextField(Document document, String name, Date value) {
        if (value != null) {
            addTextField(document, name, DateTools.dateToString(value, dateTimeResolution));
        }
    }

    protected static void addUnindexedField(Document document, String name, String value) {
        if (value != null) {
            document.add(new Field(name, value, Field.Store.YES, Field.Index.NO));
        }
    }

    protected void addUnstoredKeywordField(Document document, String name, String value) {
        if (value != null) {
            document.add(new Field(name, value, Field.Store.NO, Field.Index.UN_TOKENIZED));
        }
    }
}
