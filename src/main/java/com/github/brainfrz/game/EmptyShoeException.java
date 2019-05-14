package com.github.brainfrz.game;

public class EmptyShoeException extends RuntimeException {
    public EmptyShoeException() {
        super("No cards are left in deck.");
    }

    public EmptyShoeException(int needed) {
        super("No cards are left in deck. " + needed + " are needed.");
    }

    public EmptyShoeException(int left, int needed) {
        super(left + " cards are left in deck. " + needed + " are needed.");
    }
}
