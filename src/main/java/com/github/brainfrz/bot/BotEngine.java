package com.github.brainfrz.bot;

import com.github.brainfrz.game.Game;
import com.github.brainfrz.game.Hand;
import org.javacord.api.entity.user.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class BotEngine {
    private Game game;
    private ArrayList<Player> players;
    private int handSize;

    public BotEngine() {
        game = new Game();
        players = new ArrayList<>();
        handSize = 0;
    }

    public BotEngine(int handSize) {
        this();
        this.handSize = handSize;
    }

    public BotEngine(Player player) {
        this();
        handSize = player.HAND.size();
        Hand hand = new Hand(game.getShoe(), handSize);
        players.add(player);
    }


    public ArrayList<Player> addPlayer(User user) {
        Hand hand = new Hand(game.getShoe(), handSize);
        Player newPlayer = new Player(user, hand);

        if (players == null) {
            players = new ArrayList<>();
        }
        if (game == null) {
            game = new Game(1);
        }

        players.add(newPlayer);
        return players;
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


    public ArrayList<Player> roster() {
        return players;
    }

    public int rosterSize() {
        return players.size();
    }
}
