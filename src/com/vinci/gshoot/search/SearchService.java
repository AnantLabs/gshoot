package com.vinci.gshoot.search;

import com.vinci.gshoot.document.FileDocument;
import com.vinci.gshoot.file.FileInfo;
import com.vinci.gshoot.fileparser.ParserFactory;
import org.apache.log4j.Logger;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.Searcher;
import org.apache.lucene.search.TopDocCollector;
import org.apache.lucene.search.highlight.Fragmenter;
import org.apache.lucene.search.highlight.Highlighter;
import org.apache.lucene.search.highlight.QueryScorer;
import org.apache.lucene.search.highlight.SimpleFragmenter;
import org.apache.lucene.search.highlight.SimpleHTMLFormatter;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;

public class SearchService {
    private String indexDir;
    private int hitsPerPage;
    private String prefixHTML = "<em>";
    private String suffixHTML = "</em>";
    private Logger logger = Logger.getLogger(SearchService.class);

    public SearchService(String indexDir, int hitsPerPage) {
        this.indexDir = indexDir;
        if (hitsPerPage < 1) {
            throw new IllegalArgumentException("Hits per page cannot be less than 1.");
        }
        this.hitsPerPage = hitsPerPage;
    }

    public SearchResults search(String keywords, int pageIndex) throws SearchException {
        if (pageIndex < 1) {
            pageIndex = 1;
        }
        IndexReader reader = null;
        try {
            QueryParser parser = new QueryParser(FileDocument.FIELD_CONTENT, getAnalyzer());

            Query query = parser.parse(keywords);

            reader = IndexReader.open(indexDir);
            return doPagingSearch(query, pageIndex, reader);
        } catch (Exception e) {
            throw new SearchException(e.getMessage(), e);
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                }
            }
        }
    }

    private SearchResults doPagingSearch(Query query, int pageIndex, IndexReader indexReader) throws IOException {
        TopDocCollector collector = new TopDocCollector(pageIndex * hitsPerPage);
        IndexSearcher searcher = new IndexSearcher(indexReader);
        searcher.search(query, collector);
        ScoreDoc[] hits = collector.topDocs().scoreDocs;

        int numTotalHits = collector.getTotalHits();
        logger.debug("Return " + hits.length + " of " + numTotalHits + " total matching document.");

        return extractResult(searcher, hits, new Pager(numTotalHits, hitsPerPage, pageIndex), query);
    }

    private SearchResults extractResult(Searcher searcher, ScoreDoc[] hits, Pager pager, Query query) throws IOException {
        int start = pager.getStartRecord();
        int end = pager.getEndRecord();
        logger.debug("Get records[" + start + ", " + end + "]");
        SearchResults results = new SearchResults(pager);
        if (hits.length > 0) {
            for (int i = start - 1; i < end; i++) {
                Document doc = searcher.doc(hits[i].doc);
                String path = doc.get(FileDocument.FIELD_PATH);
                FileInfo info = FileInfo.getFileInfo(path);
                try {
                    info.setScore(hits[i].score);
                    info.setDigest(getDigest(query, doc));
                }
                catch (Exception e) {
                    info.setDigest("无法得到该文件的摘要内容: " + e.getMessage());
                    logger.warn("Failed to get digest for file " + path, e);
                }
                results.add(info);
            }
        }
        return results;
    }

    private String getDigest(Query query, Document doc) throws Exception {
        String path = doc.get(FileDocument.FIELD_PATH);

        SimpleHTMLFormatter formatter = new SimpleHTMLFormatter(prefixHTML, suffixHTML);
        QueryScorer scorer = new QueryScorer(query);
        Highlighter highlighter = new Highlighter(formatter, scorer);

        Fragmenter fragmenter = new SimpleFragmenter(200);
        highlighter.setTextFragmenter(fragmenter);

        String content = ParserFactory.getParser(path).parse(new File(path)).getContentAsText();
        TokenStream tokenStream = getAnalyzer().tokenStream(FileDocument.FIELD_CONTENT, new StringReader(content));

        return highlighter.getBestFragment(tokenStream, content) + " ...";
    }

    private StandardAnalyzer getAnalyzer() {
        return new StandardAnalyzer();
    }
}
