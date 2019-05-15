package com.github.brainfrz.game;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Stack;

public class Shoe {
    private Stack<Card> cards;
    private int numDecks;
    private int players;

    final public static int PLAYERS_PER_DECK = 2;


    public Shoe(final int players) {
        if (numDecks <= 0) {
            throw new IllegalArgumentException("Negative number of decks: " + numDecks);
        }

        this.players = players;
        fillShoe();
    }

    public ArrayList<Card> addDeck() {
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

    public void emptyShoe() {
        cards = new Stack<>();
        numDecks = 0;
    }


    public boolean addPlayer() {
        boolean addedDeck = false;

        players += 1;
        if (players % 2 == 0) {
            fillShoe();
            addedDeck = true;
        }

        return addedDeck;
    }

    public boolean dropPlayer() {
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

    private void fillShoe() {
        cards = new Stack<>();
        numDecks = (int)Math.ceil(players / (double)PLAYERS_PER_DECK);
        if (numDecks == 0) {
            numDecks = 1;
        }
        for (int i = 0; i < numDecks; i++) {
            addDeck();
        }
        Collections.shuffle(cards);
    }


    public Shoe shuffle() {
        Collections.shuffle(cards);
        return this;
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


    /**
     * Deals the next card from the shoe.
     *
     * @return Card dealt
     */
    public Card deal() throws EmptyShoeException {
        if (this.isEmpty()) {
            throw new EmptyShoeException();
        }
        return cards.pop();
    }

    public ArrayList<Card> deal(final int handSize) throws EmptyShoeException {
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

    public ArrayList<Card> deal(Hand hand, final int handSize) throws EmptyShoeException {
        if (handSize > cardsLeft()) {
            throw new EmptyShoeException(cardsLeft(), handSize);
        }

        ArrayList<Card> cardsDealt = deal(handSize);
        hand = new Hand(cardsDealt);
        return cardsDealt;
    }

    public Card deal(Hand hand) {
        Card next = deal();
        hand.addCard(next);
        return next;
    }
}
