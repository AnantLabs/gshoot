package com.vinci.gshoot.index;

import com.vinci.gshoot.document.FileDocument;
import com.vinci.gshoot.fileparser.Parser;
import com.vinci.gshoot.fileparser.ParserFactory;
import org.apache.log4j.Logger;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.DateTools;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.Term;
import org.apache.lucene.index.TermEnum;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class IndexService {
    private String indexDir;
    private String fileDir;
    private boolean indexing = false;
    private Set<String> failedFileUids = new LinkedHashSet<String>();
    private static final char FILE_SEPARATOR = System.getProperty("file.separator").charAt(0);
    private DateTools.Resolution dateTimeResolution = DateTools.Resolution.SECOND;
    private Logger logger = Logger.getLogger(IndexService.class);

    public IndexService(String indexDir, String fileDir) {
        this.indexDir = indexDir;
        this.fileDir = fileDir;
        logger.info("Indexing directory is set to: " + indexDir + "'");
    }

    public void indexAll() {
        if (!checkIndex(this.indexDir)) {
            return;
        }
        if (indexing) {
            logger.warn("Indexing is already in process.");
            return;
        }
        indexing = true;

        logger.debug("Try to update index...");
        Set<String> newUids = new LinkedHashSet<String>();
        listAvailableUids(new File(fileDir), newUids);
        Set<String> oldUids = null;
        try {
            oldUids = getOldUids(this.indexDir);
        } catch (IOException e) {
            logger.warn("Failed to list old uids while indexing: " + e.getMessage(), e);
        }
        if (newUids.size() == 0 && oldUids.size() == 0) {
            logger.debug("Nothing to update.");
        } else {
            updateIndex(newUids, oldUids);
        }
        indexing = false;
    }

    private void updateIndex(Set<String> newUids, Set<String> oldUids) {
        LinkedHashSet<String> newFilePaths = new LinkedHashSet<String>();
        LinkedHashSet<String> obsoleteUids = new LinkedHashSet<String>();
        diffChanges(oldUids, newUids, obsoleteUids, newFilePaths);

        if (obsoleteUids.size() > 0) {
            try {
                logDeleted(obsoleteUids);
                delete(indexDir, obsoleteUids);
                logger.info("Delete done!");
            } catch (IOException e) {
                logger.warn("Delete failed: " + e.getMessage(), e);
            }
        }
        if (newFilePaths.size() > 0) {
            try {
                logger.info("Try to add new files: \n" + newFilePaths);
                Map<String, String> failedFiles = add(indexDir, newFilePaths);
                if (failedFiles.size() > 0) {
                    logFailedFiles(failedFiles);
                }
            } catch (IOException e) {
                logger.info("Add new files failed: " + e.getMessage(), e);
            }
        }
    }

    private void logFailedFiles(Map<String, String> failedFiles) {
        StringBuilder sb = new StringBuilder("Failed to index these files: \n");
        for (String file : failedFiles.keySet()) {
            sb.append("\t--").append(file).append(": ").append(failedFiles.get(file)).append("\n");
        }
        logger.warn(sb.toString());
    }

    private Map<String, String> add(String indexDir, LinkedHashSet<String> newFilePaths) throws IOException {
        Map<String, String> failedFiles = new LinkedHashMap<String, String>();
        IndexWriter writer = null;

        try {
            writer = createWriter(indexDir, false);
            for (String filePath : newFilePaths) {
                logger.debug("Index file: " + filePath);
                try {
                    Parser parser = ParserFactory.getParser(filePath);
                    if (parser != null) {
                        FileDocument document = new FileDocument(parser);
                        writer.addDocument(document.toDocument(new File(filePath)));
                    }
                    logger.info("Skip file: " + filePath);
                }
                catch (Exception e) {
                    failedFiles.put(filePath, e.getMessage() == null ? "Unknow reason" : e.getMessage());
                    failedFileUids.add(toUid(new File(filePath)));
                }
            }
        } finally {
            closeQuietly(writer);
        }
        return failedFiles;
    }

    private void delete(String indexDir, LinkedHashSet<String> deleted) throws IOException {
        IndexWriter writer = null;
        try {
            writer = createWriter(indexDir, false);
            List<Term> terms = new ArrayList<Term>(deleted.size());
            for (String del : deleted) {
                terms.add(new Term(FileDocument.FIELD_UID, del));
            }
            writer.deleteDocuments(terms.toArray(new Term[terms.size()]));
        }
        finally {
            closeQuietly(writer);
        }
    }

    private void logDeleted(LinkedHashSet<String> deleted) {
        Set<String> deletedPaths = new LinkedHashSet<String>();
        for (String del : deleted) {
            deletedPaths.add(toPath(del));
        }
        logger.info("Try to delete obsolete files: \n" + deletedPaths);
    }

    private void diffChanges(Set<String> oldUids, Set<String> newUids, Set<String> deleted, Set<String> added) {
        for (String oldUid : oldUids) {
            if (!newUids.contains(oldUid)) {
                deleted.add(oldUid);
            }
        }
        for (String newUid : newUids) {
            if (!oldUids.contains(newUid)) {
                added.add(toPath(newUid));
            }
        }
    }

    private Set<String> getOldUids(String indexDir) throws IOException {
        Set<String> oldUids = new LinkedHashSet<String>();
        IndexReader reader = null;
        try {
            reader = IndexReader.open(indexDir);
            TermEnum uidIter = reader.terms(new Term(FileDocument.FIELD_UID, ""));
            while (uidIter.term() != null && uidIter.term().field().equals("uid")) {
                oldUids.add(uidIter.term().text());
                uidIter.next();
            }
        } finally {
            reader.close();
        }
        return oldUids;
    }

    private void listAvailableUids(File file, Set<String> uids) {
        if (file.isDirectory()) {
            for (File f : file.listFiles()) {
                listAvailableUids(f, uids);
            }
        } else {
            String path = file.getPath();
            if (isValidFile(path)) {
                String uid = toUid(file);
                if (!failedFileUids.contains(uid)) {
                    uids.add(uid);
                }
            }
        }
    }

    private String toUid(File file) {
        return file.getPath().replace(FILE_SEPARATOR, '\u0000') + "\u0000" + DateTools.timeToString(file.lastModified(), dateTimeResolution);
    }

    private String toPath(String uid) {
        String url = uid.replace('\u0000', FILE_SEPARATOR);      // replace nulls with slashes
        return url.substring(0, url.lastIndexOf(FILE_SEPARATOR)); // remove date from end
    }

    private IndexWriter createWriter(String indexdir, boolean createFlag) throws IOException {
        return new IndexWriter(indexdir, new StandardAnalyzer(), createFlag, IndexWriter.MaxFieldLength.LIMITED);
    }

    private boolean checkIndex(String indexDir) {
        File dir = new File(indexDir);
        if (!dir.exists()) {
            dir.mkdir();
        }
        if (!indexIsCreated(indexDir)) {
            return createIndex(indexDir);
        }
        return true;
    }

    private boolean indexIsCreated(String indexDir) {
        try {
            IndexReader.open(indexDir);
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    private boolean createIndex(String indexDir) {
        IndexWriter writer = null;
        try {
            logger.warn("Create index writer on directory " + indexDir);
            writer = new IndexWriter(indexDir, new StandardAnalyzer(), true, IndexWriter.MaxFieldLength.LIMITED);
            return true;
        } catch (IOException e) {
            logger.warn("Create index writer failed: " + e.getMessage(), e);
        } finally {
            closeQuietly(writer);
        }
        return false;
    }

    private void closeQuietly(IndexWriter writer) {
        if (writer != null) {
            try {
                writer.close();
            } catch (IOException e) {
            }
        }
    }

    private boolean isValidFile(String path) {
        if (path.endsWith(".xls")) return true;
        if (path.endsWith(".doc")) return true;
        if (path.endsWith(".docs")) return true;
        if (path.endsWith(".pdf")) return true;
        if (path.endsWith(".ppt")) return true;
//        if (toPath.endsWith(".htm")) return true;
//        if (toPath.endsWith(".html")) return true;
        return false;
    }
}
