package com.ben.packet;

import com.ben.client.Client;
import com.ben.handler.PacketHandler;
import com.ben.logger.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.Arrays;

public class PacketReader implements Runnable {

    /**
     * The client
     */
    private final Client _client;

    /**
     * Initializes a new instance of the PacketReader class
     * @param client The client
     */
    public PacketReader(Client client) {
        _client = client;
    }

    /**
     * Receives data into the underlying socket
     */
    public void receive() {

        InputStream in = getInputStream(_client.getSocket());

        if (in == null) {
            Logger.getInstance().writeWarning("Input stream was null!");
            _client.getInfo().setIsConnected(false);
            return;
        }

        ObjectInputStream objectInputStream = getObjectInputStream(in);

        if (objectInputStream == null) {
            Logger.getInstance().writeWarning("Object input stream was null!.");
            _client.getInfo().setIsConnected(false);
            return;
        }

        Packet packet = null;

        try {
            packet = (Packet) objectInputStream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        // Handle the packet
        PacketHandler.handlePacket(_client, packet);
    }

    /**
     * Get's the input stream
     * @param socket The socket
     * @return The socket's input stream
     */
    private InputStream getInputStream(Socket socket) {
        try {
            return socket.getInputStream();
        } catch (IOException e) {
            Logger.getInstance().writeError("Failed to get the server's input stream");
            Logger.getInstance().writeError(Arrays.toString(e.getStackTrace()));
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Get's the object input stream
     * @param stream The input stream
     * @return The object input stream
     */
    private ObjectInputStream getObjectInputStream(InputStream stream) {
        try {
            return new ObjectInputStream(stream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Receives data as long as the socket is connected.
     */
    @Override
    public void run() {
        while (true) {
            if (_client.getInfo().isConnected()) {
                receive();
            } else {
                break;
            }
        }
    }
}
