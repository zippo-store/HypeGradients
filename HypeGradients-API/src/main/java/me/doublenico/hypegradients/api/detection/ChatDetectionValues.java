package me.doublenico.hypegradients.api.detection;

public record ChatDetectionValues(
        boolean enabled, boolean chat, boolean title,
        boolean subtitle, boolean guiItem, boolean guiTitle,
        boolean scoreboardTitle, boolean scoreboardLines, boolean bossbar,
        boolean entity, boolean entityMetadata, boolean motd,
        boolean footer, boolean header, boolean playerInfo) {

    @Override
    public boolean enabled() {
        return enabled;
    }

    @Override
    public boolean chat() {
        return chat || enabled;
    }

    @Override
    public boolean title() {
        return title || enabled;
    }

    @Override
    public boolean subtitle() {
        return subtitle || enabled;
    }

    @Override
    public boolean guiItem() {
        return guiItem || enabled;
    }

    @Override
    public boolean guiTitle() {
        return guiTitle || enabled;
    }

    @Override
    public boolean scoreboardTitle() {
        return scoreboardTitle || enabled;
    }

    @Override
    public boolean scoreboardLines() {
        return scoreboardLines || enabled;
    }

    @Override
    public boolean bossbar() {
        return bossbar || enabled;
    }

    @Override
    public boolean entity() {
        return entity || enabled;
    }

    @Override
    public boolean entityMetadata() {
        return entityMetadata || enabled;
    }

    @Override
    public boolean motd() {
        return motd || enabled;
    }

    @Override
    public boolean footer() {
        return footer || enabled;
    }

    @Override
    public boolean header() {
        return header || enabled;
    }

    @Override
    public boolean playerInfo() {
        return playerInfo || enabled;
    }
}
