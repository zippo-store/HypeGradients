package me.doublenico.hypegradients.api;

import me.doublenico.hypegradients.api.packet.enums.DetectionPriority;
import me.doublenico.hypegradients.api.packet.annotations.Priority;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class MessageDetectionManager {
    private final List<MessageDetection> messageDetectionList = new ArrayList<>();

    private MessageDetectionManager() {
    }

    public static MessageDetectionManager getInstance() {
        return MessageDetectionManager.InstanceHolder.instance;
    }

    /**
     * Get the list of {@link MessageDetection}
     * @return the list of {@link MessageDetection}
     */
    public List<MessageDetection> getMessageDetectionList() {
        return messageDetectionList;
    }

    /**
     * Add a message detection to the list of message detections
     * @param messageDetection the custom message detection
     */
    public void addMessageDetection(MessageDetection messageDetection) {
        messageDetectionList.add(messageDetection);
        messageDetectionList.sort(Comparator.comparingInt(this::getPriority));
    }

    /**
     * Remove a message detection from the list of message detections
     * @param messageDetection the custom message detection
     */
    public void removeMessageDetection(MessageDetection messageDetection) {
        messageDetectionList.remove(messageDetection);
    }

    /**
     * Get a message detection from the list of message detections
     * @param name the name of the message detection
     * @return the message detection
     */
    public MessageDetection getMessageDetection(String name) {
        for (MessageDetection messageDetection : messageDetectionList) {
            if (messageDetection.getName().equals(name))
                return messageDetection;
        }
        return null;
    }

    private int getPriority(MessageDetection detection) {
        Priority priority = detection.getClass().getAnnotation(Priority.class);
        if (priority != null) return priority.value().getSlot();

        return DetectionPriority.NORMAL.getSlot();
    }

    private static final class InstanceHolder {
        private static final MessageDetectionManager instance = new MessageDetectionManager();
    }
}
