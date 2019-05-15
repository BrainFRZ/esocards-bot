package com.github.brainfrz.bot;

import com.github.brainfrz.game.EmptyShoeException;
import com.github.brainfrz.game.Hand;
import org.javacord.api.DiscordApi;
import org.javacord.api.DiscordApiBuilder;
import org.javacord.api.entity.message.MessageAuthor;
import org.javacord.api.entity.message.MessageBuilder;
import org.javacord.api.entity.permission.PermissionType;
import org.javacord.api.entity.permission.Permissions;
import org.javacord.api.entity.permission.PermissionsBuilder;
import org.javacord.api.entity.user.User;
import org.javacord.api.event.message.MessageCreateEvent;

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
//                    && event.getMessage().getContent().equalsIgnoreCase("!eso-cards join")) {
                    && event.getMessage().getContent().equalsIgnoreCase("!join")) {
                addUser(event, event.getMessageAuthor().asUser().get(), engine);
            }
        });

        api.addMessageCreateListener(event -> {
            if (!event.getMessageAuthor().isBotUser()
//                    && event.getMessage().getContent().equalsIgnoreCase("!eso-cards leave")) {
                    && event.getMessage().getContent().equalsIgnoreCase("!leave")) {
                removeUser(event, event.getMessageAuthor().asUser().get(), engine);
            }
        });

        api.addMessageCreateListener(event -> {
            if (!event.getMessageAuthor().isBotUser()
//                    && event.getMessage().getContent().equalsIgnoreCase("!eso-cards roster")) {
                    && event.getMessage().getContent().equalsIgnoreCase("!roster")) {
                printRoster(event, event.getMessageAuthor().asUser().get(), engine);
            }
        });

        api.addMessageCreateListener(event -> {
            if (!event.getMessageAuthor().isBotUser()
//                    && event.getMessage().getContent().equalsIgnoreCase("!eso-cards hand")) {
                    && event.getMessage().getContent().equalsIgnoreCase("!hand")) {
                tellHand(event, event.getMessageAuthor().asUser().get(), engine);
            }
        });

        api.addMessageCreateListener(event -> {
            String message = event.getMessage().getContent().toLowerCase();
            if (!event.getMessageAuthor().isBotUser()
//                    && event.getMessage().getContent().equalsIgnoreCase("!eso-cards table")) {
                    && message.regionMatches(true, 0, "!table", 0, 6)) {
                doTable(event, event.getMessageAuthor().asUser().get(), engine);
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


    private static void addUser(MessageCreateEvent event, User user, BotEngine engine) {
        int oldDecks = engine.shoeSize();
        if (engine.addPlayer(user)) {
            event.getChannel().sendMessage(user.getMentionTag() + " just joined the game!");

            int newDecks = engine.shoeSize();
            if (oldDecks != newDecks) {
                event.getChannel().sendMessage("A new deck has been added to the game. " + engine.shoeSizeStr());
            }
            event.getChannel().sendMessage("There are " + engine.rosterSize() + " players.");
        } else {
            event.getChannel().sendMessage(user.getMentionTag() + " is already playing.");
        }

        System.out.println("There are " + engine.game.cardsLeft() + " cards left.");
    }

    private static void removeUser(MessageCreateEvent event, User user, BotEngine engine) {
        int oldDecks = engine.shoeSize();
        if (engine.dropPlayer(user)) {
            event.getChannel().sendMessage(user.getMentionTag() + " just left the game. Awwwww...");

            int newDecks = engine.shoeSize();
            if (oldDecks != newDecks) {
                event.getChannel().sendMessage("A deck has been removed from the game. " + engine.shoeSizeStr());
            }
        } else {
            event.getChannel().sendMessage(user.getMentionTag() + " isn't playing anyway.");
        }
    }


    private static void printRoster(MessageCreateEvent event, User user, BotEngine engine) {
        ArrayList<Player> roster = engine.roster();

        if (roster.isEmpty()) {
            event.getChannel().sendMessage("No one is playing cards right now. " +
                    "Be the first by typing `!eso-cards add`!");
            return;
        }

        MessageBuilder builder = new MessageBuilder();
        builder.append("The following Guardians are playing cards:").appendNewLine();
        for (Player player : roster) {
            builder.append("\t").append(player.USER.getMentionTag()).appendNewLine();
        }

        builder.send(event.getChannel());
    }


    private static void tellHand(MessageCreateEvent event, User user, BotEngine engine) {
        if (!engine.isPlaying(user)) {
            event.getChannel().sendMessage(user.getMentionTag() + " isn't playing. Come join!");
            return;
        }

        event.getChannel().sendMessage(user.getMentionTag() + " just checked their hand.");

        Hand hand = engine.getUserHand(user);
        if (hand.isEmpty()) {
            user.sendMessage("Your hand is empty. Deal another.");
        } else {
            user.sendMessage("You have the following hand:\n" + hand);
        }
    }


    private static void doTable(MessageCreateEvent event, User user, BotEngine engine) {
        String message = event.getMessageContent().toLowerCase();

        if (message.equals("!table")) {
            showTable(event, user, engine);
        } else if (message.matches("^!table \\d+$")) {
            dealTable(event, user, engine);
        } else if (message.equalsIgnoreCase("!table clear")) {
            clearTable(event, user, engine);
        } else {
            event.getChannel().sendMessage("Invalid usage. Type `!eso-cards help table` for help.");
        }
    }

    private static void showTable(MessageCreateEvent event, User user, BotEngine engine) {
        if (engine.getTable().isEmpty()) {
            event.getChannel().sendMessage("There are no cards on the table.");
        } else {
            event.getChannel().sendMessage("The following cards are on the table:\n"
                    + engine.getTable().tabbedString());
        }
    }

    private static void dealTable(MessageCreateEvent event, User user, BotEngine engine) {
        String message = event.getMessageContent();
        int cardsDealt = Integer.parseInt(message.substring(7));

        try {
            engine.dealTable(cardsDealt);
            event.getChannel()
                    .sendMessage(user.getMentionTag() + " deals " + cardsDealt + " cards to the table:\n"
                                    + engine.getTable().tabbedString());
        } catch (EmptyShoeException e) {
            event.getChannel().sendMessage("There aren't enough cards left in the shoe. " +
                    "Type `!eso-cards reshoe` to reset the discard pile.");
        }
    }

    public static void clearTable(MessageCreateEvent event, User user, BotEngine engine) {
        boolean cleared = engine.clearTable();
        if (cleared) {
            event.getChannel().sendMessage(user.getMentionTag() + " cleared the table.");
        } else {
            event.getChannel().sendMessage("There are no cards on the table.");
        }
    }
}
