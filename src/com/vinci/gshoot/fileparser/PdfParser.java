package com.vinci.gshoot.fileparser;

import org.apache.pdfbox.exceptions.CryptographyException;
import org.apache.pdfbox.exceptions.InvalidPasswordException;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDDocumentInformation;
import org.apache.pdfbox.util.PDFTextStripper;

import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;

public class PdfParser implements Parser {
    private PDFTextStripper stripper = null;
    private String content;
    private String summary;

    private PDDocumentInformation info;

    public Parser parse(File file) throws Exception {
        PDDocument pdfDocument = null;
        try {
            pdfDocument = PDDocument.load(file);

            this.content = extract(pdfDocument);

            this.summary = content.substring(0, Math.min(content.length(), SUMMARY_LENGTH));

            this.info = pdfDocument.getDocumentInformation();
        } finally {
            closeQuietly(pdfDocument);
        }
        return this;
    }

    public Reader getContent() {
        return new StringReader(this.content);
    }

    public String getTextContent() {
        return this.content;
    }

    public String getTitle() {
        return this.info.getTitle();
    }

    public String getSummary() {
        return this.summary;
    }

    private String extract(PDDocument pdfDocument) throws CryptographyException, IOException, InvalidPasswordException {
        if (pdfDocument.isEncrypted()) {
            pdfDocument.decrypt("");
        }

        StringWriter writer = null;
        try {
            writer = new StringWriter();
            if (stripper == null) {
                stripper = new PDFTextStripper();
            } else {
                stripper.resetEngine();
            }
            stripper.writeText(pdfDocument, writer);

            return writer.getBuffer().toString();
        } finally {
            if (writer != null) {
                writer.close();
            }
        }
    }

    private void closeQuietly(PDDocument pdfDocument) {
        if (pdfDocument != null) {
            try {
                pdfDocument.close();
            } catch (IOException e) {
            }
        }
    }
}
