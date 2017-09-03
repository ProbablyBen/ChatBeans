package com.ben;

import com.ben.logger.Logger;
import com.ben.server.Server;

public class Main {

    /**
     * The server
     */
    private static Server _server;

    /**
     * The main entry point
     * @param args The arguments
     */
    public static void main(String[] args) {
        if(args.length != 1) {
            Logger.getInstance().writeWarning("You must start the server with a port argument. Example: java -jar Server.jar 8443");
        } else {
            try{
                int port = Integer.parseInt(args[0]);
                _server = new Server(port);
                new Thread(_server::acceptConnections).run();
            } catch (NumberFormatException e) {
                Logger.getInstance().writeWarning("Failed to parse port argument.");
            }
        }
    }
}
