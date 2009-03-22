package com.vinci.gshoot.index;

import com.vinci.gshoot.utils.FileUtils;
import org.apache.log4j.Logger;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.Term;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class IndexService {
    private String indexDir;
    private Logger logger = Logger.getLogger(IndexService.class);

    public IndexService(String indexDir) {
        this.indexDir = indexDir;
        File dir = new File(indexDir);
        if (!dir.exists()) {
            dir.mkdir();
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

            failedFiles = index(files, writer);
            logger.info("Optimizing...");
            try {
                writer.optimize();
            } catch (Exception e) {
                logger.warn("Optimizing indices failed: " + e.getMessage(), e);
            }
        } finally {
            forceClose(writer);
        }

        StringBuilder sb = new StringBuilder("Indexing failed files: \n");
        for (String file : failedFiles.keySet()) {
            sb.append("\t--").append(file).append(": ").append(failedFiles.get(file)).append("\n");
        }
        logger.warn(sb.toString());
    }

    private Map<String, String> index(Collection<String> files, IndexWriter writer) {
        Map<String, String> failedFiles = new LinkedHashMap<String, String>();
        for (String file : files) {
            logger.debug("Index file: " + file);
            try {
                FileDocument document = FileDocumentFactory.getFileDocument(file);
                if (document != null) {
                    writer.addDocument(document.toDocument(new File(file)));
                } else {
                    logger.info("Skip file " + file);
                }
            }
            catch (Exception e) {
                failedFiles.put(file, e.getMessage());
            }
        }
        return failedFiles;
    }

    private void deleteFilesFromIndex(List<String> files) {
        IndexWriter writer = null;
        try {
            writer = createWriter(indexDir, false);
            List<Term> terms = new ArrayList<Term>(files.size());
            for (String file : files) {
                terms.add(new Term(FileDocument.FIELD_PATH, file));
            }
            writer.deleteDocuments(terms.toArray(new Term[terms.size()]));
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
