package com.vinci.gshoot.search;

public class SearchException extends Exception {
    public SearchException() {
    }

    public SearchException(String s) {
        super(s);
    }

    public SearchException(String s, Throwable throwable) {
        super(s, throwable);
    }

    public SearchException(Throwable throwable) {
        super(throwable);
    }
}
