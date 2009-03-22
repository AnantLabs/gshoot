package com.vinci.gshoot.files;

import com.vinci.gshoot.file.FileInfo;
import com.vinci.gshoot.file.FileType;
import static org.junit.Assert.*;
import org.junit.Test;

public class FileInfoTest {
    @Test
    public void should_return_file_info_for_absolute_path_file() {
        FileInfo info = FileInfo.getFileInfo("/fixtures/file1.txt");
        assertEquals("file1", info.getName());
        assertEquals("/fixtures/file1.txt", info.getPath());
        assertEquals("txt", info.getExtension());
        assertEquals(FileType.FILE_TXT, info.getType());
    }

    @Test
    public void should_return_file_info_for_relative_path_file() {
        FileInfo info = FileInfo.getFileInfo("file1.txt");
        assertEquals("file1", info.getName());
        assertEquals("file1.txt", info.getPath());
        assertEquals(FileType.FILE_TXT, info.getType());
    }

    @Test
    public void should_return_file_info_for_root_path_file() {
        FileInfo info = FileInfo.getFileInfo("/file1.txt");
        assertEquals("file1", info.getName());
        assertEquals("/file1.txt", info.getPath());
        assertEquals(FileType.FILE_TXT, info.getType());
    }

    @Test
    public void should_return_file_info_for_file_with_no_extension() {
        FileInfo info = FileInfo.getFileInfo("/file1");
        assertEquals("file1", info.getName());
        assertEquals("/file1", info.getPath());
        assertEquals(FileType.FILE_UNKNOWN, info.getType());
    }

    @Test
    public void should_return_file_info_for_file_with_dot_ended() {
        FileInfo info = FileInfo.getFileInfo("/file1.");
        assertEquals("file1", info.getName());
        assertEquals("/file1.", info.getPath());
        assertEquals(FileType.FILE_UNKNOWN, info.getType());
    }
}
