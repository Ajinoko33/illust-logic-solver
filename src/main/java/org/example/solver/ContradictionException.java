package org.example.solver;

public class ContradictionException extends RuntimeException {
    public ContradictionException(String message) {
        super(message);
    }
}
