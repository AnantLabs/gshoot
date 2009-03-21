package com.vinci.gshoot.search;

public class Pager {
    private int pageIndex;
    private int pagesCount;
    private int hitsPerPage;
    private int numTotalHits;
    private int startPage;
    private static final int SHOW_PAGES = 10;
    private int endPage;

    public Pager(int numTotalHits, int hitsPerPage, int pageIndex) {
        this.numTotalHits = numTotalHits;
        this.hitsPerPage = hitsPerPage;
        this.pagesCount = getTotalPages(numTotalHits, hitsPerPage);
        this.pageIndex = pageIndex;
        if (pagesCount != 0 && this.pageIndex > pagesCount) {
            this.pageIndex = pagesCount;
        }
        calcStartPage();
        calcEndPage();
    }

    public int getNumTotalHits() {
        return numTotalHits;
    }

    public int getPagesCount() {
        return pagesCount;
    }

    public int getPageIndex() {
        return pageIndex;
    }

    public int getHitsPerPage() {
        return hitsPerPage;
    }


    private int getTotalPages(int numTotalHits, int hitsPerPage) {
        int pages = numTotalHits / hitsPerPage;
        if (numTotalHits % hitsPerPage != 0) {
            pages++;
        }
        return pages;
    }

    public int getStartPage() {
        return this.startPage;
    }

    public int getEndPage() {
        return this.endPage;
    }

    public int getStartRecord() {
        return numTotalHits == 0 ? 0 : (this.pageIndex - 1) * this.hitsPerPage + 1;
    }

    public int getEndRecord() {
        return Math.min(this.pageIndex * this.hitsPerPage, this.numTotalHits);
    }

    private void calcStartPage() {
        if (pageIndex <= SHOW_PAGES / 2) {
            this.startPage = 1;
        } else if (pageIndex >= (pagesCount - SHOW_PAGES)) {
            this.startPage = pagesCount - SHOW_PAGES + 1;
        } else {
            this.startPage = pageIndex - SHOW_PAGES / 2;
        }
    }

    private void calcEndPage() {
        this.endPage = Math.min(this.startPage + SHOW_PAGES - 1, this.pagesCount);
    }
}
