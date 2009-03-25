package com.vinci.gshoot.parser;

import org.junit.Test;
import static org.junit.Assert.assertTrue;

import java.io.File;

public class ExcelParserTestTest {
    @Test
    public void should_should_parse_excel_file() throws Exception {
        String content = new ExcelParser().parse(new File("fixtures/excel/courses.xls")).getTextContent();

        assertTrue(content.contains("装饰"));
        assertTrue(content.contains("班级"));
    }
}
