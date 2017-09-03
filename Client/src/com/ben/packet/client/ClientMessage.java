package com.ben.packet.client;

import com.ben.packet.Packet;

import java.io.Serializable;

public class ClientMessage extends Packet implements Serializable {

    /**
     * The chat message
     */
    private final String _message;

    /**
     * Initializes a new instance of the ClientMessage class
     * @param message The chat message
     */
    public ClientMessage(String message) {
        _message = message;
    }

    /**
     * Gets the chat message
     * @return The chat message
     */
    public String getMessage() {
        return _message;
    }

}
