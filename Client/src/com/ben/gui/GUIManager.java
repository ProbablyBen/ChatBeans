package com.ben.gui;

import com.ben.client.Client;
import javafx.application.Application;
import javafx.stage.Stage;

public class GUIManager extends Application {

    /**
     * The client
     */
    private Client _client;

    /**
     * The main window
     */
    private final MainWindow _mainWindow;

    /**
     * The host window
     */
    private final HostWindow _hostWindow;

    /**
     * The primary stage
     */
    private Stage _primaryStage;

    /**
     * Initializes a new instance of the GUIManager class
     */
    public GUIManager() {
        _hostWindow = new HostWindow(this);
        _mainWindow = new MainWindow(this);
    }

    /**
     * The main entry point
     * @param args The arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

    /**
     * Starts the host window GUI
     * @param primaryStage The primary stage
     * @throws Exception Any GUI Exception
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        _primaryStage = primaryStage;

        getHostWindow().start(_primaryStage);
    }

    /**
     * Gets the client
     * @return The client
     */
    public Client getClient() {
        return _client;
    }

    /**
     * Sets the client
     * @param client The client
     */
    public void setClient(Client client) {
        _client = client;
    }

    /**
     * Gets the main window
     * @return The main window
     */
    public MainWindow getMainWindow() {
        return _mainWindow;
    }

    /**
     * Gets the host window
     * @return The host window
     */
    public HostWindow getHostWindow() {
        return _hostWindow;
    }
}
