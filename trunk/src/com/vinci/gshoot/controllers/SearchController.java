package com.vinci.gshoot.controllers;

import com.vinci.gshoot.search.SearchException;
import com.vinci.gshoot.search.SearchResults;
import com.vinci.gshoot.search.SearchService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

@Controller
public class SearchController {
    private SearchService searchService;
    private Logger logger = Logger.getLogger(SearchController.class);

    @Autowired
    public SearchController(SearchService indexService) {
        this.searchService = indexService;
    }

    @RequestMapping("/search.do")
    public ModelAndView handleRequest(HttpServletRequest httpServletRequest) throws Exception {
        String keywords = httpServletRequest.getParameter("q");
        if (keywords == null || "".equals(keywords.trim())) {
            return new ModelAndView("search");
        }
        int pageIndex = parsePageIndex(httpServletRequest);
        logger.info("Request with: " + keywords);
        try {
            long start = System.currentTimeMillis();
            SearchResults results = this.searchService.search(keywords, pageIndex);
            ModelAndView view = new ModelAndView("result");
            view.getModel().put("t", (System.currentTimeMillis() - start) / 1000.0);
            view.getModel().put("q", keywords);
            view.getModel().put("rs", results);

            return view;
        }
        catch (SearchException e) {
            logger.warn("Search failed for keywords " + keywords + ": " + e.getMessage(), e);
            ModelAndView view = new ModelAndView("search");
            view.getModel().put("message", e);
            return view;
        }
    }

    private int parsePageIndex(HttpServletRequest httpServletRequest) {
        int index = 1;
        try {
            index = Integer.valueOf(httpServletRequest.getParameter("pageIndex"));
            if (index < 1) {
                index = 1;
            }
        }
        catch (Exception e) {
        }
        return index;
    }
}