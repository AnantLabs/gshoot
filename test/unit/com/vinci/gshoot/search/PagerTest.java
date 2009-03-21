package com.vinci.gshoot.search;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class PagerTest {
    @Test
    public void should_return_10_pages_from_1_to_10() {
        Pager pager = new Pager(100, 10, 1);
        assertEquals(100, pager.getNumTotalHits());
        assertEquals(10, pager.getHitsPerPage());
        assertEquals(1, pager.getPageIndex());
        assertEquals(10, pager.getPagesCount());
        assertEquals(1, pager.getStartPage());
        assertEquals(10, pager.getEndPage());
        assertEquals(1, pager.getStartRecord());
        assertEquals(10, pager.getEndRecord());
    }

    @Test
    public void should_return_12_pages_from_1_to_10() {
        Pager pager = new Pager(100, 9, 1);
        assertEquals(100, pager.getNumTotalHits());
        assertEquals(9, pager.getHitsPerPage());
        assertEquals(1, pager.getPageIndex());
        assertEquals(12, pager.getPagesCount());
        assertEquals(1, pager.getStartPage());
        assertEquals(10, pager.getEndPage());
        assertEquals(1, pager.getStartRecord());
        assertEquals(9, pager.getEndRecord());
    }

    @Test
    public void should_return_another_10_pages_from_1_to_10() {
        Pager pager = new Pager(100, 10, 10);
        assertEquals(100, pager.getNumTotalHits());
        assertEquals(10, pager.getHitsPerPage());
        assertEquals(10, pager.getPageIndex());
        assertEquals(10, pager.getPagesCount());
        assertEquals(1, pager.getStartPage());
        assertEquals(10, pager.getEndPage());
        assertEquals(91, pager.getStartRecord());
        assertEquals(100, pager.getEndRecord());
    }

    @Test
    public void should_return_another_12_pages_from_1_to_10() {
        Pager pager = new Pager(100, 9, 6);
        assertEquals(100, pager.getNumTotalHits());
        assertEquals(9, pager.getHitsPerPage());
        assertEquals(6, pager.getPageIndex());
        assertEquals(12, pager.getPagesCount());
        assertEquals(3, pager.getStartPage());
        assertEquals(12, pager.getEndPage());
        assertEquals(46, pager.getStartRecord());
        assertEquals(54, pager.getEndRecord());
    }


    @Test
    public void should_return_0_pages_from_0_to_0() {
        Pager pager = new Pager(0, 10, 1);
        assertEquals(0, pager.getNumTotalHits());
        assertEquals(10, pager.getHitsPerPage());
        assertEquals(1, pager.getPageIndex());
        assertEquals(0, pager.getPagesCount());
        assertEquals(1, pager.getStartPage());
        assertEquals(0, pager.getEndPage());
        assertEquals(0, pager.getStartRecord());
        assertEquals(0, pager.getEndRecord());
    }


    @Test
    public void should_return_1_pages_from_1_to_2() {
        Pager pager = new Pager(2, 15, 1);
        assertEquals(2, pager.getNumTotalHits());
        assertEquals(15, pager.getHitsPerPage());
        assertEquals(1, pager.getPageIndex());
        assertEquals(1, pager.getPagesCount());
        assertEquals(1, pager.getStartPage());
        assertEquals(1, pager.getEndPage());
        assertEquals(1, pager.getStartRecord());
        assertEquals(2, pager.getEndRecord());
    }


    @Test
    public void should_return_2_pages_from_0_to_0() {
        Pager pager = new Pager(2, 1, 15);
        assertEquals(2, pager.getNumTotalHits());
        assertEquals(1, pager.getHitsPerPage());
        assertEquals(2, pager.getPageIndex());
        assertEquals(2, pager.getPagesCount());
        assertEquals(1, pager.getStartPage());
        assertEquals(2, pager.getEndPage());
        assertEquals(2, pager.getStartRecord());
        assertEquals(2, pager.getEndRecord());
    }
}
