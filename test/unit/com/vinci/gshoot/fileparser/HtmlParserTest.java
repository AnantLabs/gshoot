package com.vinci.gshoot.fileparser;

import static org.junit.Assert.assertTrue;
import org.junit.Test;

import java.io.File;

public class HtmlParserTest {
    @Test
    public void should_parse_content_of_html() throws Exception {
        File f = new File("fixtures/html/jquery.html");
        String content = new HtmlParser().parse(f).getContentAsText();
//        assertTrue(content.contains("教程"));
    }
}