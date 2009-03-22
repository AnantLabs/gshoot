package com.vinci.gshoot.watchdog;

import com.vinci.gshoot.file.FileChangeSet;
import com.vinci.gshoot.file.FileInfo;
import com.vinci.gshoot.file.FileService;
import com.vinci.gshoot.index.IndexService;
import org.apache.log4j.Logger;

import java.util.Map;

public class WatchDogObserver implements Observer {
    private IndexService indexService;
    private FileService fileService;
    private Logger logger = Logger.getLogger(WatchDogObserver.class);
    private Map<String, FileInfo> currentFiles;

    public WatchDogObserver(IndexService indexService, FileService fileService) {
        this.indexService = indexService;
        this.fileService = fileService;
    }

    public void fired() {
        if (neverIndexedBefore()) {
            indexFirstTime();
        } else {
            diffChangeAndIndex();
        }
    }

    private void diffChangeAndIndex() {
        Map<String, FileInfo> newFiles = this.fileService.getAllFiles();
        FileChangeSet changeSet = diffChange(currentFiles, newFiles);
        if (changeSet.anyChanges()) {
            logger.info(changeSet.toString());
            indexService.indexNewFiles(changeSet.getNewFiles());
            indexService.updateExistingFiles(changeSet.getUpdatedFiles());
            indexService.deleteFiles(changeSet.getDeletedFiles());
        }
        this.currentFiles = newFiles;
    }

    private boolean neverIndexedBefore() {
        return currentFiles == null;
    }

    private void indexFirstTime() {
        currentFiles = fileService.getAllFiles();
        indexService.indexAll(currentFiles.keySet());
    }

    private FileChangeSet diffChange(Map<String, FileInfo> currentFiles, Map<String, FileInfo> newFiles) {
        FileChangeSet changeset = new FileChangeSet();
        for (String newFileName : newFiles.keySet()) {
            if (!currentFiles.containsKey(newFileName)) {
                changeset.addNewFile(newFileName);
            } else {
                if (!currentFiles.get(newFileName).equals(newFiles.get(newFileName))) {
                    changeset.addUpdatedFile(newFileName);
                }
            }
        }
        for (String previousFileName : currentFiles.keySet()) {
            if (!newFiles.containsKey(previousFileName)) {
                changeset.addDeletedFile(previousFileName);
            }
        }
        return changeset;
    }

}
