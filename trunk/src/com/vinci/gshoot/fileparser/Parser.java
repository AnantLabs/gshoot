package com.vinci.gshoot.parser;

public interface Parser {
    String parse(String fileName) throws WrongFileException;
}
