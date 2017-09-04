package com.ben.gui;

import com.ben.client.Client;
import com.ben.config.Config;
import com.ben.config.ConfigIO;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.io.File;

public class HostWindow extends Application {

    /**
     * The host text field
     */
    private final TextField _txtHost = new TextField();

    /**
     * The port text field
     */
    private final TextField _txtPort = new TextField();

    /**
     * The connect button
     */
    private final Button _btnConnect = new Button("Connect");

    /**
     * The save connection checkbox
     */
    private final CheckBox _chkSaveConnection = new CheckBox("Save connection");

    /**
     * The primary stage
     */
    private Stage _stage;

    /**
     * The client
     */
    private Client _client;

    /**
     * The GUI Manager
     */
    private final GUIManager _manager;

    /**
     * Initializes a new instance of the HostWindow class
     * @param manager The GUI Manager
     */
    public HostWindow(GUIManager manager) {
        _manager = manager;
    }

    /**
     * Determines whether the input is numeric
     * @param input The input to test
     * @return True if numeric, otherwise false
     */
    public static boolean isNumeric(String input) {
        // See: https://stackoverflow.com/questions/1102891/how-to-check-if-a-string-is-numeric-in-java
        return input.matches("-?\\d+(\\.\\d+)?");  //match a number with optional '-' and decimal.
    }

    /**
     * Starts HostWindow GUI
     * @param stage The primary stage
     * @throws Exception Any GUI Exception
     */
    @Override
    public void start(Stage stage) throws Exception {
        _stage = stage;
        _stage.setTitle("Host configuration");

        _btnConnect.setDefaultButton(true);
        _btnConnect.setOnAction(this::handleBtnConnect);

        GridPane root = getGridPane();
        Scene scene = new Scene(root, 310, 110);
        _stage.setScene(scene);
        _stage.show();

        _stage.setOnCloseRequest(e -> System.exit(0));
    }

    /**
     * Reads the configuration file
     * @return The configuration file
     */
    private Config readConfig() {
        File file = new File("config.json");
        return new ConfigIO().readConfig(file);
    }

    /**
     * Handles the connect button's event
     * @param event The ActionEvent
     */
    private void handleBtnConnect(ActionEvent event) {
        if (!isValidInput()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Invalid Input!");
            alert.setHeaderText(null);
            alert.setContentText("You entered invalid input.");
            alert.showAndWait();
        } else {
            if (_chkSaveConnection.isSelected()) {
                new ConfigIO().writeConfig(_txtHost.getText(), _txtPort.getText());
            }

            _client = new Client(getManager());

            // Successful connect
            if (_client.connect(_txtHost.getText(), Integer.parseInt(_txtPort.getText()))) {
                new Thread(_client).run(); // Run client packet reader.
                getManager().setClient(_client);
                try {
                    getManager().getMainWindow().start(_stage);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                // Connection failed.
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Connection failed");
                alert.setHeaderText(null);
                alert.setContentText("Failed to connect to the host.");
                alert.showAndWait();
            }
        }
    }

    /**
     * Determines whether the input submitted into _txtHost and _txtPort is valid input
     * @return True if valid input, otherwise false
     */
    private boolean isValidInput() {
        return isNumeric(_txtPort.getText());
    }

    /**
     * Determines whether a string is an IP Address
     * @param input The input to test
     * @return True if an IP Address, otherwise false
     */
    private boolean isIPAddress(String input) {
        // See: http://www.regular-expressions.info/ip.html
        return input.matches("\\b\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\b");
    }

    /**
     * Decorates a GridPane with various Nodes
     * @return A decorated GridPane
     */
    private GridPane getGridPane() {
        Config config = readConfig();
        String host;
        int port;
        if (config == null) {
            host = "";
            port = 0;
        } else {
            host = config.getHost();
            port = config.getPort();
        }
        _txtHost.setText(host);
        _txtPort.setText(Integer.toString(port));

        GridPane root = new GridPane();
        root.setHgap(3);
        root.setVgap(3);

        // Add third empty label for progress bar to fit properly
        root.addColumn(0, new Label("Host:"), new Label("Port:"), new Label());
        root.addRow(0, _txtHost);
        root.addRow(1, _txtPort);
        root.addColumn(2, _btnConnect, _chkSaveConnection);
        return root;
    }

    /**
     * Gets the GUI Manager
     * @return The GUI Manager
     */
    public GUIManager getManager() {
        return _manager;
    }
}
