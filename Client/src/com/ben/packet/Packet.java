package com.ben.packet;

import com.ben.client.Client;

public abstract class Packet {

    /**
     * Sends itself to the client
     *
     * @param client The client
     */
    public void send(Client client) {
        client.getPacketWriter().send(this);
    }
}
