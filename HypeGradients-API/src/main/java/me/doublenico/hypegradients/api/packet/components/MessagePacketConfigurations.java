package me.doublenico.hypegradients.api.packet.components;

import me.doublenico.hypegradients.api.detection.ChatDetectionConfiguration;
import me.doublenico.hypegradients.api.detection.ChatDetectionValues;

/**
 * Contains the settings and messageDetection {@link ChatDetectionValues} and the gradient config {@link ChatDetectionConfiguration}.
 * @param settings The settings values
 * @param messageDetection The message detection values
 * @param gradient The gradient configuration
 */
public record MessagePacketConfigurations(ChatDetectionValues settings, ChatDetectionValues messageDetection, ChatDetectionConfiguration gradient) {}
