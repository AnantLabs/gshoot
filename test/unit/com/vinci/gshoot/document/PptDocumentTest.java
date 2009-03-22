package com.vinci.gshoot.document;

import org.junit.Test;
import static org.junit.Assert.assertTrue;

import java.io.File;

public class PptDocumentTest {
    @Test
    public void should_parse_ppt_file() throws Exception {
        String content = new PptDocument().getContent(new File("fixtures/ppt/prototype.ppt"));
        assertTrue(content.contains("流程"));
    }

}
