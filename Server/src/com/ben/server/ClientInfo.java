package com.ben.server;

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
     * @return The username
     */
    public String getUsername() {
        return _username;
    }

    /**
     * Sets the username
     * @param username The username
     */
    public void setUsername(String username) {
        _username = username;
    }

    /**
     * Gets whether the client is authenticated
     * @return True if authenticated, otherwise false
     */
    public boolean isAuthenticated() {
        return _isAuthenticated;
    }

    /**
     * Sets whether the client is authenticated
     * @param isAuthenticated Is the client authenticated?
     */
    public void setIsAuthenticated(boolean isAuthenticated) {
        _isAuthenticated = isAuthenticated;
    }

    /**
     * Determines whether the client is connected
     * @return True if connected, otherwise false
     */
    public boolean isConnected() {
        return _isConnected;
    }

    public void setIsConnected(boolean isConnected) {
        _isConnected = isConnected;
    }
}
