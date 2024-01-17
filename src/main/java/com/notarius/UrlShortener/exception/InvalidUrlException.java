package com.notarius.UrlShortener.exception;

public class InvalidUrlException extends Exception{
    public InvalidUrlException(String errorMessage) {
        super(errorMessage);
    }
}
