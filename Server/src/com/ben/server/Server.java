package com.ben.server;

import com.ben.logger.Logger;
import com.ben.packet.Packet;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;

public class Server {

    /**
     * The server socket
     */
    private ServerSocket _socket;

    /**
     * The client list
     */
    private final ArrayList<Client> _clients = new ArrayList<>();

    /**
     * Initializes a new instance of the Server class
     *
     * @param port The Port
     */
    public Server(int port) {
        try {
            // Create new socket and binding to the local interface and port
            _socket = new ServerSocket(port, 5);
        } catch (IOException e) {
            Logger.getInstance().writeError("Failed to initialize a new ServerSocket");
            Logger.getInstance().writeError(Arrays.toString(e.getStackTrace()));
        }
    }

    /**
     * Gets the clients
     * @return The clients
     */
    public ArrayList<Client> getClients() {
        return _clients;
    }

    /**
     * Blocks the thread and begins accepting connections
     */
    public void acceptConnections() {
        try {
            Logger.getInstance().writeInfo(String.format("Beginning to accept connections on %s port %d", _socket.getInetAddress().getHostAddress(), _socket.getLocalPort()));
            while (true) {
                // Begin accepting connections
                Socket sock = _socket.accept();

                // Create a new client instance
                Client client = new Client(this, sock);

                // Add the new client to the client list
                _clients.add(client);
                Logger.getInstance().writeInfo(String.format("Accepted a new client %s:%d", sock.getInetAddress().getHostAddress(), sock.getPort()));
            }
        } catch (IOException e) {
            Logger.getInstance().writeError("Failed to accept a connection.");
            Logger.getInstance().writeError(Arrays.toString(e.getStackTrace()));
        }
    }

    public boolean isNameTaken(String name) {
        for(Client client : getClients()){
            if(!client.getInfo().isConnected()){
                // Ignore disconnected clients
                continue;
            }

            if(client.getInfo().getUsername().trim().equalsIgnoreCase(name.trim())){
                return true;
            }
        }
        return false;
    }

    public boolean isNameReserved(String name) {
        String[] reservedNames = {
                "server",
        }; // TODO: Reserve more names

        for(String reservedName : reservedNames) {
            if(reservedName.trim().equalsIgnoreCase(name.trim())) {
                return true;
            }
        }
        return false;
    }

    public void broadcastPacket(Packet packet) {
        for(Client c : getClients()) {
            if(c.getInfo().isConnected()){
                // Only send to connected clients
                packet.send(c);
            }
        }
    }

    public String getConnectedClientList() {
        StringBuilder sb = new StringBuilder();
        int totalClients = 1;
        sb.append("\nClients online:\n");
        for(Client c : getClients()){
            if(c.getInfo().isConnected()) {
                // 1) Username \n
                sb.append(totalClients++).append(") ").append(c.getInfo().getUsername()).append("\n");
            }
        }
        return sb.toString();
    }
}
