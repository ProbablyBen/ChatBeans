package com.ben.server;

import com.ben.handler.ClientHandler;
import com.ben.logger.Logger;
import com.ben.packet.Packet;
import com.ben.packet.server.ServerMessage;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Arrays;
import java.util.concurrent.Executors;

public class Client {

    /**
     * The socket
     */
    private final Socket _socket;

    /**
     * The handler
     */
    private final ClientHandler _handler;

    /**
     * The client info
     */
    private final ClientInfo _info;

    /**
     * The server
     */
    private final Server _server;

    /**
     * Initializes a new instance of the Client class
     *
     * @param server The server
     * @param socket The socket
     */
    public Client(Server server, Socket socket) {
        _socket = socket;
        _handler = new ClientHandler(this);
        _info = new ClientInfo();
        _server = server;
        getInfo().setIsConnected(true);
        // Submit a new task to run the client handler
        Executors.newSingleThreadExecutor().submit(_handler);
    }

    /**
     * Gets the socket
     *
     * @return Socket
     */
    public Socket getSocket() {
        return _socket;
    }

    /**
     * Get's the client info
     *
     * @return Client info
     */
    public ClientInfo getInfo() {
        return _info;
    }

    /**
     * Get's the server
     *
     * @return The server
     */
    public Server getServer() {
        return _server;
    }

    /**
     * Sends a packet to the client
     *
     * @param packet The packet
     */
    public void send(Packet packet) {

        OutputStream out = getOutputStream();
        if (out == null) {
            disconnect();
            return;
        }

        ObjectOutputStream serializer = getObjectOutputStream(out);

        if(serializer == null) {
            disconnect();
            return;
        }

        try {
            if(_socket.isClosed()) {
                Logger.getInstance().writeWarning("Tried to send data to a closed socket!");
                disconnect();
            } else {
                serializer.writeObject(packet);
            }
        } catch (IOException e) {
            Logger.getInstance().writeError("Failed to serialize an object to the stream");
            Logger.getInstance().writeError(Arrays.toString(e.getStackTrace()));
            disconnect();
            e.printStackTrace();
        }
    }

    /**
     * Get's the output stream.
     * @return The output stream
     */
    @Nullable
    private OutputStream getOutputStream() {
        try {
            return getSocket().getOutputStream();
        } catch (IOException e) {
            Logger.getInstance().writeError("Failed to get output stream");
            Logger.getInstance().writeError(Arrays.toString(e.getStackTrace()));
            disconnect();
            return null;
        }
    }

    /**
     * Get's the object output stream
     * @param stream The output stream
     * @return The object output stream
     */
    @Nullable
    private ObjectOutputStream getObjectOutputStream(OutputStream stream) {
        try {
            return new ObjectOutputStream(stream);
        } catch (IOException e) {
            Logger.getInstance().writeError("Failed to create the serializer stream. Disconnecting.");
            Logger.getInstance().writeError(Arrays.toString(e.getStackTrace()));
            return null;
        }
    }

    /**
     * Shutdowns the socket
     */
    public void disconnect() {
        try {
            getInfo().setIsConnected(false);
            String message = String.format("%s has left the server.", getInfo().getUsername());
            _server.broadcastPacket(new ServerMessage("Server", message));
            Logger.getInstance().writeInfo(String.format("Closed %s:%d <%s>", getSocket().getInetAddress().getHostAddress(), getSocket().getPort(), getInfo().getUsername()));
            _socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
