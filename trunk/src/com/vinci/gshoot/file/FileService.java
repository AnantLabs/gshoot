package com.vinci.gshoot.file;

import com.vinci.gshoot.watchdog.InvalidConfigurationException;
import org.apache.log4j.Logger;

import java.io.File;
import java.util.*;

public class FileService {
    private String watchDir;
    private Logger logger = Logger.getLogger(FileService.class);

    public FileService(String watchDir) {
        File dir = new File(watchDir);
        if (!dir.exists() || !dir.isDirectory()) {
            logger.error("Cannot find directory: " + this.watchDir);
            throw new InvalidConfigurationException("Cannot find directory " + this.watchDir);
        }
        this.watchDir = watchDir;
    }

    public Set<String> getAllFileNames() {
        return this.getAllFiles().keySet();
    }

    public Map<String, FileInfo> getAllFiles() {
        Map<String, FileInfo> files = new LinkedHashMap<String, FileInfo>();
        listFilesRecursively(new File(watchDir), files);
        return files;
    }

    public FileInfo getFile(String fileName) {
        return this.getAllFiles().get(fileName);
    }

    private void listFilesRecursively(File file, Map<String, FileInfo> fileList) {
        if (file.isDirectory()) {
            for (File f : file.listFiles()) {
                listFilesRecursively(f, fileList);
            }
        } else {
            String path = file.getPath();
            if (path.endsWith(".doc") || path.endsWith(".docx") || path.endsWith(".pdf")) {
                fileList.put(file.getPath(), FileInfo.getFileInfo(file));
            }
        }
    }

}
