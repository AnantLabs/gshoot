package com.vinci.gshoot.index;

import org.junit.Test;import static org.junit.Assert.assertTrue;

import java.io.File;

public class ExcelDocumentTestTest {
     @Test
     public void should_should_parse_excel_file() throws Exception {
         String content = new ExcelDocument().getContent(new File("fixtures/excel/courses.xls"));
         assertTrue(content.contains("装饰"));
         assertTrue(content.contains("班级"));
     }
}
