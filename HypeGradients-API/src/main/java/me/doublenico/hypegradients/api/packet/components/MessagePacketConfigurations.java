package me.doublenico.hypegradients.api.packet.components;

import me.doublenico.hypegradients.api.detection.ChatDetectionConfiguration;

/**
 * Contains all the configurations for the message packets, the settings.yml, gradient.yml and messageDetection.yml, see {@link ChatDetectionConfiguration}.
 * @param settings The settings.yml configuration
 * @param gradient The gradient.yml configuration
 * @param messageDetection The messageDetection.yml configuration
 */
public record MessagePacketConfigurations(ChatDetectionConfiguration settings, ChatDetectionConfiguration gradient, ChatDetectionConfiguration messageDetection) {}
