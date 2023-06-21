package me.doublenico.hypegradients.api.detection;

import dev.perryplaysmc.dynamicconfigurations.utils.DynamicConfigurationDirectory;

public record ChatDetection(String configName, boolean appendMissingKeys) {

    public ChatDetection(String configName, boolean appendMissingKeys) {
        this.configName = configName;
        this.appendMissingKeys = appendMissingKeys;
        ChatDetectionManager.getInstance().addChatDetection(this);
    }

    public ChatDetectionConfiguration build(DynamicConfigurationDirectory directory) {
        return new ChatDetectionConfiguration(directory, configName, appendMissingKeys);
    }
}
