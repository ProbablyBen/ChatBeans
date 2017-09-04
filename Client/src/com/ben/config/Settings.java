package com.ben.config;

public class Settings {

    private boolean _desktopNotificationsEnabled = true;

    private boolean _timestampsEnabled = true;

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
