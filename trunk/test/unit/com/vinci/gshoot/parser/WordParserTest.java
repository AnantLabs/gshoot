package com.vinci.gshoot.parser;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.junit.Assert.assertNotNull;

public class WordParserTest {
    @Test
    public void should_parse_word_file() {
        try {
            assertNotNull(new WordParser().parse("fixtures/word/laborlaw.doc"));
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }
}