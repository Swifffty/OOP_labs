package Exceptions;

import java.util.NoSuchElementException;

public class FewElements extends NoSuchElementException {
    public FewElements(String message) {
        super(message);
    }
}
