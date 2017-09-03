package com.ben.packet;

import com.ben.client.Client;
import com.ben.logger.Logger;
import com.ben.packet.client.ClientAuthenticate;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Arrays;

public class PacketWriter {

    /**
     * The client
     */
    private final Client _client;

    /**
     * Initializes a new instance of the PacketWriter class
     *
     * @param client The client
     */
    public PacketWriter(Client client) {
        _client = client;
    }

    /**
     * Gets the output stream
     *
     * @param socket The socket
     * @return The output stream
     */
    private OutputStream getOutputStream(Socket socket) {
        try {
            return socket.getOutputStream();
        } catch (IOException e) {
            Logger.getInstance().writeError("Failed to get output stream");
            Logger.getInstance().writeError(Arrays.toString(e.getStackTrace()));
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Serializes an object to the stream
     *
     * @param stream The Stream
     * @param packet The Packet
     */
    private void serializeObject(ObjectOutputStream stream, Packet packet) {
        try {
            stream.writeObject(packet);
        } catch (IOException e) {
            Logger.getInstance().writeError("Failed to serialize an object to the stream");
            Logger.getInstance().writeError(Arrays.toString(e.getStackTrace()));
        } finally {
            try {
                stream.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Sends a packet to the client
     *
     * @param packet The packet
     */
    public void send(Packet packet) {

        if (!_client.getInfo().isConnected()) {
            Logger.getInstance().writeWarning("Attempted to send a packet while not connected!");
            _client.getManager().getMainWindow().setTitle("Client - Disconnected");
            return;
        }

        // First check if we are authenticated
        // If we aren't authenticated, we shouldn't send
        // any other packet other than a ClientAuthenticate packet
        // because the server will auto disconnect us
        if (!_client.getInfo().isAuthenticated() && !(packet instanceof ClientAuthenticate)) {
            Logger.getInstance().writeWarning("Attempted to send a non auth packet without being authenticated. Type: " + packet.getClass());
            _client.getManager().getMainWindow().setTitle("Client - Disconnected");
        }

        OutputStream out = getOutputStream(_client.getSocket());

        if (out == null) {
            return;
        }

        ObjectOutputStream serializer = null;
        try {
            serializer = new ObjectOutputStream(out);
        } catch (IOException e) {
            Logger.getInstance().writeError("Failed to create the serializer stream");
            Logger.getInstance().writeError(Arrays.toString(e.getStackTrace()));
        }

        if (serializer == null) {
            return;
        }

        serializeObject(serializer, packet);
    }
}
