package com.github.brainfrz.game;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Stack;

public class Shoe {
    private Stack<Card> cards;
    private int numDecks;
    private int players;

    final static int PLAYERS_PER_DECK = 1;


    Shoe(final int players) {
        if (numDecks < 0) {
            throw new IllegalArgumentException("Negative number of decks: " + numDecks);
        }

        this.players = players;
        fillShoe();
    }

    ArrayList<Card> addDeck() {
        ArrayList<Card> deck;
        deck = new ArrayList<Card>();
        for (Card.Suit suit : Card.Suit.values()) {
            for (Card.Face face : Card.Face.values()) {
                deck.add(new Card(face, suit));
            }
        }

        cards.addAll(deck);
        return deck;
    }

    void emptyShoe() {
        cards = new Stack<>();
        numDecks = 0;
    }

    void reshoe(Shoe discard) {
        for (Card card : discard.cards) {
            cards.push(discard.cards.pop());
        }
        Collections.shuffle(cards);
    }


    public boolean refreshPlayers() {
        boolean addedDeck = false;

        if (players % PLAYERS_PER_DECK == 0) {
            fillShoe();
            addedDeck = true;
        }

        return addedDeck;
    }

    boolean dropPlayer() {
        if (players <= 0) {
            return false;
        }

        boolean removedDeck = false;

        players -= 1;
        if (players % 2 == 0 && numDecks > 0) {
            fillShoe();
            removedDeck = true;
        }

        return removedDeck;
    }

    void fillShoe() {
        cards = new Stack<>();
        numDecks = (int)Math.ceil(players / (double)PLAYERS_PER_DECK);
        for (int i = 0; i < numDecks; i++) {
            addDeck();
        }
        Collections.shuffle(cards);
    }


    Shoe shuffle() {
        Collections.shuffle(cards);
        return this;
    }


    Card nextCard() {
        return cards.peek();
    }

    int getNumDecks() {
        return numDecks;
    }

    boolean isEmpty() {
        return cards.isEmpty();
    }

    int cardsLeft() {
        return cards.size();
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
        return cards.pop();
    }

    ArrayList<Card> deal(final int handSize) throws EmptyShoeException {
        if (handSize > cardsLeft()) {
            throw new EmptyShoeException(cardsLeft(), handSize);
        }

        ArrayList<Card> cardsDealt = new ArrayList<>();

        Card card;
        for (int i = 0; i < handSize; i++) {
            card = cards.pop();
            cardsDealt.add(card);
        }

        return cardsDealt;
    }

    ArrayList<Card> deal(Hand hand, final int handSize) throws EmptyShoeException {
        if (handSize > cardsLeft()) {
            throw new EmptyShoeException(cardsLeft(), handSize);
        }

        ArrayList<Card> cardsDealt = deal(handSize);
        hand = new Hand(cardsDealt);
        return cardsDealt;
    }

    Card deal(Hand hand) {
        Card next = deal();
        hand.addCard(next);
        return next;
    }


    Shoe discard(Shoe discard, Card card) {
        discard.cards.push(card);
        return discard;
    }
}
