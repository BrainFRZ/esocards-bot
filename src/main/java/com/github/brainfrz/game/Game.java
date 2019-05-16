package com.github.brainfrz.game;

import java.util.ArrayList;

public class Game {
    private ArrayList<Hand> hands;
    private Shoe shoe;
    private Shoe discard;
    private int handSize;
    private Hand table;  // Optional table for games that require it.

    final static int PLAYERS_PER_DECK = 1;
    final static int DEFAULT_HAND_SIZE = 5;


    public Game(int numPlayers) {
        shoe = new Shoe(numPlayers);
        discard = new Shoe(0);
        hands = new ArrayList<>();
        table = new Hand();
        handSize = DEFAULT_HAND_SIZE;
    }

    public Game() {
        this(0);
    }


    public Hand dealTable(final int cards) throws EmptyShoeException {
        Card nextCard;
        for (int i = 0; i < cards; i++) {
            nextCard = shoe.deal();
            table.add(nextCard);
        }
        return table;
    }

    public void clearTable() {
        table.clear();
    }

    public Hand table() {
        return table;
    }


    public Hand dealHand(final int cards, final Hand hand) {
        return shoe.deal(hand, cards);
    }

    public void discard(Hand pile) {
        discard.addAll(pile);
        pile.clear();
    }


    public int setHandSize(int size) {
        handSize = size;
        return handSize;
    }

    public int getHandSize() {
        return handSize;
    }

    public void addHand(Hand hand) {
        hands.add(hand);
    }

    public boolean newPlayerAddsDeck() {
        return (hands.size() % PLAYERS_PER_DECK == PLAYERS_PER_DECK - 1);
    }

    public boolean removeHand(Hand hand) {
        return hands.remove(hand);
    }

    public boolean leftPlayerDropsDeck() {
        return (hands.size() % PLAYERS_PER_DECK == 1);
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

    public void fillShoe(final int players) {
        shoe.fillShoe(players);
    }

    public int cardsLeft() {
        return shoe.cardsLeft();
    }


    public int playerTotal() {
        return hands.size();
    }


    public void reset() {
        shoe.emptyShoe();
        hands.clear();
        table.clear();
        discard.emptyShoe();
    }
}
