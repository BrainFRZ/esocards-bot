package com.github.brainfrz.game;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class Hand {
    private ArrayList<Card> hand;
    private int value;


    public Hand() {
        hand = new ArrayList<>();
        value = 0;
    }

    public Hand(Shoe shoe, final int initialSize) {
        this();

        if (initialSize <= 0) {
            throw new IllegalArgumentException("Negative number of cards dealt to hand.");
        }

        if (initialSize > shoe.cardsLeft()) {
            throw new EmptyDeckException(shoe.cardsLeft(), initialSize);
        }

        Card next;
        for (int i = 0; i < initialSize; i++) {
            next = shoe.deal(this);
            value += next.value();
        }
    }


    public Hand addCard(final Card card) {
        hand.add(card);
        return this;
    }

    public Hand playCard(final Card card) {
        hand.remove(card);
        return this;
    }


    public ArrayList<Card> hand() {
        return hand;
    }

    public Card[] toArray() {
        return (Card[])hand.toArray();
    }

    public Card get(final int i) {
        return hand.get(i);
    }

    public int size() {
        return hand.size();
    }

    public int value() {
        return value;
    }
}