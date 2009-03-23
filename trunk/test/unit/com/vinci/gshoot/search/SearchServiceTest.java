package com.vinci.gshoot.search;

import com.vinci.gshoot.index.IndexService;
import com.vinci.gshoot.utils.FileUtils;
import org.apache.lucene.queryParser.ParseException;
import org.junit.After;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

public class SearchServiceTest {
    private String indexDir = "./indices";
    private String fileDir = "fixtures/fileDir";
    private IndexService indexService;

    @Before
    public void before() throws IOException {
        FileUtils.deleteIfExist(new File(indexDir));
        FileUtils.deleteFilesIn(new File(fileDir));
        FileUtils.createFile(fileDir, "file0.txt", "This is an existing file\nThis is the second line.");
        createFiles(new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10});

        indexService = new IndexService(indexDir, fileDir);
        indexService.indexAll();
    }

    private void createFiles(int... index) throws IOException {
        for (int i : index) {
            String name = fileDir + File.separator + "file" + i + ".txt";
            FileUtils.createFile(name, "This is a file " + i + ".");
        }
    }

    @After
    public void after() {
        FileUtils.deleteFilesIn(new File(fileDir));
    }

    @Test
    public void should_return_first_page_results() throws IOException, ParseException, SearchException {
        int hitsPerPage = 15;
        int pageIndex = 1;
        SearchService searchService = new SearchService(indexDir, hitsPerPage);

        SearchResults results = searchService.search("existing", pageIndex);
        assertEquals(1, results.getPager().getNumTotalHits());
        assertEquals(1, results.getPager().getPagesCount());
        assertEquals(pageIndex, results.getPager().getPageIndex());
        assertEquals(hitsPerPage, results.getPager().getHitsPerPage());
        assertEquals("fixtures/fileDir/file0.txt", results.getResults().get(0).getPath());
    }

    @Test
    public void should_return_last_page_if_page_index_is_too_large() throws SearchException {
        int hitsPerPage = 1;
        int pageIndex = 100;
        SearchService searchService = new SearchService(indexDir, hitsPerPage);

        SearchResults results = searchService.search("existing", pageIndex);
        assertEquals(1, results.getPager().getNumTotalHits());
        assertEquals(1, results.getPager().getPagesCount());
        assertEquals(1, results.getPager().getPageIndex());
        assertEquals(hitsPerPage, results.getPager().getHitsPerPage());
        assertEquals(1, results.getResults().size());
    }

    @Test
    public void should_return_results_for_not_first_page_index() throws SearchException {

        int hitsPerPage = 1;
        int pageIndex = 6;
        SearchService searchService = new SearchService(indexDir, hitsPerPage);
        SearchResults results = searchService.search("file", pageIndex);
        assertEquals(11, results.getPager().getNumTotalHits());
        assertEquals(11, results.getPager().getPagesCount());
        assertEquals(pageIndex, results.getPager().getPageIndex());
        assertEquals(hitsPerPage, results.getPager().getHitsPerPage());
        assertEquals("fixtures/fileDir/file5.txt", results.getResults().get(0).getPath());
        assertEquals("file5", results.getResults().get(0).getName());
    }
}
