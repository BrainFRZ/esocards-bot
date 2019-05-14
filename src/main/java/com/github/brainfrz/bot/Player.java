package com.github.brainfrz.bot;

import org.javacord.api.entity.user.User;

import com.github.brainfrz.game.Hand;

public class Player {
    final public User USER;
    final public Hand HAND;

    public Player(User user, Hand hand) {
        USER = user;
        HAND = hand;
    }
}
