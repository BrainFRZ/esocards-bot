package com.github.brainfrz.bot;

import com.github.brainfrz.game.Game;
import com.github.brainfrz.game.Hand;
import org.javacord.api.entity.user.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class BotEngine {
    private Game game;
    private Map<Player, Hand> players;
    private int handSize;

    public BotEngine() {
        game = new Game();
        players = new HashMap<>();
        handSize = 0;
    }

    public BotEngine(int handSize) {
        this();
        this.handSize = handSize;
    }

    public BotEngine(Player player, int handSize) {
        this();
        Hand hand = new Hand(game.getShoe(), handSize);
        players.put(player, hand);
    }


    public Map<Player,Hand> addPlayer(User user) {
        Player newPlayer = new Player(user);

        if (players == null) {
            players = new HashMap<>();
        }
        if (game == null) {
            game = new Game(1);
        }

        Hand hand = new Hand(game.getShoe(), handSize);
        players.put(newPlayer, hand);
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


    public ArrayList<Player> getPlayers() {
        return new ArrayList<Player>(players.keySet());
    }

    public int getPlayersSize() {
        return players.size();
    }
}
