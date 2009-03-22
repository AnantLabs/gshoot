package com.vinci.gshoot.document;

import org.apache.commons.io.IOUtils;
import org.apache.lucene.demo.html.HTMLParser;
import org.apache.lucene.document.Document;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;

public class HtmlDocument extends AbstractFileDocument {
    static char dirSep = System.getProperty("file.separator").charAt(0);

    public String getContent(File file) throws Exception {
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(file);
            HTMLParser parser = new HTMLParser(fis);
            BufferedReader reader = new BufferedReader(parser.getReader());
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line).append("\n");
            }
            return sb.toString();
        } finally {
            IOUtils.closeQuietly(fis);
        }
    }

    void addContentToDocument(Document document, File file) throws Exception {
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(file);
            HTMLParser parser = new HTMLParser(fis);

            // Add the tag-stripped contents as a Reader-valued Text field so it will
            // get tokenized and indexed.
            addTextField(document, FIELD_CONTENT, parser.getReader());

            // Add the summary as a field that is stored and returned with
            // hit documents for display.
            addKeywordField(document, FIELD_SUMMARY, parser.getSummary());

            // Add the title as a field that it can be searched and that is stored.
            addTextField(document, FIELD_TITLE, parser.getTitle());

        } finally {
            IOUtils.closeQuietly(fis);
        }
    }

    public static String uid2url(String uid) {
        String url = uid.replace('\u0000', '/');      // replace nulls with slashes
        return url.substring(0, url.lastIndexOf('/')); // remove date from end
    }
}
