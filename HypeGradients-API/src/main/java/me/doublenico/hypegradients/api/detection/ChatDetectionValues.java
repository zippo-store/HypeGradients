package me.doublenico.hypegradients.api.detection;

public record ChatDetectionValues(
        boolean enabled, boolean chat, boolean title,
        boolean subtitle, boolean guiItem, boolean guiTitle,
        boolean scoreboardTitle, boolean scoreboardLines, boolean bossbar,
        boolean entity, boolean entityMetadata, boolean motd,
        boolean footer, boolean header, boolean playerInfo, boolean sign) {

    @Override
    public boolean enabled() {
        return enabled;
    }

    @Override
    public boolean chat() {
        if (!enabled) return false;
        return chat;
    }

    @Override
    public boolean title() {
        if (!enabled) return false;
        return title;
    }

    @Override
    public boolean subtitle() {
        if (!enabled) return false;
        return subtitle;
    }

    @Override
    public boolean guiItem() {
        if (!enabled) return false;
        return guiItem;
    }

    @Override
    public boolean guiTitle() {
        if (!enabled) return false;
        return guiTitle;
    }

    @Override
    public boolean scoreboardTitle() {
        if (!enabled) return false;
        return scoreboardTitle;
    }

    @Override
    public boolean scoreboardLines() {
        if (!enabled) return false;

        return scoreboardLines;
    }

    @Override
    public boolean bossbar() {
        if (!enabled) return false;
        return bossbar;
    }

    @Override
    public boolean entity() {
        if (!enabled) return false;
        return entity;
    }

    @Override
    public boolean entityMetadata() {
        if (!enabled) return false;
        return entityMetadata;
    }

    @Override
    public boolean motd() {
        if (!enabled) return false;
        return motd;
    }

    @Override
    public boolean footer() {
        if (!enabled) return false;
        return footer;
    }

    @Override
    public boolean header() {
        if (!enabled) return false;
        return header;
    }

    @Override
    public boolean playerInfo() {
        if (!enabled) return false;
        return playerInfo;
    }

    @Override
    public boolean sign() {
        if (!enabled) return false;
        return sign;
    }
}
