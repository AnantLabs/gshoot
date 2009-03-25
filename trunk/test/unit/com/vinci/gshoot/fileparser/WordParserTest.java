package com.vinci.gshoot.fileparser;

import static org.junit.Assert.assertTrue;
import org.junit.Test;

import java.io.File;

public class WordParserTest {
    @Test
    public void should_load_file_as_Parser() throws Exception {
        File f = new File("fixtures/word/laborlaw.doc");
        String content = new WordParser().parse(f).getTextContent();
        assertTrue(content.contains("合同"));
    }
}
