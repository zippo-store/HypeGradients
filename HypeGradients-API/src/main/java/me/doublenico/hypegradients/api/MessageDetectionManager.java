package me.doublenico.hypegradients.api;

import java.util.ArrayList;
import java.util.List;

public class MessageDetectionManager {
    private final List<MessageDetection> messageDetectionList = new ArrayList<>();

    private MessageDetectionManager() {
    }

    public static MessageDetectionManager getInstance() {
        return MessageDetectionManager.InstanceHolder.instance;
    }

    public List<MessageDetection> getMessageDetectionList() {
        return messageDetectionList;
    }

    public void addMessageDetection(MessageDetection messageDetection) {
        messageDetectionList.add(messageDetection);
    }

    public void removeMessageDetection(MessageDetection messageDetection) {
        messageDetectionList.remove(messageDetection);
    }

    public MessageDetection getMessageDetection(String name) {
        for (MessageDetection messageDetection : messageDetectionList) {
            if (messageDetection.getName().equals(name))
                return messageDetection;
        }
        return null;
    }

    private static final class InstanceHolder {
        private static final MessageDetectionManager instance = new MessageDetectionManager();
    }
}
