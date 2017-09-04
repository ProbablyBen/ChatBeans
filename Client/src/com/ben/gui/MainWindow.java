package com.ben.gui;

import com.ben.config.Settings;
import com.ben.logger.Logger;
import com.ben.packet.client.ClientAuthenticate;
import com.ben.packet.client.ClientGracefulDisconnect;
import com.ben.packet.client.ClientMessage;
import com.ben.packet.server.ServerMessage;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Duration;
import tray.animations.AnimationType;
import tray.notification.NotificationType;
import tray.notification.TrayNotification;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

public class MainWindow extends Application {

    /**
     * The submit button
     */
    private final Button _btnSubmit = new Button();

    /**
     * The message field
     */
    private final TextField _txtMessage = new TextField();

    /**
     * The list view to display messages
     */
    private final ListView<String> _listView = new ListView<>();

    /**
     * The GUI Manager
     */
    private final GUIManager _manager;

    /**
     * The primary stage
     */
    private Stage _primaryStage;

    /**
     * The settings
     */
    private Settings _settings = new Settings();

    /**
     * The main menu
     */
    private MenuBar _mainMenu;

    /**
     * The options menu
     */
    private Menu _optionsMenu;

    /**
     * The desktop notifications menu item
     */
    private MenuItem _desktopNotificationsItem;

    /**
     * The time stamps menu item
     */
    private MenuItem _timestampsItem;

    /**
     * The date formatter
     */
    private DateTimeFormatter _formatter = DateTimeFormatter.ofPattern("hh:mm:ss a");

    /**
     * Initializes a new instance of the MainWindow class
     *
     * @param manager The GUI Manager
     */
    public MainWindow(GUIManager manager) {
        _manager = manager;
    }

    /**
     * Starts the MainWindow GUI
     *
     * @param primaryStage The primary stage
     * @throws Exception Any GUI Exception
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        _primaryStage = primaryStage;
        _primaryStage.setTitle("Client");
        _primaryStage.setMinWidth(610);
        _primaryStage.setMinHeight(410);

        BorderPane root = new BorderPane();
        // root.setTop(addMenuBar()); Don't want menu bar at the moment
        root.setTop(addMenuBar());
        root.setCenter(addListView());
        root.setBottom(addHBox());

        Scene scene = new Scene(root, 610, 410);
        _primaryStage.setScene(scene);
        _primaryStage.show();
        if (_manager.getClient().getInfo().isConnected()) {
            displayAuthPrompt("Username", "Enter a username", "User");
        } else {
            _primaryStage.setTitle("Client - Not Connected");
        }
        _primaryStage.setOnCloseRequest(this::handleStageClose);
    }

    /**
     * Handles the close event of the stage
     *
     * @param event
     */
    private void handleStageClose(WindowEvent event) {
        new ClientGracefulDisconnect().send(_manager.getClient());
        System.exit(0);
    }

    /**
     * Decorates the HBox with various Nodes
     *
     * @return A decorated HBox
     */
    private HBox addHBox() {
        HBox hbox = new HBox();
        hbox.setPadding(new Insets(15));
        hbox.setSpacing(10);
        hbox.setStyle("-fx-background-color: #2a4b99;");

        _txtMessage.setMinSize(500, 20);
        hbox.setHgrow(_txtMessage, Priority.ALWAYS);

        _btnSubmit.setText("Submit");
        _btnSubmit.setOnAction(this::handleBtnSubmit);

        // Enable enter key on btnSubmit
        _btnSubmit.setDefaultButton(true);
        _btnSubmit.setPrefSize(100, 20);

        hbox.setAlignment(Pos.CENTER_RIGHT);

        hbox.getChildren().addAll(_txtMessage, _btnSubmit);

        return hbox;
    }

    /**
     * Displays an authentication prompt
     *
     * @param title    The prompt title
     * @param content  The prompt content
     * @param defValue The prompt default value
     */
    public void displayAuthPrompt(String title, String content, String defValue) {
        TextInputDialog dialog = new TextInputDialog(defValue);
        dialog.setTitle(title);
        dialog.setHeaderText(null);
        dialog.setContentText(content);

        Optional<String> result = dialog.showAndWait();
        if (result.isPresent()) {
            setTitle("Client - Authenticating");
            new ClientAuthenticate(result.get()).send(_manager.getClient());
        } else {
            displayAuthPrompt(title, content, defValue);
        }
    }

