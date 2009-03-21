package com.vinci.gshoot.search;

import com.vinci.gshoot.file.FileInfo;

import java.util.ArrayList;
import java.util.List;

public class SearchResults {
    private List<FileInfo> results;
    private Pager pager;

    public SearchResults(Pager pager) {
        results = new ArrayList<FileInfo>();
        this.pager = pager;
    }

    public Pager getPager() {
        return pager;
    }

    public List<FileInfo> getResults() {
        return results;
    }

    public void add(FileInfo result) {
        this.results.add(result);
    }
}
