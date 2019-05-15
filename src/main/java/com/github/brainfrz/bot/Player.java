package com.github.brainfrz.bot;

import org.javacord.api.entity.user.User;

import com.github.brainfrz.game.Hand;

public class Player {
    public User user;
    public Hand hand;

    public Player(User user, Hand hand) {
        this.user = user;
        this.hand = hand;
    }

    public Hand dealHand(Hand hand) {
        Hand oldHand = this.hand;
        this.hand = hand;
        return oldHand;
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
        return this.user.equals(p2.user);
    }
}
