package me.doublenico.hypegradients.chat;

import dev.dynamicstudios.json.data.util.CColor;
import dev.perryplaysmc.dynamicconfigurations.IDynamicConfigurationSection;
import me.doublenico.hypegradients.HypeGradients;
import org.bukkit.command.CommandSender;

public record ColorChat(String message) {

    public String replaceColors(HypeGradients plugin) {
        if (this.message == null)
            return "";
        IDynamicConfigurationSection section = plugin.getColorConfig().getConfig().getSection("colors");
        if (section == null)
            return "";
        String message = this.message;
        for (String key : section.getKeys(false)) {
            String color = section.getString(key);
            if (color == null)
                continue;
            message = message.replaceAll("<" + key + ">", "#" + color);
        }
        return message;
    }

    public void sendMessage(CommandSender sender) {
        if (this.message.isEmpty()) {
            sender.sendMessage("");
        } else {
            sender.sendMessage(CColor.translateCommon(this.message));
        }
    }

    public String toFormattedString(HypeGradients plugin) {
        return CColor.translateHex('#', replaceColors(plugin));
    }
}
