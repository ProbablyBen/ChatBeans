package com.ben.packet.server;

import com.ben.packet.Packet;

import java.io.Serializable;

public class ServerMessage extends Packet implements Serializable{

    /**
     * The username
     */
    private final String _username;

    /**
     * The chat message
     */
    private final String _message;

    /**
     * Initializes a new instance of the ServerMessage class
     * @param username The username
     * @param message The chat message
     */
    public ServerMessage(String username, String message) {
        _message = message;
        _username = username;
    }

    /**
     * Gets the username
     * @return The username
     */
    public String getUsername() {
        return _username;
    }

    /**
     * Gets the message
     * @return The message
     */
    public String getMessage() {
        return _message;
    }

}
