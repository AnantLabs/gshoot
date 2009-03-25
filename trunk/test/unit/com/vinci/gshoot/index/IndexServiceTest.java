package com.vinci.gshoot.index;

import com.vinci.gshoot.utils.FileUtils;
import org.junit.After;
import static org.junit.Assert.assertEquals;
import org.junit.Before;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class IndexServiceTest {

    private String indexDir = "./indices";
    private String fileDir = "fixtures/fileDir";
    private Set files = new LinkedHashSet();
    private IndexService indexService;


    @Before
    public void before() throws IOException {
        removeIndexDir(indexDir);
        checkFileDirExistsAndEmpty(fileDir);
//        files.add(createFile("existingFile.txt", "This is an existing file"));
//        files.add(createFile("newFile.txt", "This is a new file"));
//        indexService = new IndexService(indexDir);
//        indexService.indexAll(files);
    }

    private void removeIndexDir(String indexDir) {
        FileUtils.deleteIfExist(new File(indexDir));
    }

    private void checkFileDirExistsAndEmpty(String fileDir) {
        FileUtils.deleteIfExist(new File(fileDir));
        new File(fileDir).mkdir();
    }

    @After
    public void after() {
       // FileUtils.deleteIfExist(new File(fileDir));
    }
//
//    @Test
//    public void should_index_all_files_of_directory() throws IOException, ParseException {
//        assertEquals(1, getMatchedCount(indexDir, "existing"));
//    }
//
//    @Test
//    public void should_index_a_new_file() throws IOException, ParseException {
//        indexService.indexNewFiles(Arrays.asList(fileDir + File.separator + "newFile.txt"));
//        assertEquals(1, getMatchedCount(indexDir, "existing"));
//        assertEquals(3, getMatchedCount(indexDir, "file"));
//    }
//
//    @Test
//    public void should_update_an_existing_file() throws IOException, ParseException {
//        String fileName = fileDir + File.separator + "existingFile.txt";
//        FileUtils.updateFileContent(fileName, "This is an updated file");
//        new IndexService(indexDir).updateExistingFiles(Arrays.asList(fileName));
//        assertEquals(2, getMatchedCount(indexDir, "file"));
//        assertEquals(1, getMatchedCount(indexDir, "updated"));
//        assertEquals(0, getMatchedCount(indexDir, "existing"));
//    }
//
//    @Test
//    public void should_delete_an_existing_file() throws IOException, ParseException {
//        File existingFile = new File(fileDir + File.separator + "existingFile.txt");
//        FileUtils.deleteIfExist(existingFile);
//        new IndexService(indexDir).deleteFiles(Arrays.asList(existingFile.getPath()));
//        assertEquals(0, getMatchedCount(indexDir, "existingFile"));
//    }
//
//    private String createFile(String fileName, String content) throws IOException {
//        String file = fileDir + File.separator + fileName;
//        FileUtils.createFile(file, content);
//        return file;
//    }
//
//    private int getMatchedCount(String indexDir, String queryString) throws IOException, ParseException {
//        IndexReader reader = IndexReader.open(indexDir);
//        try {
//            Searcher searcher = new IndexSearcher(reader);
//            Analyzer analyzer = new StandardAnalyzer();
//
//            QueryParser parser = new QueryParser(FileDocument.FIELD_CONTENT, analyzer);
//
//            Query query = parser.parse(queryString);
//
//            TopDocs topDocs = searcher.search(query, null, 100);
//            for (ScoreDoc scoreDoc : topDocs.scoreDocs) {
//                System.out.println("Query string " + queryString + " in doc: " + scoreDoc.doc);
//            }
//
//            return topDocs.scoreDocs.length;
//        } finally {
//            reader.close();
//        }
//    }
}
