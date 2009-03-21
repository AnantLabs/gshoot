package com.vinci.gshoot.file;

import java.util.List;
import java.util.ArrayList;

public class FileChangeSet {
    private List<String> newFiles = new ArrayList<String>();
    private List<String> updatedFiles = new ArrayList<String>();
    private List<String> deletedFiles = new ArrayList<String>();

    public List<String> getNewFiles() {
        return newFiles;
    }

    public void addNewFile(String file) {
        this.newFiles.add(file);
    }

    public List<String> getUpdatedFiles() {
        return updatedFiles;
    }

    public void addUpdatedFile(String fileName) {
        this.updatedFiles.add(fileName);
    }

    public void addDeletedFile(String fileName) {
        this.deletedFiles.add(fileName);
    }

    public List<String> getDeletedFiles() {
        return deletedFiles;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder("File changeset: \n");
        sb.append("\tNew files: " + this.newFiles);
        sb.append("\tUpdated files: " + this.updatedFiles);
        sb.append("\tDeleted files: " + this.deletedFiles);

        return sb.toString();
    }

    public boolean anyChanges() {
        return this.newFiles.size() > 0 || this.updatedFiles.size() > 0 || this.deletedFiles.size() > 0;
    }
}
