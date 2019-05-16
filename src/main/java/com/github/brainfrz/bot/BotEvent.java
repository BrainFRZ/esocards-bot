package com.github.brainfrz.bot;

import org.javacord.api.entity.user.User;
import org.javacord.api.event.message.MessageCreateEvent;

public class BotEvent {
    public MessageCreateEvent event;
    public User user;
    public BotEngine engine;

    BotEvent(MessageCreateEvent event, User user, BotEngine engine) {
        this.event = event;
        this.user = user;
        this.engine = engine;
    }
}
