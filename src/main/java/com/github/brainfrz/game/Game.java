package com.github.brainfrz.game;

import java.util.ArrayList;

public class Game {
    private ArrayList<Hand> hands;
    private Shoe shoe;
    private Shoe discard;
    private int handSize;
    private ArrayList<Card> table;  // Optional table for games that require it.


    public Game(int numPlayers) {
        shoe = new Shoe(numPlayers);
        discard = new Shoe(0);
        hands = new ArrayList<>();
        table = new ArrayList<>();
    }

    public Game() {
        this(0);
    }

/*
    public Game(Hand initialHand) {
        this(1);
        this.handSize = initialHand.size();
        hands.add(initialHand);
    }

    public Game(ArrayList<Hand> hands) {
        this(hands.size());
        if (hands.isEmpty()) {
            handSize = 0;
        } else {
            handSize = hands.get(0).size();
        }
        this.hands = hands;
    }
 */


    public ArrayList<Card> dealTable(final int cards) throws EmptyShoeException {
        Card nextCard;
        for (int i = 0; i < cards; i++) {
            nextCard = shoe.deal();
            table.add(nextCard);
        }
        return table;
    }

    public void clearTable() {
        table = new ArrayList<>();
    }

    public ArrayList<Card> table() {
        return table;
    }


    public int setHandSize(int size) {
        handSize = size;
        return handSize;
    }

    public void drawInitialHand(Hand hand) {
        hands.add(hand);
    }

    public boolean addHand(Hand hand) {
        hands.add(hand);
        return shoe.addPlayer();
    }

    public boolean removeHand(Hand hand) {
        hands.remove(hand);
        return shoe.dropPlayer();
    }


    public void resetShoe() {
        shoe = new Shoe(playerTotal());
    }


    public void shuffleShoe() {
        shoe.shuffle();
    }

    public Shoe getShoe() {
        return shoe;
    }

    public int shoeSize() {
        return shoe.getNumDecks();
    }


    public int playerTotal() {
        return hands.size();
    }


    public void reset() {
        shoe = new Shoe(0);
        hands = new ArrayList<>();
        table = new ArrayList<>();
        discard = new Shoe(0);
    }
}
