package com.github.brainfrz.bot;

import com.github.brainfrz.game.Card;
import com.github.brainfrz.game.EmptyShoeException;
import com.github.brainfrz.game.Hand;

import org.javacord.api.DiscordApi;
import org.javacord.api.DiscordApiBuilder;
import org.javacord.api.entity.message.MessageBuilder;
import org.javacord.api.entity.permission.PermissionType;
import org.javacord.api.entity.permission.Permissions;
import org.javacord.api.entity.permission.PermissionsBuilder;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

public class ESOCardsBot {
    public static void main(String[] args) {
        final String FILENAME = "src/BOT_TOKEN";
        String token = "";
        try {
            token = readTokenFile(FILENAME);
        } catch (IOException e) {
            System.out.println("Unable to read filename " + FILENAME + ".");
            return;
        }

        DiscordApi api = new DiscordApiBuilder().setToken(token).login().join();
        BotEngine engine = new BotEngine();


        api.addMessageCreateListener(event -> {
            if (!event.getMessageAuthor().isBotUser()
                    && event.getMessage().getContent().equalsIgnoreCase("!join")) {
                addUser(new BotEvent(event, event.getMessageAuthor().asUser().get(), engine));
            }
        });

        api.addMessageCreateListener(event -> {
            if (!event.getMessageAuthor().isBotUser()
                    && event.getMessage().getContent().equalsIgnoreCase("!leave")) {
                removeUser(new BotEvent(event, event.getMessageAuthor().asUser().get(), engine));
            }
        });

        api.addMessageCreateListener(event -> {
            if (!event.getMessageAuthor().isBotUser()
                    && event.getMessage().getContent().equalsIgnoreCase("!roster")) {
                printRoster(new BotEvent(event, event.getMessageAuthor().asUser().get(), engine));
            }
        });

        api.addMessageCreateListener(event -> {
            if (!event.getMessageAuthor().isBotUser()
                    && event.getMessage().getContent().equalsIgnoreCase("!hand")) {
                tellHand(new BotEvent(event, event.getMessageAuthor().asUser().get(), engine));
            }
        });

        api.addMessageCreateListener(event -> {
            String message = event.getMessage().getContent().toLowerCase();
            if (!event.getMessageAuthor().isBotUser()
                    && message.regionMatches(true, 0, "!table", 0, 6)) {
                doTable(new BotEvent(event, event.getMessageAuthor().asUser().get(), engine));
            }
        });

        api.addMessageCreateListener(event -> {
            String message = event.getMessage().getContent().toLowerCase();
            if (!event.getMessageAuthor().isBotUser()
                    && message.regionMatches(true, 0, "!draw", 0, 5)) {
                doDraw(new BotEvent(event, event.getMessageAuthor().asUser().get(), engine));
            }
        });

        api.addMessageCreateListener(event -> {
            if (!event.getMessageAuthor().isBotUser()
                    && event.getMessage().getContent().equalsIgnoreCase("!cards left")) {
                cardsLeft(new BotEvent(event, event.getMessageAuthor().asUser().get(), engine));
            }
        });

        api.addMessageCreateListener(event -> {
            String message = event.getMessage().getContent().toLowerCase();
            if (!event.getMessageAuthor().isBotUser()
                    && message.regionMatches(true, 0, "!burn", 0, 5)) {
                doBurn(new BotEvent(event, event.getMessageAuthor().asUser().get(), engine));
            }
        });

        api.addMessageCreateListener(event -> {
            String message = event.getMessage().getContent().toLowerCase();
            if (!event.getMessageAuthor().isBotUser()
                    && message.equalsIgnoreCase("!reshoe")) {
                doReshoe(new BotEvent(event, event.getMessageAuthor().asUser().get(), engine));
            }
        });


        // Print the invite url of your bot:
        // https://discordapp.com/oauth2/authorize?client_id=577728737391673344&scope=bot&permissions=2048
        PermissionsBuilder pBuilder = new PermissionsBuilder();
        pBuilder.setAllowed(PermissionType.SEND_MESSAGES);
        Permissions permissions = pBuilder.build();
        System.out.println("You can invite the bot by using the following url: " + api.createBotInvite(permissions));
    }

    private static String readTokenFile(final String filename) throws IOException {
        String token = new String(Files.readAllBytes(Paths.get(filename)), "UTF-8");
        return token;
    }


    private static void addUser(BotEvent ev) {
        int oldDecks = ev.engine.shoeSize();
        if (ev.engine.addPlayer(ev.user)) {
            ev.event.getChannel().sendMessage(ev.user.getNicknameMentionTag() + " just joined the game!");

            int newDecks = ev.engine.shoeSize();
            if (oldDecks != newDecks) {
                ev.event.getChannel().sendMessage("A new deck has been added to the game. "
                        + ev.engine.shoeSizeStr());
            }
            ev.event.getChannel().sendMessage("There are " + ev.engine.rosterSize() + " players.");
        } else {
            ev.event.getChannel().sendMessage(ev.user.getNicknameMentionTag() + " is already playing.");
        }
    }

