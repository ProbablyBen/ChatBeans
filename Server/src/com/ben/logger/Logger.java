package com.ben.logger;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Logger {

    /**
     * The logger instance
     */
    private static Logger _instance;

    /**
     * The time formatter
     */
    private static final DateTimeFormatter _formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm:ss");

    /**
     * Prevents the Logger class from being instantiated
     */
    private Logger() {
    }

    /**
     * Gets the singleton instance of Logger
     * @return An instance of Logger
     */
    public static Logger getInstance() {
        if (_instance == null) { // First lock check
            synchronized (Logger.class) {
                if (_instance == null) { // Second lock check
                    _instance = new Logger();
                }
            }
        }
        return _instance;
    }

    /**
     * Writes info
     * @param message The message
     */
    public void writeInfo(String message) {
        write(LogSeverity.Info, message);
    }

    /**
     * Writes a warning
     * @param message The message
     */
    public void writeWarning(String message) {
        write(LogSeverity.Warning, message);
    }

    /**
     * Writes an error
     * @param message The message
     */
    public void writeError(String message) {
        write(LogSeverity.Error, message);
    }

    /**
     * Writes to stdout a message
     * @param severity The severity
     * @param message The message
     */
    private void write(LogSeverity severity, String message) {

        String title = "";

        switch (severity) {
            case Info: {
                title = "Info";
                break;
            }
            case Warning: {
                title = "Warning";
                break;
            }
            case Error: {
                title = "Error";
                break;
            }
        }
        String time = _formatter.format(LocalDateTime.now());
        System.out.printf("[%-19s][%-7s]: %s\n", time, title, message);
    }
}

