package me.doublenico.hypegradients.api.configuration;

import io.dynamicstudios.configurations.IDynamicConfiguration;
import io.dynamicstudios.configurations.utils.DynamicConfigurationDirectory;
import io.dynamicstudios.configurations.utils.StringWrap;

public class Configuration {
    private final DynamicConfigurationDirectory configDirectory;

    private final IDynamicConfiguration config;

    public Configuration(DynamicConfigurationDirectory configDirectory, String configName, boolean appendMissingKeys) {
        this.configDirectory = configDirectory;
        this.config = configDirectory.createConfiguration(configName + ".yml").options().autoSave(true).indent(2).stringWrap(StringWrap.SINGLE_QUOTED).loadDefaults(true).appendMissingKeys(appendMissingKeys).configuration();
        configDirectory.addConfiguration(config);
        ConfigurationManager.getInstance().addConfiguration(configName, this);
    }

    @Override
    public String toString() {
        return "Configuration{" +
            "configDirectory=" + configDirectory.name() +
            ", config=" + config.name() +
            '}';
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
