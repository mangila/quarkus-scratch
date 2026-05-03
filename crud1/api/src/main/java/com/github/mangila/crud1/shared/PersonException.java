package com.github.mangila.crud1.shared;

public class PersonException extends RuntimeException {

    public PersonException(String message) {
        super(message);
    }

    public PersonException(String message, Throwable e) {
        super(message, e);
    }
}