    private static void removeUser(BotEvent ev) {
        int oldDecks = ev.engine.shoeSize();
        if (ev.engine.dropPlayer(ev.user)) {
            ev.event.getChannel().sendMessage(ev.user.getNicknameMentionTag()
                    + " just left the game. Awwwww...");

            int newDecks = ev.engine.shoeSize();
            if (oldDecks != newDecks) {
                ev.event.getChannel().sendMessage("A deck has been removed from the game. " + ev.engine.shoeSizeStr());
            }
        } else {
            ev.event.getChannel().sendMessage(ev.user.getNicknameMentionTag() + " isn't playing anyway.");
        }
    }


    private static void printRoster(BotEvent ev) {
        ArrayList<Player> roster = ev.engine.roster();

        if (roster.isEmpty()) {
            ev.event.getChannel().sendMessage("No one is playing cards right now. " +
                    "Be the first by typing `!eso-cards join`!");
            return;
        }

        MessageBuilder builder = new MessageBuilder();
        builder.append("The following Guardians are playing cards:").appendNewLine();
        for (Player player : roster) {
            builder.append("\t").append(player.user.getNicknameMentionTag()).appendNewLine();
        }

        builder.send(ev.event.getChannel());
    }


    private static void tellHand(BotEvent ev, boolean silent) {
        if (!silent) {
            if (!ev.engine.isPlaying(ev.user)) {
                ev.event.getChannel().sendMessage(ev.user.getNicknameMentionTag() + " isn't playing. Come join!");
                return;
            }
            ev.event.getChannel().sendMessage(ev.user.getNicknameMentionTag() + " just checked their hand.");
        }

        Hand hand = ev.engine.getPlayer(ev.user).hand;
        if (hand.isEmpty()) {
            ev.user.sendMessage("Your hand is empty. Deal another.");
        } else {
            ev.user.sendMessage("You have the following hand:\n" + hand.indexedString(true));
        }
    }

    private static void tellHand(BotEvent ev) {
        tellHand(ev, false);
    }


    private static void doTable(BotEvent ev) {
        String message = ev.event.getMessageContent().toLowerCase();

        if (message.equals("!table")) {
            showTable(ev);
        } else if (message.matches("^!table \\d+$")) {
            dealTable(ev);
        } else if (message.equalsIgnoreCase("!table clear")) {
            clearTable(ev);
        } else {
            ev.event.getChannel().sendMessage("Invalid usage. Type `!help table` for help.");
        }
    }

    private static void showTable(BotEvent ev) {
        if (ev.engine.getTable().isEmpty()) {
            ev.event.getChannel().sendMessage("There are no cards on the table.");
        } else {
            ev.event.getChannel().sendMessage("The following cards are on the table:\n"
                    + ev.engine.getTable().tabbedString());
        }
    }

    private static void dealTable(BotEvent ev) {
        String message = ev.event.getMessageContent();
        int cardsDealt = Integer.parseInt(message.substring(7));

        try {
            ev.engine.dealTable(cardsDealt);   // throws EmptyShoeException if there aren't enough cards
            ev.event.getChannel()
                    .sendMessage(ev.user.getNicknameMentionTag()
                            + " deals " + cardsDealt + " cards to the table:\n"
                                + ev.engine.getTable().tabbedString());
        } catch (EmptyShoeException e) {
            ev.event.getChannel().sendMessage("There aren't enough cards left in the shoe. " +
                    "Type `!reshoe` to reset the discard pile.");
        }
    }

    public static void clearTable(BotEvent ev) {
        boolean cleared = ev.engine.clearTable();
        if (cleared) {
            ev.event.getChannel().sendMessage(ev.user.getMentionTag() + " cleared the table.");
        } else {
            ev.event.getChannel().sendMessage("There are no cards on the table.");
        }
    }


    public static void doReshoe(BotEvent ev) {
        if (!ev.engine.isPlaying(ev.user)) {
            ev.event.getChannel().sendMessage(ev.user.getNicknameMentionTag() + " isn't playing. Come join!");
            return;
        }

        ev.engine.reshoe();
        ev.event.getChannel().sendMessage(ev.user.getNicknameMentionTag() + " takes the discard pile and " +
                "shuffles it back into the shoe.");
    }


    public static void doDraw(BotEvent ev) {
        String message = ev.event.getMessageContent().toLowerCase();

        if (!ev.engine.isPlaying(ev.user)) {
            ev.event.getChannel().sendMessage(ev.user.getNicknameMentionTag() + " isn't playing. Come join!");
            return;
        }

        if (message.equals("!draw") || (message.length() == 7 && message.charAt(6) == '1')) {
            dealOneCard(ev);
        } else if (message.matches("^!draw new( hand)?$")) {
            dealNewHand(ev);
        } else if (message.matches("^!draw \\d+$")) {
            dealCards(ev);
        } else {
            ev.event.getChannel().sendMessage("Invalid usage. Type `!help draw` for help.");
        }
    }

