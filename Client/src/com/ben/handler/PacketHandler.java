package com.ben.handler;

import com.ben.client.Client;
import com.ben.logger.Logger;
import com.ben.packet.Packet;
import com.ben.packet.server.ClientAuthenticateResponse;
import com.ben.packet.server.ServerMessage;
import javafx.application.Platform;

public class PacketHandler {

    /**
     * Handles a packet
     * @param client The client
     * @param packet The packet
     */
    public static void handlePacket(Client client, Packet packet) {
        if (packet == null) {
            Logger.getInstance().writeError("Received a null packet!");
            // Probably unnecessary but if I ever end up adding
            // extra statements outside these if statements it will help
            return;
        } else if (packet instanceof ClientAuthenticateResponse) {
            handleClientAuthenticationResponse(client, (ClientAuthenticateResponse) packet);
        } else if (packet instanceof ServerMessage) {
            handleServerMessage(client, (ServerMessage) packet);
        } else {
            Logger.getInstance().writeWarning("Received an unknown packet! Type: " + packet.getClass());
        }
    }

    /**
     * Handles the authentication response
     * @param client The client
     * @param packet The authentication response
     */
    private static void handleClientAuthenticationResponse(Client client, ClientAuthenticateResponse packet) {
        // Set whether we are authenticated or not
        client.getInfo().setIsAuthenticated(packet.isAuthenticated());

        if (packet.isAuthenticated()) {
            client.getInfo().setUsername(packet.getUsername());
            client.getManager().getMainWindow().setTitle(String.format("Client - Authenticated as %s", packet.getUsername()));
        } else {
            Platform.runLater(() -> {
                client.getManager().getMainWindow().displayAuthPrompt("Authentication Failed. Reason: " + packet.getReason(), "Enter a username", "User");
            });
        }
    }

    /**
     * Handles the server message
     * @param client The client
     * @param packet The server message
     */
    private static void handleServerMessage(Client client, ServerMessage packet) {
        client.getManager().getMainWindow().addMessage(packet);
    }

}
