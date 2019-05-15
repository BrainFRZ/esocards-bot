package com.github.brainfrz.game;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Stack;

class Shoe {
    private Stack<Card> cards;
    private int numDecks;



    Shoe(final int players) {
        if (numDecks < 0) {
            throw new IllegalArgumentException("Negative number of decks: " + numDecks);
        }

        fillShoe(players);
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
        while (!discard.cards.isEmpty()) {
            cards.push(discard.cards.pop());
        }
        Collections.shuffle(cards);
    }


    void fillShoe(final int players) {
        cards = new Stack<>();
        numDecks = (int)Math.ceil(players / (double)Game.PLAYERS_PER_DECK);
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

    public int cardsLeft() {
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

    Shoe discard(Shoe discard, Hand pile) {
        for (Card card : pile.hand()) {
            discard.cards.push(card);
        }
        return discard;
    }
}
