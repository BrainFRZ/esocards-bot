package com.github.brainfrz.bot;

import com.github.brainfrz.game.EmptyShoeException;
import com.github.brainfrz.game.Game;
import com.github.brainfrz.game.Hand;
import org.javacord.api.entity.user.User;

import java.util.ArrayList;


public class BotEngine {
    private Game game;
    private ArrayList<Player> players;
    private int handSize;

    public BotEngine() {
        game = new Game();
        players = new ArrayList<>();
//        handSize = 0;
        handSize = 5;
    }

    public BotEngine(int handSize) {
        this();
        this.handSize = handSize;
    }

    public BotEngine(Player player) {
        this();
        handSize = player.HAND.size();
        players.add(player);
    }


    public boolean addPlayer(User user) {
        if (players == null) {
            players = new ArrayList<>();
        }
        if (game == null) {
            game = new Game(0);
        }

        if (game.playerTotal() == 0) {
            game = new Game(1);
            Hand hand = new Hand(game.getShoe(), handSize);
            Player newPlayer = new Player(user, hand);
            players.add(newPlayer);
            game.drawInitialHand(hand);
            return true;
        }

        Hand hand = new Hand(game.getShoe(), handSize);
        Player newPlayer = new Player(user, hand);
        if (players.indexOf(newPlayer) == -1) {
            players.add(newPlayer);
            game.addHand(hand);
            return true;
        }
        return false;
    }

    public boolean dropPlayer(User user) {
        if (players.isEmpty()) {
            return false;
        }

        Player player;
        for (int i = 0; i < players.size(); i++) {
            player = players.get(i);
            if (user.equals(player.USER)) {
                game.removeHand(player.HAND);
                players.remove(player);
                return true;
            }
        }

        return false;
    }


    public int shoeSize() {
        return game.shoeSize();
    }

    public String shoeSizeStr() {
        int size = shoeSize();
        if (size == 1) {
            return "There is now 1 deck in play.";
        } else {
            return "There are now " + size + " decks in play.";
        }
    }


    public void setHandSize(int size) {
        if (handSize < 0) {
            throw new IllegalArgumentException("Negative hand size: " + handSize);
        }

        handSize = size;
    }

    public int getHandSize() {
        return handSize;
    }

    public Hand getUserHand(User user) {
        Player player;
        for (Player value : players) {
            player = value;
            if (user.equals(player.USER)) {
                return player.HAND;
            }
        }
        return new Hand();
    }


    public ArrayList<Player> roster() {
        return players;
    }

    public int rosterSize() {
        return players.size();
    }

    public boolean isPlaying(User user) {
        for (Player player : players) {
            if (user.equals(player.USER)) {
                return true;
            }
        }
        return false;
    }


    public Hand getTable() {
        return new Hand(game.table());
    }

    public Hand dealTable(final int cards) throws EmptyShoeException {
        game.dealTable(cards);
        return getTable();
    }

    public boolean clearTable() {
        if (game.table().isEmpty()) {
            return false;
        } else {
            game.clearTable();
            return true;
        }
    }
}
