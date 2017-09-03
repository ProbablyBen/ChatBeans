package com.ben.config;

public class Config {

    /**
     * The host
     */
    private final String _host;

    /**
     * The port
     */
    private final int _port;

    /**
     * Initializes a new instance of the Config class
     * @param host The host
     * @param port The port
     */
    public Config(String host, int port) {
        _host = host;
        _port = port;
    }

    /**
     * Gets the host
     * @return The host
     */
    public String getHost() {
        return _host;
    }

    /**
     * Gets the port
     * @return The port
     */
    public int getPort() {
        return _port;
    }

}
