package com.vinci.gshoot.index;

import org.apache.log4j.Logger;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.Term;

import java.io.File;
import java.io.IOException;
import java.util.*;

import com.vinci.gshoot.utils.FileUtils;

public class IndexService {
    private String indexDir;
    private Logger logger = Logger.getLogger(IndexService.class);

    public IndexService(String indexDir) {
        this.indexDir = indexDir;
        File dir = new File(indexDir);
        if (!dir.exists()) {
            throw new IllegalArgumentException("Directory " + indexDir + " does not exists.");
        }
        if (!dir.isDirectory()) {
            throw new IllegalArgumentException(indexDir + " is not directory.");
        }
        FileUtils.deleteFilesIn(dir);
    }

    public void indexAll(Set<String> files) {
        Date start = new Date();
        logger.info("Indexing files: " + files);
        logger.info("Indexing to directory '" + indexDir + "'...");
        this.indexFiles(files, true);
        logger.info("Indexing finished in " + (System.currentTimeMillis() - start.getTime()) + " milliseconds");
    }

    public void indexNewFiles(List<String> files) {
        indexFiles(files, false);
    }

    public void deleteFiles(List<String> files) {
        deleteFilesFromIndex(files);
    }

    public void updateExistingFiles(List<String> files) {
        logger.debug("Update files: " + Arrays.asList(files));
        this.deleteFilesFromIndex(files);

        this.indexFiles(files, false);
    }

    private void indexFiles(Collection<String> files, boolean createFlag) {
        Map<String, String> failedFiles = new LinkedHashMap<String, String>();
        IndexWriter writer = null;
        try {
            try {
                writer = createWriter(indexDir, createFlag);
            } catch (Exception e) {
                failedFiles.put(files.toString(), e.getMessage());
                logger.warn("Create writer failed while indexing files: " + e.getMessage(), e);
                return;
            }

            index(files, failedFiles, writer);
            logger.info("Optimizing...");
            try {
                writer.optimize();
            } catch (Exception e) {
                logger.warn("Optimizing indices failed: " + e.getMessage(), e);
            }
        } finally {
            forceClose(writer);
        }

        logger.warn("Indexing failed files: ");
        for (String file : failedFiles.keySet()) {
            logger.warn("\t--" + file + ": " + failedFiles.get(file));
        }
    }

    private void index(Collection<String> files, Map<String, String> failedFiles, IndexWriter writer) {
        for (String file : files) {
            logger.debug("Index file: " + file);
            try {
                FileDocument document = FileDocumentFactory.getFileDocument(file);
                writer.addDocument(document.toDocument(new File(file)));
            }
            catch (Exception e) {
                failedFiles.put(file, e.getMessage());
            }
        }
    }

    private void deleteFilesFromIndex(List<String> files) {
        IndexWriter writer = null;
        try {
            writer = createWriter(indexDir, false);
            List<Term> terms = new ArrayList<Term>(files.size());
            for (String file : files) {
                terms.add(new Term(FileDocument.FIELD_PATH, file));
            }
            writer.deleteDocuments(terms.toArray(new Term[0]));
        }
        catch (IOException e) {
            logger.warn("Failed to delete index for updated files: " + e.getMessage(), e);
        }
        finally {
            forceClose(writer);
        }
    }

    private IndexWriter createWriter(String indexdir, boolean createFlag) throws IOException {
        return new IndexWriter(indexdir, new StandardAnalyzer(), createFlag, IndexWriter.MaxFieldLength.LIMITED);
    }

    private void forceClose(IndexWriter writer) {
        try {
            if (writer != null) {
                writer.close();
            }
        } catch (IOException e) {
        }
    }
}
