package com.github.brainfrz.game;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Stack;

class Shoe extends Stack<Card> {
    private int numDecks;



    Shoe(final int players) {
        if (numDecks < 0) {
            throw new IllegalArgumentException("Negative number of decks: " + numDecks);
        }

        fillShoe(players);
    }

    void emptyShoe() {
        this.clear();
        numDecks = 0;
    }

    void reshoe(Shoe discard) {
        while (!discard.isEmpty()) {
            this.push(discard.pop());
        }
        Collections.shuffle(this);
    }


    void fillShoe(final int players) {
        this.clear();
        numDecks = (int)Math.ceil(players / (double)Game.PLAYERS_PER_DECK);
        for (int i = 0; i < numDecks; i++) {
            addDeck();
        }
        Collections.shuffle(this);
    }

    private ArrayList<Card> addDeck() {
        ArrayList<Card> deck;
        deck = new ArrayList<Card>();
        for (Card.Suit suit : Card.Suit.values()) {
            for (Card.Face face : Card.Face.values()) {
                deck.add(new Card(face, suit));
            }
        }

        this.addAll(deck);
        return deck;
    }


    Shoe shuffle() {
        Collections.shuffle(this);
        return this;
    }


    Card nextCard() {
        return peek();
    }

    int getNumDecks() {
        return numDecks;
    }

    public int cardsLeft() {
        return size();
    }


    /**
     * Deals the next card from the shoe.
     *
     * @return Card dealt
     */
    Card deal() throws EmptyShoeException {
        if (this.isEmpty()) {
            throw new EmptyShoeException();
        }
        return pop();
    }

    Hand deal(final int handSize) throws EmptyShoeException {
        if (handSize > cardsLeft()) {
            throw new EmptyShoeException(cardsLeft(), handSize);
        }

        Hand cardsDealt = new Hand();

        Card card;
        for (int i = 0; i < handSize; i++) {
            card = pop();
            cardsDealt.add(card);
        }

        return cardsDealt;
    }

    /**
     * Deals the given number of cards to the hand and removes them from this shoe.
     *
     * @param hand Hand receiving the cards
     * @param handSize Number of cards to be dealt
     * @return Hand of cards dealt
     * @throws EmptyShoeException Thrown if there aren't enough cards left in the shoe
     */
    Hand deal(Hand hand, final int handSize) throws EmptyShoeException {
        if (handSize > cardsLeft()) {
            throw new EmptyShoeException(cardsLeft(), handSize);
        }

        Hand cardsDealt = deal(handSize);
        hand.addAll(cardsDealt);
        return cardsDealt;
    }

    Card deal(Hand hand) {
        Card next = deal();
        hand.add(next);
        return next;
    }


    Shoe discard(Shoe discard, Card card) {
        discard.push(card);
        this.remove(card);
        return discard;
    }

    Shoe discard(Shoe discard, Hand pile) {
        while (!pile.isEmpty()) {
            discard.push(pile.get(0));
            pile.remove(0);
        }
        return discard;
    }
}
