package me.doublenico.hypegradients.api.detection;

public record ChatDetectionValues(
        boolean enabled, boolean chat, boolean title,
        boolean subtitle, boolean guiItem, boolean guiTitle,
        boolean scoreboardTitle, boolean scoreboardLines, boolean bossbar,
        boolean entity, boolean entityMetadata, boolean motd,
        boolean footer, boolean header, boolean playerInfo) {


}
