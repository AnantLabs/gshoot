package com.vinci.gshoot.parser;

import org.apache.poi.hwpf.extractor.WordExtractor;

import java.io.FileInputStream;

public class WordParser implements Parser {
    public String parse(String fileName) throws WrongFileException {
        try {
            return new WordExtractor(new FileInputStream(fileName)).getText();
        } catch (Exception e) {
            throw new WrongFileException(e.getMessage());
        }
    }
}
