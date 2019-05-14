package com.github.brainfrz.bot;

import org.javacord.api.DiscordApi;
import org.javacord.api.DiscordApiBuilder;
import org.javacord.api.entity.permission.Permissions;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

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

        // Print the invite url of your bot:
        // https://discordapp.com/oauth2/authorize?client_id=577728737391673344&scope=bot&permissions=2048
        Permissions permissions = Permissions.fromBitmask(2048,0); // Messages
        System.out.println("You can invite the bot by using the following url: " + api.createBotInvite(permissions));
    }

    private static String readTokenFile(final String filename) throws IOException {
        String token = new String(Files.readAllBytes(Paths.get(filename)), "UTF-8");
        return token;
    }
}
