package com.github.brainfrz.bot;

import org.javacord.api.entity.user.User;

import com.github.brainfrz.game.Hand;

public class Player {
    final public User USER;
    final public Hand HAND;

    public Player(User user, Hand hand) {
        USER = user;
        HAND = new Hand();
    }

    @Override
    public boolean equals(Object player2) {
        if (this == player2) {
            return true;
        }

        if (player2 == null || getClass() != player2.getClass()) {
            return false;
        }

        Player p2 = (Player)player2;
        System.out.println(p2.USER.getMentionTag() + " is a player.");
        return this.USER == p2.USER;
    }
}
