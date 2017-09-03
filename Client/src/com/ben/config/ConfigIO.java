package com.ben.config;

import com.ben.logger.Logger;
import org.json.JSONObject;

import java.io.*;

public class ConfigIO {

    /**
     * Reads the config
     * @param reader The BufferedReader
     * @return The config
     */
    public Config readConfig(BufferedReader reader) {
        StringBuilder sb = new StringBuilder();
        String line;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            JSONObject obj = new JSONObject(sb.toString());
            String host = obj.getString("host");
            int port = obj.getInt("port");
            return new Config(host, port);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Reads the config
     * @param file The file
     * @return The config
     */
    public Config readConfig(File file) {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(file));
            // Read config
            return readConfig(reader);
        } catch (FileNotFoundException e) {
            Logger.getInstance().writeError("Unable to find config.json. Creating a new one");
            writeConfig();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    /**
     * Writes the config with default values
     */
    public void writeConfig() {
        writeConfig("127.0.0.1", "8443");
    }


    /**
     * Writes a configuration file named config.json with the host and port
     * @param host The host
     * @param port The port
     */
    public void writeConfig(String host, String port) {
        File newConfig = new File("config.json");
        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new FileWriter(newConfig, false));
            JSONObject obj = new JSONObject().put("host", host).put("port", port);

            writer.write(obj.toString());

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
