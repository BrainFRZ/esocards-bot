package com.github.brainfrz.game;

import java.util.ArrayList;
import java.util.Collections;

public class Hand extends ArrayList<Card> {

    public Hand() {
        super();
    }

    public Hand(final ArrayList<Card> cards) {
        this();
        this.addAll(cards);
    }

    public Hand(Hand hand2) {
        this(new ArrayList(hand2));
    }


    public Hand(final Shoe shoe, final int initialSize) {
        this();

        if (initialSize < 0) {
            throw new IllegalArgumentException("Negative number of cards dealt to hand.");
        }

        if (initialSize > shoe.cardsLeft()) {
            throw new EmptyShoeException(shoe.cardsLeft(), initialSize);
        }

        for (int i = 0; i < initialSize; i++) {
            shoe.deal(this);
        }
    }


    public int value() {
        int value = 0;
        for (Card card : this) {
            value += card.value();
        }
        return value;
    }


    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        for (Card card : this) {
            builder.append(card).append("\n");
        }
        return builder.toString();
    }

    public String tabbedString() {
        StringBuilder builder = new StringBuilder();
        Card[] hand = (Card[])this.toArray();
        for (Card card : hand) {
            builder.append("\t").append(card).append("\n");
        }
        return builder.toString();
    }
}
