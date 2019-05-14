package com.github.brainfrz.game;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Stack;

public class Shoe {
    private Stack<Card> cards;
    private int numDecks;


    public Shoe(final int numDecks) {
        if (numDecks <= 0) {
            throw new IllegalArgumentException("Negative number of decks: " + numDecks);
        }

        this.cards = new Stack<>();
        this.numDecks = numDecks;

        ArrayList<Card> deck;
        for (int i = 0; i < numDecks; i++) {
            deck = new ArrayList<Card>();
            for (Card.Suit suit : Card.Suit.values()) {
                deck.add(new Card(face, suit));
            }

            cards.addAll(deck);
        }

        Collections.shuffle(cards);
    }


    public Card getNext() {
        return cards.pop();
    }

    public int getNumDecks() {
        return numDecks;
    }

    public boolean isEmpty() {
        return cards.isEmpty();
    }

    public int cardsLeft() {
        return cards.size();
    }
}
