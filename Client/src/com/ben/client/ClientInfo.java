package com.ben.client;

public class ClientInfo {

    /**
     * The username
     */
    private String _username;

    /**
     * Determines whether authenticated or not
     */
    private boolean _isAuthenticated;

    private boolean _isConnected;

    /**
     * Initializes a new instance of the ClientInfo class
     */
    public ClientInfo() {
        setUsername(""); // Avoid null pointers
    }

    /**
     * Gets the username
     *
     * @return The username
     */
    public String getUsername() {
        return _username;
    }

    /**
     * Sets the username
     *
     * @param username The username
     */
    public void setUsername(String username) {
        _username = username;
    }

    public boolean isAuthenticated() {
        return _isAuthenticated;
    }

    public void setIsAuthenticated(boolean isAuthenticated) {
        _isAuthenticated = isAuthenticated;
    }

    public boolean isConnected() {
        return _isConnected;
    }

    public void setIsConnected(boolean isConnected) {
        _isConnected = isConnected;
    }
}
