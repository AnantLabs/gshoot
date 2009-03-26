package com.vinci.gshoot.fileparser;

import static org.junit.Assert.assertTrue;
import org.junit.Test;

import java.io.File;

public class WordParserTest {
    @Test
    public void should_load_file_as_Parser() throws Exception {
        System.out.println(new File(".").getAbsolutePath());
        File f = new File("fixtures/word/laborlaw.doc");
        String content = new WordParser().parse(f).getContentAsText();
        assertTrue(content.contains("合同"));
    }
}
