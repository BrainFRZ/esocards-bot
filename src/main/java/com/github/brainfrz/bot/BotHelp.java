package com.github.brainfrz.bot;

import org.javacord.api.event.message.MessageCreateEvent;
import org.javacord.api.listener.message.MessageCreateListener;

public class BotHelp implements MessageCreateListener {
    @Override
    public void onMessageCreate(MessageCreateEvent event) {
        if (event.getMessageAuthor().isBotUser() || !event.getMessageContent()
                .regionMatches(true, 0, "!help", 0, 5)) {
            return;
        }

        String message = event.getMessageContent().toLowerCase();
        String helpMessage;
        if (message.equalsIgnoreCase("!help")) {
            generalHelp(event);
            return;
        }

        String helpTopic = message.toLowerCase().substring(6);
        switch (helpTopic) {
            case "join":        helpJoin(event); break;
            case "leave":       helpLeave(event); break;
            case "roster":      helpRoster(event); break;
            case "hand":        helpHand(event); break;
            case "show":        helpShow(event); break;
            case "table":       helpTable(event); break;
            case "play":        helpPlay(event); break;
            case "draw":        helpDraw(event); break;
            case "take":        helpTake(event); break;
            case "burn":        helpBurn(event); break;
            case "reshoe":      helpReshoe(event); break;
            case "handsize":    helpHandsize(event); break;
            default:            generalHelp(event);
        }
    }


    private static void generalHelp(MessageCreateEvent event) {
        event.getChannel().sendMessage(
                "The ESO Cards Bot is meant to duplicate playing with a physical deck of cards rather than any " +
                        "particular card game. As such, it has been designed to allow for as much flexibility as " +
                        "possible so any multi-player card game can be played. The following commands are available. All" +
                        "commands must be prefixed with `!`. Type `!help <command>` for more details on each command.\n" +
                "\t__**join**__\t\t\t\t  Allows you to join a game.\n" +
                "\t__**leave**__\t\t\t   Allows you to leave a game.\n" +
                "\t__**roster**__\t\t\t  Lists all the players who have joined.\n" +
                "\t__**hand**__\t\t\t\tSends a private message to you with your hand.\n" +
                "\t__**show**__\t\t\t\tAllows you to show one or more cards to the room\n" +
                "\t__**table**__\t\t\t\tDisplays all cards currently on the table.\n" +
                "\t__**play**__\t\t\t\t Shows a card and lets you play it to the discard pile or table.\n" +
                "\t__**draw**__\t\t\t\tDeals one or more cards to your hand from the shoe.\n" +
                "\t__**take**__\t\t\t\t Takes one or more cards from the table.\n" +
                "\t__**burn**__\t\t\t\tDiscards one or more cards from the top of the shoe.\n" +
                "\t__**reshoe**__\t\t\tAdds discard pile back to shoe and reshuffles it.\n" +
                "\t__**handsize**__\t\tSets the default hand size for future hands dealt.");
    }

    private static void helpJoin(MessageCreateEvent event) {
        event.getChannel().sendMessage(
            "**Syntax**: !join\n" +
                    "This command adds you to the roster and automatically deals you a hand. If there are too many " +
                    "players, a new deck will be added to the shoe. If that happens, a new game will be started, and " +
                    "everyone will be dealt a new hand of the set hand size."
        );
    }

    private static void helpLeave(MessageCreateEvent event) {
        event.getChannel().sendMessage(
                "**Syntax**: !leave\n" +
                        "This command removes you from the roster and starts a new game. Everyone is dealt a new " +
                        "hand automatically. If there are too few players left, one of the decks will be removed " +
                        "from the shoe."
        );
    }

    private static void helpRoster(MessageCreateEvent event) {
        event.getChannel().sendMessage(
                "**Syntax**: !roster\n" +
                        "This command lists all the players actively playing."
        );
    }

    private static void helpHand(MessageCreateEvent event) {
        event.getChannel().sendMessage(
                "**Syntax**: !hand\n" +
                        "This command sends a private message to you with your hand in an indexed list."
        );
    }

    private static void helpShow(MessageCreateEvent event) {
        event.getChannel().sendMessage(
                "**Syntax**: !show [hand]\n" +
                        "               !show <index/indices>\n" +
                        "This command will show a given card or cards from your hand to the channel. If no indices " +
                        "are given, your entire hand will be shown."
        );
    }

    private static void helpTable(MessageCreateEvent event) {
        event.getChannel().sendMessage(
                "**Syntax**: !table\n" +
                        "               !table clear\n" +
                        "               !table <number of cards>\n" +
                        "               !table last [number of cards]\n" +
                        "With no argument, this command will show all the cards on the table. If `clear` is used, " +
                        "it will put all the cards on the table into the discard pile. If a number of cards is given, " +
                        "it will deal that many cards from the shoe onto the table. If `last <number>` is given, it " +
                        "will display the most recently dealt cards on the table. This is useful for games that have " +
                        "a large stack of cards on the table. Using `last` without a number will display the last card."
        );
    }

    private static void helpPlay(MessageCreateEvent event) {
        event.getChannel().sendMessage(
                "**Syntax**: !play <index/indices> [table]\n" +
                        "This command will show the card(s) to the channel and either discard them or add them to " +
                        "the table."
        );
    }

    private static void helpDraw(MessageCreateEvent event) {
        event.getChannel().sendMessage(
                "**Syntax**: !draw new [hand]\n" +
                        "               !draw <number of cards>" +
                        "This command will either deal you a new hand or the number of cards specified. If dealing " +
                        "a new hand while there are still cards in your current hand, those cards will be added to " +
                        "the discard pile."
        );
    }

    private static void helpTake(MessageCreateEvent event) {
        event.getChannel().sendMessage(
                "**Syntax**: !take <number of cards>\n" +
                        "               !take <list of indices>" +
                        "This command takes cards from the table and adds them to your hand. If a number is given, it" +
                        " will take the most recently dealt cards. If a list of indices is given, it will take " +
                        "whichever cards are specified in the order displayed by `!table`."
        );
    }

    private static void helpBurn(MessageCreateEvent event) {
        event.getChannel().sendMessage(
                "**Syntax**: !burn [number of cards]\n" +
                        "If no argument is given, this command discards the next card from the shoe, or else the " +
                        "number of cards specified."
        );
    }

    private static void helpReshoe(MessageCreateEvent event) {
        event.getChannel().sendMessage(
                "**Syntax**: !reshoe\n" +
                        "This command takes all cards from the discard pile and adds them back to the shoe. The shoe" +
                        " is then re-shuffled."
        );
    }

    private static void helpHandsize(MessageCreateEvent event) {
        event.getChannel().sendMessage(
                "**Syntax**: !handsize [size]\n" +
                        "If no argument is given, this command will send the current starting hand size to the " +
                        "channel. If a size is given, it will change the starting size for future hands dealt."
        );
    }
}
