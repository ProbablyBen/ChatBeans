package com.ben.handler;

import com.ben.logger.Logger;
import com.ben.packet.Packet;
import com.ben.packet.client.ClientAuthenticate;
import com.ben.packet.client.ClientGracefulDisconnect;
import com.ben.packet.client.ClientMessage;
import com.ben.packet.server.ClientAuthenticateResponse;
import com.ben.packet.server.ServerMessage;
import com.ben.server.Client;
import com.ben.server.Server;

import java.net.Socket;

public class PacketHandler {

    /**
     * Handles a packet
     * @param server The server
     * @param client The client
     * @param packet The packet
     */
    public static void handlePacket(Server server, Client client, Packet packet) {
        if (packet == null) {
            Logger.getInstance().writeError("Received a null packet!");
            return;
        }

        // If the packet is not an authentication packet and the client is not authenticated we disconnect them.
        if(!(packet instanceof ClientAuthenticate) && !client.getInfo().isAuthenticated()) {
            client.disconnect();
        }

        if(packet instanceof ClientAuthenticate) {
            handleClientAuthenticate(server, client, (ClientAuthenticate) packet);
        }
        else if (packet instanceof ClientMessage) {
            handleClientMessage(server, client, (ClientMessage) packet);
        } else if(packet instanceof ClientGracefulDisconnect) {
            handleGracefulDisconnect(server, client, (ClientGracefulDisconnect) packet);
        } else {
            Logger.getInstance().writeWarning("Non implemented packet: " + packet.getClass());
        }
    }

    /**
     * Handles a graceful disconnect packet
     * @param server The server
     * @param client The client
     * @param packet The packet
     */
    private static void handleGracefulDisconnect(Server server, Client client, ClientGracefulDisconnect packet) {
        client.disconnect();
    }

    /**
     * Handles a client authenticate packet
     * @param server The server
     * @param client The client
     * @param packet The packet
     */
    private static void handleClientAuthenticate(Server server, Client client, ClientAuthenticate packet) {
        if(packet.getUsername().length() > 15) {
            new ClientAuthenticateResponse("", false, "Name too long. Max characters 15").send(client);
        } else if(server.isNameTaken(packet.getUsername().trim())) {
            new ClientAuthenticateResponse("", false, "Name taken").send(client);
        } else if(server.isNameReserved(packet.getUsername().trim())) {
            new ClientAuthenticateResponse("", false, "Name reserved").send(client);
        } else {
            client.getInfo().setIsAuthenticated(true);
            client.getInfo().setUsername(packet.getUsername());
            new ClientAuthenticateResponse(packet.getUsername(), true, "Success").send(client);
            // Broadcast to other clients that the client has authenticated.
            String message = String.format("%s has joined the server.", client.getInfo().getUsername());
            server.broadcastPacket(new ServerMessage("Server", message));
        }
    }

    /**
     * Handles a client message
     * @param server The server
     * @param client The client
     * @param packet The packet
     */
    private static void handleClientMessage(Server server, Client client, ClientMessage packet) {
        if(packet.getMessage().trim().isEmpty()) {
            return; // Don't put out empty messages.
        }

        Socket sock = client.getSocket(); // Makes the next line less long
        String host = String.format("%s:%d", sock.getInetAddress().getHostAddress(), sock.getPort());

        String messageFormatted = String.format("%s <%s>: %s", host, client.getInfo().getUsername(), packet.getMessage());
        Logger.getInstance().writeInfo(messageFormatted);

        // Commands start with /
        if(packet.getMessage().startsWith("/")) {
            handleCommand(client, packet.getMessage());
        } else {
            ServerMessage serverMessage = new ServerMessage(client.getInfo().getUsername(), packet.getMessage());
            server.broadcastPacket(serverMessage);
        }
    }

    /**
     * Handles a command
     * @param client The client
     * @param command The command
     */
    private static void handleCommand(Client client, String command) {

        if(command.equalsIgnoreCase("/help")) {
            new ServerMessage("Server", "\n" +
                    "Commands:\n" +
                    "1) /help - Displays help\n" +
                    "2) /ping - Pings server").send(client);
        } else if(command.equalsIgnoreCase("/ping")) {
            new ServerMessage("Server", "Pong!").send(client);
        } else {
            new ServerMessage("Server","Unknown command. Type \"/help\" for help.").send(client);
        }
    }
}
