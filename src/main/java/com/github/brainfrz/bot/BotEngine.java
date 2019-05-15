package com.github.brainfrz.bot;

import com.github.brainfrz.game.EmptyShoeException;
import com.github.brainfrz.game.Game;
import com.github.brainfrz.game.Hand;
import org.javacord.api.entity.user.User;

import java.util.ArrayList;


public class BotEngine {
    public Game game;
    private ArrayList<User> users;
    private ArrayList<Player> players;
    private int handSize;

    public BotEngine() {
        game = new Game();
        users = new ArrayList<>();
        players = new ArrayList<>();
//        handSize = 0;
        handSize = 5;
    }


    public boolean addPlayer(User newUser) {
        if (users == null) {
            users = new ArrayList<>();
        }
        if (game == null) {
            game = new Game(0);
        }

        if (users.indexOf(newUser) == -1) {
            boolean addedDeck = game.newPlayerAddsDeck();
            users.add(newUser);
            game = new Game(users.size());
            players = new ArrayList<>();

            if (addedDeck) {
                game.fillShoe(users.size());

                for (User user : users) {
                    Hand hand = new Hand(game.getShoe(), handSize);
                    Player newPlayer = new Player(newUser, hand);
                    players.add(newPlayer);
                    game.addHand(hand);
                }
            } else {
                Hand hand = new Hand(game.getShoe(), handSize);
                Player newPlayer = new Player(newUser, hand);
                players.add(newPlayer);
                game.addHand(hand);
            }

            return true;
        }
        return false;
    }

    public boolean dropPlayer(User leftUser) {
        if (players.isEmpty() || users.indexOf(leftUser) == -1) {
            return false;
        }

        boolean droppedDeck = game.leftPlayerDropsDeck();
        boolean playerDropped = false;
        Player player;
        for (int i = 0; i < players.size() && !playerDropped; i++) {
            player = players.get(i);
            if (leftUser.equals(player.USER)) {
                game.removeHand(player.HAND);
                players.remove(player);
                users.remove(leftUser);
                playerDropped = true;
            }
        }

        if (playerDropped) {
            game.fillShoe(users.size());
            players = new ArrayList<>();

            for (User user : users) {
                Hand hand = new Hand(game.getShoe(), handSize);
                player = new Player(user, hand);
                players.add(player);
                game.addHand(hand);
            }
        }

        return playerDropped;
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
        return (users.indexOf(user) != -1);
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
