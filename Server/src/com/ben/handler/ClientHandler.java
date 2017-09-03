package com.ben.handler;

import com.ben.logger.Logger;
import com.ben.packet.Packet;
import com.ben.server.Client;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.Arrays;

public class ClientHandler implements Runnable {

    /**
     * The client
     */
    private final Client _client;

    /**
     * Initializes a new instance of the ClientHandler class
     * @param client The client
     */
    public ClientHandler(Client client) {
        _client = client;
    }

    /**
     * Loops accepting data from the client
     */
    @Override
    public void run() {
        while (_client.getInfo().isConnected()) {
            acceptData();
        }
    }

    /**
     * Accepts data from the client's socket
     */
    private void acceptData() {
        InputStream in = getInputStream(_client.getSocket());

        if (in == null) {
            return;
        }

        try {
            // Get the object input stream
            ObjectInputStream stream = getObjectInputStream(in);

            if(stream == null) {
                return;
            }

            // Read the next packet object in the stream
            Packet packet = (Packet) stream.readObject();

            // Handle the packet
            PacketHandler.handlePacket(_client.getServer(), _client, packet);

        } catch (IOException e) {
            Logger.getInstance().writeError("Failed to read data from the client.");
            Logger.getInstance().writeError(Arrays.toString(e.getStackTrace()));
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Gets the object input stream
     * @param stream The input stream
     * @return The object input stream
     */
    private ObjectInputStream getObjectInputStream(InputStream stream) {
        try {
            return new ObjectInputStream(stream);
        } catch (IOException e) {
            _client.getInfo().setIsConnected(false);
            _client.disconnect();
            Logger.getInstance().writeError("Failed to get the client's object input stream");
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Gets the input stream
     * @param socket The socket
     * @return The input stream
     */
    private InputStream getInputStream(Socket socket) {
        try {
            return socket.getInputStream();
        } catch (IOException e) {
            _client.getInfo().setIsConnected(false);
            _client.disconnect();
            Logger.getInstance().writeError("Failed to get the client's input stream");
            Logger.getInstance().writeError(Arrays.toString(e.getStackTrace()));
        }
        return null;
    }
}
