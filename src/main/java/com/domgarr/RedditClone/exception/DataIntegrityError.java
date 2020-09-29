package com.domgarr.RedditClone.exception;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class DataIntegrityError extends RuntimeException{
    //RuntimeExceptions are exceptions that can be prevent programmatically.

    public DataIntegrityError(String message) {
        super(message);
    }
}
