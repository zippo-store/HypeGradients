package me.doublenico.hypegradients.config;


import dev.perryplaysmc.dynamicconfigurations.IDynamicConfiguration;
import dev.perryplaysmc.dynamicconfigurations.utils.DynamicConfigurationDirectory;

public class ConfigManager {
    private final DynamicConfigurationDirectory configDirectory;

    private final IDynamicConfiguration config;

    public ConfigManager(DynamicConfigurationDirectory configDirectory, IDynamicConfiguration config) {
        this.configDirectory = configDirectory;
        this.config = config;
    }

    public DynamicConfigurationDirectory getConfigDirectory() {
        return this.configDirectory;
    }

    public IDynamicConfiguration getConfig() {
        return this.config;
    }

    public void addDefault(String path, Object value) {
        if (!this.config.isSet(path))
            this.config.set(path, value);
    }

    public void addDefault(String path, Object value, String comment) {
        if (!this.config.isSet(path))
            this.config.set(path, value, comment);
    }

    public void addComment(String path, String... comment) {
        if (!this.config.isSet(path))
            this.config.comment(comment);
    }

    public void addInlineDefault(String path, Object value, String comment) {
        if (!this.config.isSet(path))
            this.config.setInline(path, value, comment);
    }
}
