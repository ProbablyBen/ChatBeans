package com.ben.packet;

import com.ben.server.Client;

public abstract class Packet {

    /**
     * Sends itself to the client
     * @param client The client
     */
    public void send(Client client) {
        client.send(this);
    }
}
