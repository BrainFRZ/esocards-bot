package com.github.brainfrz.game;

import java.util.ArrayList;

public class Hand {
    private ArrayList<Card> hand;


    public Hand() {
        hand = new ArrayList<>();
    }

    public Hand(final ArrayList<Card> cards) {
        this();
        for (Card card : cards) {
            addCard(card);
        }
    }

    public Hand(Hand hand2) {
        this.hand = hand2.hand;
    }


    public Hand(final Shoe shoe, final int initialSize) {
        this();

        if (initialSize < 0) {
            throw new IllegalArgumentException("Negative number of cards dealt to hand.");
        }

        if (initialSize > shoe.cardsLeft()) {
            throw new EmptyShoeException(shoe.cardsLeft(), initialSize);
        }

        Card next;
        for (int i = 0; i < initialSize; i++) {
            next = shoe.deal(this);
        }
    }


    Hand addCard(final Card card) {
        hand.add(card);
        return this;
    }

    Hand playCard(final Card card) {
        hand.remove(card);
        return this;
    }


    public ArrayList<Card> hand() {
        return hand;
    }

    public Card[] toArray() {
        return (Card[])hand.toArray();
    }

    Card get(final int i) {
        return hand.get(i);
    }

    public int size() {
        return hand.size();
    }

    public boolean isEmpty() {
        return hand.isEmpty();
    }

    public int value() {
        int value = 0;
        for (Card card : hand) {
            value += card.value();
        }
        return value;
    }


    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        for (Card card : hand) {
            builder.append(card).append("\n");
        }
        return builder.toString();
    }

    public String tabbedString() {
        StringBuilder builder = new StringBuilder();
        for (Card card : hand) {
            builder.append("\t").append(card).append("\n");
        }
        return builder.toString();
    }
}
