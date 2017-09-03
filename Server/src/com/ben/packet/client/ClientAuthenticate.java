package com.ben.packet.client;

import com.ben.packet.Packet;

import java.io.Serializable;

public class ClientAuthenticate extends Packet implements Serializable{

    /**
     * The client username
     */
    private final String _username;

    /**
     * Initializes a new instance of the ClientAuthenticate class
     * @param username The username requested
     */
    public ClientAuthenticate(String username) {
        _username = username;
    }

    /**
     * Gets the username
     * @return The username
     */
    public String getUsername() {
        return _username;
    }


}
