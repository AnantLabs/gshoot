package com.vinci.gshoot.index;

import org.apache.log4j.Logger;
import org.apache.lucene.document.Document;
import org.apache.pdfbox.exceptions.CryptographyException;
import org.apache.pdfbox.exceptions.InvalidPasswordException;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDDocumentInformation;
import org.apache.pdfbox.util.PDFTextStripper;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;

public class PdfDocument extends AbstractFileDocument {
    private PDFTextStripper stripper = null;

    public PdfDocument() {
    }

    public String getContent(File file) throws Exception {
        PDDocument pdfDocument = null;
        try {
            pdfDocument = PDDocument.load(file);
            return extract(pdfDocument);
        } finally {
            quietlyClose(pdfDocument);
        }
    }

    void addContentToDocument(Document document, File file) throws Exception {
        PDDocument pdfDocument = null;
        try {
            pdfDocument = PDDocument.load(file);

            String contents = extract(pdfDocument);
            StringReader reader = new StringReader(contents);

            // Add contents as Reader-valued Text field, so as to get tokenized and indexed.
            addTextField(document, FIELD_CONTENT, reader);
            addSummaryField(document, contents);

            PDDocumentInformation info = pdfDocument.getDocumentInformation();
            if (info != null) {
                addMoreIndexedFields(document, info);
            }
        } finally {
            quietlyClose(pdfDocument);
        }
    }

    private String extract(PDDocument pdfDocument) throws CryptographyException, IOException, InvalidPasswordException {
        if (pdfDocument.isEncrypted()) {
            pdfDocument.decrypt("");
        }

        StringWriter writer = new StringWriter();
        if (stripper == null) {
            stripper = new PDFTextStripper();
        } else {
            stripper.resetEngine();
        }
        stripper.writeText(pdfDocument, writer);

        String content = writer.getBuffer().toString();
        return content;
    }

    private void addSummaryField(Document document, String contents) {
        int summarySize = Math.min(contents.length(), 500);
        String summary = contents.substring(0, summarySize);
        // Add summary as UnIndexed field, it is stored and returned with hit documents for display.
        addUnindexedField(document, "summary", summary);
    }

    private void addMoreIndexedFields(Document document, PDDocumentInformation info) {
        addTextField(document, "Author", info.getAuthor());
        try {
            addTextField(document, "CreationDate", info.getCreationDate().getTime());
        }
        catch (IOException io) {
        }
        addTextField(document, "Creator", info.getCreator());
        addTextField(document, "Keywords", info.getKeywords());
        try {
            addTextField(document, "ModificationDate", info.getModificationDate().getTime());
        }
        catch (IOException io) {
        }
        addTextField(document, "Producer", info.getProducer());
        addTextField(document, "Subject", info.getSubject());
        addTextField(document, "Title", info.getTitle());
        addTextField(document, "Trapped", info.getTrapped());
    }

    private void quietlyClose(PDDocument pdfDocument) {
        if (pdfDocument != null) {
            try {
                pdfDocument.close();
            } catch (IOException e) {
            }
        }
    }
}
