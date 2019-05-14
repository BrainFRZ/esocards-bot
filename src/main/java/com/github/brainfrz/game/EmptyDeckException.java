package com.github.brainfrz.game;

public class EmptyDeckException extends RuntimeException {
    public EmptyDeckException() {
        super("No cards are left in deck.");
    }

    public EmptyDeckException(int needed) {
        super("No cards are left in deck. " + needed + " are needed.");
    }

    public EmptyDeckException(int left, int needed) {
        super(left + " cards are left in deck. " + needed + " are needed.");
    }
}
