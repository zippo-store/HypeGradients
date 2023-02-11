package me.doublenico.hypegradients.config;


import dev.perryplaysmc.dynamicconfigurations.IDynamicConfiguration;
import dev.perryplaysmc.dynamicconfigurations.utils.DynamicConfigurationDirectory;
import dev.perryplaysmc.dynamicconfigurations.utils.StringWrap;

public class ConfigManager {
    private final DynamicConfigurationDirectory configDirectory;

    private final IDynamicConfiguration config;

    public ConfigManager(DynamicConfigurationDirectory configDirectory, String configName, boolean appendMissingKeys) {
        this.configDirectory = configDirectory;
        this.config = configDirectory.createConfiguration(configName + ".yml").options().autoSave(true).indent(2).stringWrap(StringWrap.SINGLE_QUOTED).loadDefaults(true).appendMissingKeys(appendMissingKeys).configuration();
        configDirectory.addConfiguration(config);
    }

    public DynamicConfigurationDirectory getConfigDirectory() {
        return this.configDirectory;
    }

    public IDynamicConfiguration getConfig() {
        return this.config;
    }

    public void addDefault(String path, Object value) {
        this.config.setIfNotSet(path, value);
    }

    public void addInlineDefault(String path, Object value, String comment) {
        this.config.setIfNotSetInline(path, value, comment);
    }
}
