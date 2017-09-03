package com.ben.packet.server;

import com.ben.packet.Packet;

import java.io.Serializable;

public class ClientAuthenticateResponse extends Packet implements Serializable {

    /**
     * Determines whether the client is authenticated
     */
    private final boolean _isAuthenticated;

    /**
     * The reason for authentication
     */
    private final String _reason;

    /**
     * The username requested
     */
    private final String _username;

    /**
     * Initializes a new instance of the ClientAuthenticateResponse class
     * @param username The username
     * @param isAuthenticated Is authenticated?
     * @param reason The reason for authentication
     */
    public ClientAuthenticateResponse(String username, boolean isAuthenticated, String reason) {
        _username = username;
        _isAuthenticated = isAuthenticated;
        _reason = reason;
    }

    /**
     * Get's whether the client is authenticated
     * @return True if authenticated, otherwise false
     */
    public boolean isAuthenticated() {
        return _isAuthenticated;
    }

    /**
     * Get's the reason for authentication
     * @return Success if successful, otherwise custom message
     */
    public String getReason() {
        return _reason;
    }

    /**
     * Get's the username
     * @return The username
     */
    public String getUsername() {
        return _username;
    }
}
