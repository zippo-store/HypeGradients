package me.doublenico.hypegradients.api.configuration;

import java.util.HashMap;
import java.util.Map;

public class ConfigurationManager {

    private final Map<String, Configuration> configManager = new HashMap<>();

    private ConfigurationManager() {
    }

    public static ConfigurationManager getInstance() {
        return ConfigurationManager.InstanceHolder.instance;
    }

    public void addConfiguration(String config, Configuration configuration) {
        configManager.put(config, configuration);
    }

    public void removeConfiguration(String config) {
        configManager.remove(config);
    }

    public Map<String, Configuration> getConfigurations() {
        return configManager;
    }

    public Configuration getConfiguration(String name) {
        return configManager.get(name);
    }

    private static final class InstanceHolder {
        private static final ConfigurationManager instance = new ConfigurationManager();
    }


}
