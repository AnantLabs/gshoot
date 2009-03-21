package com.vinci.gshoot.parser;

public class WrongFileException extends Exception {
    public WrongFileException() {
    }

    public WrongFileException(String s) {
        super(s);
    }

    public WrongFileException(String s, Throwable throwable) {
        super(s, throwable);
    }

    public WrongFileException(Throwable throwable) {
        super(throwable);
    }
}
