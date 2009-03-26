package com.vinci.gshoot.watchdog;

public class InvalidConfigurationException extends RuntimeException {
    public InvalidConfigurationException() {
    }

    public InvalidConfigurationException(String s) {
        super(s);
    }

    public InvalidConfigurationException(String s, Throwable throwable) {
        super(s, throwable);
    }

    public InvalidConfigurationException(Throwable throwable) {
        super(throwable);
    }
}
