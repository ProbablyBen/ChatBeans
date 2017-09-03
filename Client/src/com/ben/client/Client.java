package com.ben.client;

import com.ben.gui.GUIManager;
import com.ben.logger.Logger;
import com.ben.packet.PacketReader;
import com.ben.packet.PacketWriter;

import java.io.IOException;
import java.net.Socket;
import java.util.Arrays;
import java.util.concurrent.Executors;

public class Client implements Runnable {

    /**
     * The socket
     */
    private Socket _socket;

    /**
     * The packet reader
     */
    private final PacketReader _reader = new PacketReader(this);

    /**
     * The packet writer
     */
    private final PacketWriter _writer = new PacketWriter(this);

    /**
     * The client information
     */
    private final ClientInfo _info = new ClientInfo();

    /**
     * The GUI Manager
     */
    private final GUIManager _manager;

    /**
     * Initializes a new instance of the Client class
     * @param manager The GUI Manager
     */
    public Client(GUIManager manager) {
        _manager = manager;
    }

    /**
     * Connects to the specified host and port
     * @param host The host
     * @param port The port
     * @return True if successfully connected, otherwise false
     */
    public boolean connect(String host, int port) {
        try {
            _socket = new Socket(host, port);
            getInfo().setIsConnected(true);
            return true;
        } catch (IOException e) {
            Logger.getInstance().writeError("Failed to initialize socket");
            Logger.getInstance().writeError(Arrays.toString(e.getStackTrace()));
            return false;
        }
    }

    /**
     * Runs the packet reader on a new thread
     */
    @Override
    public void run() {
        if (_socket == null) {
            // Socket was never initialized...
            Logger.getInstance().writeWarning("Socket was never initialized. Attempting to connect");
            return;
        }
        if (_socket.isConnected()) {
            Logger.getInstance().writeInfo("Connected");
            getInfo().setIsConnected(true);
            Executors.newSingleThreadExecutor().submit(_reader);
        }
    }

    /**
     * Gets the packet writer
     * @return The packet writer
     */
    public PacketWriter getPacketWriter() {
        return _writer;
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
     * Gets the client info
     * @return The client info
     */
    public ClientInfo getInfo() {
        return _info;
    }

    /**
     * Gets the GUI Manager
     * @return The GUI Manager
     */
    public GUIManager getManager() {
        return _manager;
    }
}
