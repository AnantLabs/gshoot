package com.vinci.gshoot.files;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

import java.util.Arrays;

import com.vinci.gshoot.file.FileChangeSet;

public class FileChangeSetTest {
    @Test
    public void should_be_able_to_add_new_files() {
        FileChangeSet changeset = new FileChangeSet();
        String file1 = "fixtures/tmp/newFile1.doc";
        changeset.addNewFile(file1);
        String file2 = "fixtures/tmp/newFile2.doc";
        changeset.addNewFile(file2);
        assertEquals(Arrays.asList(file1, file2), changeset.getNewFiles());
    }

    @Test
    public void should_be_able_to_add_updated_files() {
        FileChangeSet changeset = new FileChangeSet();
        String file1 = "fixtures/tmp/updateFile1.doc";
        changeset.addUpdatedFile(file1);
        String file2 = "fixtures/tmp/updateFile2.doc";
        changeset.addUpdatedFile(file2);
        assertEquals(Arrays.asList(file1, file2), changeset.getUpdatedFiles());
    }

    @Test
    public void should_be_able_to_add_deleted_files() {
        FileChangeSet changeset = new FileChangeSet();
        String file1 = "fixtures/tmp/deletedFile1.doc";
        changeset.addDeletedFile(file1);
        String file2 = "fixtures/tmp/deletedFile2.doc";
        changeset.addDeletedFile(file2);
        assertEquals(Arrays.asList(file1, file2), changeset.getDeletedFiles());
    }
}
