package com.badlogic.gdx.utils;

public class SharedLibraryLoadRuntimeException extends RuntimeException {
    private static final long serialVersionUID = 8263101105331379889L;

    public SharedLibraryLoadRuntimeException (String message) {
        super(message);
    }

    public SharedLibraryLoadRuntimeException (Throwable t) {
        super(t);
    }

    public SharedLibraryLoadRuntimeException (String message, Throwable t) {
        super(message, t);
    }
}
