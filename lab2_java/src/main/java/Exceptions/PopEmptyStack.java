package Exceptions;

import java.util.NoSuchElementException;

public class PopEmptyStack extends NoSuchElementException{
    public PopEmptyStack(String message) {
        super(message);
    }
}
