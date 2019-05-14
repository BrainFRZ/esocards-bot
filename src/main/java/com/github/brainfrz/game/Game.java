package com.github.brainfrz.game;

import java.util.ArrayList;

public class Game {
    private int numPlayers;
    private ArrayList<Hand> hands;
    private Shoe shoe;
    private int handSize;
    private ArrayList<Card> table;  // Optional table for games that require it.


    public Game(int players) {
        numPlayers = players;
        shoe = new Shoe(players);
        hands = new ArrayList<>();
        table = new ArrayList<>();
    }

    public Game() {
        this(0);
    }

    public Game(int players, int handSize) {
        this(players);
        this.handSize = handSize;
        for (int i = 0; i < players; i++) {
            hands.add(new Hand(shoe, handSize));
        }
    }


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


    public void resetShoe() {
        shoe = new Shoe(numPlayers);
    }


    public void shuffleShoe() {
        shoe.shuffle();
    }


    public int playerTotal() {
        return numPlayers;
    }


    public void reset() {
        numPlayers = 0;
        shoe = new Shoe(0);
        hands = new ArrayList<>();
        table = new ArrayList<>();
    }
}
