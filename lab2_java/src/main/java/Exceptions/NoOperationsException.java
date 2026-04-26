package Exceptions;

import java.lang.ClassNotFoundException;

public class NoOperationsException extends ClassNotFoundException {
    public NoOperationsException(String message) {
        super(message);
    }
}
