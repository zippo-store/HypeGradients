package me.doublenico.hypegradients.api.chat;

import dev.dynamicstudios.json.data.util.CColor;
import dev.perryplaysmc.dynamicconfigurations.IDynamicConfigurationSection;
import me.doublenico.hypegradients.api.GradientLogger;
import me.doublenico.hypegradients.api.configuration.ConfigurationManager;
import org.bukkit.command.CommandSender;

public record ColorChat(String message) {

    public String replaceColors() {
        if (this.message == null)
            return new GradientLogger("Cannot replace colors ! Message cannot be null!").warn("");
        IDynamicConfigurationSection section = ConfigurationManager.getInstance().getConfiguration("colors").getConfig().getSection("colors");
        if (section == null)
            return new GradientLogger("Section colors is null!").warn("");
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

    public String toFormattedString() {
        if (new ChatGradient(message).isGradient()) return new ChatGradient(message).translateGradient();
        else return CColor.translateHex('#', replaceColors());
    }
}
