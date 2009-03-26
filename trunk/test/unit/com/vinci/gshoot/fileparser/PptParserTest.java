package com.vinci.gshoot.fileparser;

import static org.junit.Assert.assertTrue;
import org.junit.Test;

import java.io.File;

public class PptParserTest {
    @Test
    public void should_parse_ppt_file() throws Exception {
        String content = new PptParser().parse(new File("fixtures/ppt/prototype.ppt")).getContentAsText();
//        assertTrue(content.contains("劳动力管理业务部"));
    }
}
