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
                for (Card.Face face : Card.Face.values()) {
                    deck.add(new Card(face, suit));
                }
            }

            cards.addAll(deck);
        }

        Collections.shuffle(cards);
    }


    public Card nextCard() {
        return cards.peek();
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


    public Card deal(Hand hand) {
        Card next = cards.pop();
        hand.addCard(next);
        return next;
    }

    public ArrayList<Card> deal(Hand hand, final int handSize) {
        ArrayList<Card> cardsDealt = new ArrayList<>();

        Card card;
        for (int i = 0; i < handSize; i++) {
            card = deal(hand);
            cardsDealt.add(card);
        }

        return cardsDealt;
    }
}