    /**
     * Creates a list view
     *
     * @return The list view
     */
    private ListView<String> addListView() {
        _listView.setEditable(false);
        ObservableList<String> list = FXCollections.observableArrayList();
        _listView.setPrefSize(_listView.getPrefWidth(), _listView.getPrefHeight());
        _listView.setItems(list);
        return _listView;
    }

    /**
     * Creates a menu bar
     * @return The menu bar
     */
    private MenuBar addMenuBar() {

        _mainMenu = new MenuBar();
        _mainMenu.getMenus().add(addOptionsMenu());

        return _mainMenu;
    }

    /**
     * Creates the options menu
     * @return The options menu
     */
    private Menu addOptionsMenu() {
        _optionsMenu = new Menu("Options");
        String desktopText = _settings.isDesktopNotificationsEnabled() ? "Disable Desktop Notifications" : "Enable Desktop Notifications";

        _desktopNotificationsItem = new MenuItem(desktopText);
        _desktopNotificationsItem.setOnAction(this::handleToggleDesktopNotifications);

        String timestampText = _settings.isTimestampsEnabled() ? "Disable Message Timestamps" : "Enable Message Timestamps";

        _timestampsItem = new MenuItem(timestampText);
        _timestampsItem.setOnAction(this::handleToggleTimestamps);

        _optionsMenu.getItems().add(_desktopNotificationsItem);
        _optionsMenu.getItems().add(_timestampsItem);
        return _optionsMenu;
    }

    /**
     * Handles the desktop notification item's event
     * @param event The ActionEvent
     */
    private void handleToggleDesktopNotifications(ActionEvent event) {
        // Toggle
        _settings.setDesktopNotificationsEnabled(!_settings.isDesktopNotificationsEnabled());

        Logger.getInstance().writeInfo("Toggled desktop notifications to: " + _settings.isDesktopNotificationsEnabled());

        String item = _settings.isDesktopNotificationsEnabled() ? "Disable Desktop Notifications" : "Enable Desktop Notifications";
        _desktopNotificationsItem.setText(item);
    }

    /**
     * Handles the timestamp notification's event
     * @param event
     */
    private void handleToggleTimestamps(ActionEvent event) {
        // Toggle
        _settings.setTimestampsEnabled(!_settings.isTimestampsEnabled());

        Logger.getInstance().writeInfo("Toggled message timestamps to: " + _settings.isTimestampsEnabled());

        String timestampText = _settings.isTimestampsEnabled() ? "Disable Message Timestamps" : "Enable Message Timestamps";
        _timestampsItem.setText(timestampText);
    }

    /**
     * Set's the stages title
     *
     * @param title The title
     */
    public void setTitle(String title) {
        Platform.runLater(() -> _primaryStage.setTitle(title));
    }

    /**
     * Handles the submit button's event
     *
     * @param event The ActionEvent
     */
    private void handleBtnSubmit(ActionEvent event) {
        String text = _txtMessage.getText();
        if (text.trim().isEmpty()) {
            // Don't send a blank message to server. Wastes bandwidth
            return;
        }
        // Send message
        new ClientMessage(_txtMessage.getText()).send(_manager.getClient());
        _txtMessage.setText("");
    }

    /**
     * Adds a message to the list view of messages
     *
     * @param msg The message packet
     */
    public void addMessage(ServerMessage msg) {
        String message;
        if(_settings.isTimestampsEnabled()) {
            String time = _formatter.format(LocalDateTime.now());
            message = String.format("[%-11s] <%s>: %s", time, msg.getUsername(), msg.getMessage());
        } else {
            message = String.format("<%s>: %s", msg.getUsername(), msg.getMessage());
        }

        Platform.runLater(() -> {
            if (!_primaryStage.isFocused()) {
                displayMessageNotification(msg);
            }
            _listView.getItems().add(message);
        });
    }

    /**
     * Displays a message notification
     * @param msg The message
     */
    public void displayMessageNotification(ServerMessage msg) {
        if(!_settings.isDesktopNotificationsEnabled()) {
            // Leave this method if they don't want desktop notifications
            return;
        }
        TrayNotification tray = new TrayNotification();
        tray.setTitle(msg.getUsername());
        tray.setMessage(msg.getMessage());
        tray.setNotificationType(NotificationType.MESSAGE);
        tray.setAnimationType(AnimationType.POPUP);

        tray.showAndDismiss(new Duration(150));

    }
}
