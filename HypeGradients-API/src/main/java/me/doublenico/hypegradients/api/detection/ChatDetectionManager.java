package me.doublenico.hypegradients.api.detection;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChatDetectionManager {

    private final Map<String, ChatDetectionConfiguration> configManager = new HashMap<>();
    private final List<ChatDetection> chatDetections = new ArrayList<>();

    private ChatDetectionManager() {
    }

    public static ChatDetectionManager getInstance() {
        return InstanceHolder.instance;
    }

    public void addChatDetection(ChatDetection detection) {
        chatDetections.add(detection);
    }

    public List<ChatDetection> getChatDetections() {
        return chatDetections;
    }

    public void addConfiguration(String config, ChatDetectionConfiguration configuration) {
        configManager.put(config, configuration);
    }

    public void removeConfiguration(String config) {
        configManager.remove(config);
    }

    public Map<String, ChatDetectionConfiguration> getConfigurations() {
        return configManager;
    }

    public ChatDetectionConfiguration getConfiguration(String name) {
        return configManager.get(name);
    }

    private static final class InstanceHolder {
        private static final ChatDetectionManager instance = new ChatDetectionManager();
    }

}
