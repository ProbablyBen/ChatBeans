package com.ben.config;

public class Settings {

    private boolean _desktopNotificationsEnabled;

    private boolean _timestampsEnabled;

    public Settings() {

    }

    public boolean isDesktopNotificationsEnabled() {
        return _desktopNotificationsEnabled;
    }

    public boolean isTimestampsEnabled() {
        return _timestampsEnabled;
    }

    public void setDesktopNotificationsEnabled(boolean isEnabled) {
        _desktopNotificationsEnabled = isEnabled;
    }

    public void setTimestampsEnabled(boolean isEnabled) {
        _timestampsEnabled = isEnabled;
    }

}