    private static void dealOneCard(BotEvent ev) {
        String message = ev.event.getMessageContent();

        try {
            ev.engine.dealHand(ev.user, 1);   // throws EmptyShoeException if there aren't enough cards
            ev.event.getChannel()
                    .sendMessage(ev.user.getNicknameMentionTag() + " draws a card from the shoe.");
            tellHand(ev, true);
        } catch (EmptyShoeException e) {
            ev.event.getChannel().sendMessage("There aren't enough cards left in the shoe. " +
                    "Type `!reshoe` to reset the discard pile.");
        }
    }

    private static void dealCards(BotEvent ev) {
        String message = ev.event.getMessageContent();
        int cardsDealt = Integer.parseInt(message.substring(6));

        try {
            ev.engine.dealHand(ev.user, cardsDealt);   // throws EmptyShoeException if there aren't enough cards
            ev.event.getChannel()
                    .sendMessage(ev.user.getNicknameMentionTag() + " draws " + cardsDealt
                            + " cards from the shoe.");
            tellHand(ev, true);
        } catch (EmptyShoeException e) {
            ev.event.getChannel().sendMessage("There aren't enough cards left in the shoe. " +
                    "Type `!reshoe` to reset the discard pile.");
        }
    }

    public static void dealNewHand(BotEvent ev) {
        String message = ev.event.getMessageContent();
        int cardsDealt = ev.engine.game.getHandSize();

        try {
            ev.engine.dealNewHand(ev.user, cardsDealt); // throws EmptyShoeException if there aren't enough cards
        } catch (EmptyShoeException e) {
            ev.event.getChannel().sendMessage("There aren't enough cards left in the shoe. " +
                    "Type `!reshoe` to reset the discard pile.");
            return;
        }

        if (ev.engine.getPlayer(ev.user).hand.isEmpty()) {
            ev.event.getChannel()
                    .sendMessage(ev.user.getNicknameMentionTag() + " draws " + cardsDealt
                            + " new cards from the shoe.");
        } else {
            ev.event.getChannel()
                    .sendMessage(ev.user.getNicknameMentionTag() + " sets down their cards in the discard" +
                            " pile and draws " + cardsDealt + " new ones from the shoe.");
        }
        tellHand(ev, true);
    }


    public static void doBurn(BotEvent ev) {
        String message = ev.event.getMessageContent().toLowerCase();

        if (!ev.engine.isPlaying(ev.user)) {
            ev.event.getChannel().sendMessage(ev.user.getNicknameMentionTag() + " isn't playing. Come join!");
            return;
        }

        if (message.equals("!burn") || (message.length() == 7 && message.charAt(6) == '1')) {
            burnOneCard(ev);
        } else if (message.matches("^!burn \\d+$")) {
            burnCards(ev);
        } else {
            ev.event.getChannel().sendMessage("Invalid usage. Type `!help burn` for help.");
        }
    }

    private static void burnOneCard(BotEvent ev) {
        String message = ev.event.getMessageContent();

        try {
            ev.engine.burn(1);   // throws EmptyShoeException if there aren't enough cards
            ev.event.getChannel()
                    .sendMessage(ev.user.getNicknameMentionTag() + " burns a card from the top of the shoe.");
        } catch (EmptyShoeException e) {
            ev.event.getChannel().sendMessage("There aren't enough cards left in the shoe. " +
                    "Type `!reshoe` to reset the discard pile.");
        }
    }

    private static void burnCards(BotEvent ev) {
        String message = ev.event.getMessageContent();
        int cardsDealt = Integer.parseInt(message.substring(6));

        try {
            ev.engine.burn(cardsDealt);   // throws EmptyShoeException if there aren't enough cards
            ev.event.getChannel()
                    .sendMessage(ev.user.getNicknameMentionTag() + " burns " + cardsDealt
                            + " cards from the top of the shoe.");
        } catch (EmptyShoeException e) {
            ev.event.getChannel().sendMessage("There aren't enough cards left in the shoe. " +
                    "Type `!reshoe` to reset the discard pile.");
        }
    }


    public static void cardsLeft(BotEvent ev) {
        String areRemaining;
        int cardsLeft = ev.engine.cardsLeft();
        if (cardsLeft == 0) {
            areRemaining = "are no cards";
        } else if (cardsLeft == 1) {
            areRemaining = "is 1 card";
        } else {
            areRemaining = "are " + cardsLeft + " cards";
        }

        ev.event.getChannel().sendMessage("There " + areRemaining + " remaining in the shoe.");
    }
}
