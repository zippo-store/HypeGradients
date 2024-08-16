package me.doublenico.hypegradients.api.event;

/**
 * The type of message packet that is being sent.
 */
public enum MessageType {

    /**
     * Bossbar message.
     */
    BOSSBAR,
    /**
     * Chat message.
     */
    CHAT,
    /**
     * Metadata of a entity, most the nametag of a player or the holograms.
     */
    METADATA,
    /**
     * Name of an entity.
     */
    ENTITY,
    /**
     * Item in a GUI or player's inventory.
     */
    GUI_ITEM,
    /**
     * Title of the GUI.
     */
    GUI_TITLE,
    /**
     * MOTD of the server.
     */
    MOTD,
    /**
     * @deprecated Use {@link #SUBTITLE_TEXT} or {@link #TITLE_TEXT} instead for better detection of the subtitle or title.
     */
    @Deprecated
    TITLE,
    /**
     * The name of a player or TAB.
     */
    PLAYER_INFO,
    /**
     * Title of a scoreboard.
     */
    SCOREBOARD_OBJECTIVE,
    /**
     * Teams are used for the scoreboard lines, nametags.
     */
    SCOREBOARD_TEAM,
    /**
     * Subtitle of a title.
     */
    SUBTITLE_TEXT,
    /**
     * Title of a title.
     */
    TITLE_TEXT,
    /**
     * Sign text, front or back.
     */
    SIGN


}
