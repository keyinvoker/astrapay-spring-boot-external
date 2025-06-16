package com.astrapay.exception;

public class NoteExistsException extends RuntimeException {
    public NoteExistsException(String title) {
        super("A note with title '" + title + "' already exists");
    }
}
