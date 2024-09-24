package me.doublenico.hypegradients.api.version;

import org.bukkit.Bukkit;

public class ServerVersion {
    public String getNMSVersion() {
        String v = Bukkit.getServer().getClass().getPackage().getName();
        return v.substring(v.lastIndexOf('.') + 1);
    }

    public boolean supportsSignature() {
        return switch (getNMSVersion()) {
            case "v1_16_R1", "v1_16_R2", "v1_16_R3", "v1_17_R1", "v1_18_R1", "v1_18_R2" -> false;
            default -> true;
        };
    }

    /**
     * This method checks if the server version supports the signature chat, the signature chat appeared in 1.19
     * @return if the server version supports the signature chat
     */
    public boolean isNewSignature() {
        if (supportsSignature()) return !getNMSVersion().equals("v1_19_R1") && !getNMSVersion().equals("v1_19_R2");
        return false;
    }

    /**
     * This method checks if the server version supports the new signature chat which is a modified version of the first one, the new signature chat appeared in 1.20
     * @return if the server version supports the new signature chat
     */
    public boolean isLegacy() {
        return switch (getNMSVersion()) {
            case "v1_16_R1", "v1_16_R2", "v1_16_R3" -> true;
            default -> false;
        };
    }
}
