package com.vinci.gshoot.document;

import org.apache.lucene.document.Document;
import org.apache.poi.hslf.HSLFSlideShow;
import org.apache.poi.hslf.extractor.PowerPointExtractor;
import org.apache.poi.hslf.model.Slide;
import org.apache.poi.hslf.model.TextRun;
import org.apache.poi.hslf.usermodel.SlideShow;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;

public class PptDocument extends AbstractFileDocument {

    void addContentToDocument(Document document, File file) throws Exception {
        String contents = getContent(file);

        // Add contents as Reader-valued Text field, so as to get tokenized and indexed.
        addTextField(document, FIELD_CONTENT, new StringReader(contents));

        // Add summary as UnIndexed field, it is stored and returned with hit documents for display.
        addUnindexedField(document, FIELD_SUMMARY, contents.substring(0, Math.min(contents.length(), SUMMARY_LENGTH)));

        addTextField(document, FIELD_TITLE, file.getName());
    }

    public String getContent(File file) throws Exception {
        return extract(file.getPath());
    }

    private String extract(String filePath) throws IOException {
        StringBuilder sb = new StringBuilder("");
        PowerPointExtractor extractor = new PowerPointExtractor(filePath);
        return extractor.getText();
//        HSLFSlideShow show = new HSLFSlideShow(filePath);
//        SlideShow ss = new SlideShow(show);
//        Slide[] slides = ss.getSlides();
//        for (int i = 0; i < slides.length; i++) {
//            sb.append(extractSlide(slides[i]));
//            sb.append("\n");
//        }
//        return sb.toString();
    }

    private String extractSlide(Slide slide) {
        StringBuilder sb = new StringBuilder("");
        TextRun[] t = slide.getTextRuns();
        for (int j = 0; j < t.length; j++) {
            sb.append(t[j].getText());
        }
        return sb.toString();
    }
}
