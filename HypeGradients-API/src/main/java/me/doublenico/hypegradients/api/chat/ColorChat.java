package me.doublenico.hypegradients.api.chat;

import dev.dynamicstudios.json.data.util.CColor;
import dev.perryplaysmc.dynamicconfigurations.IDynamicConfiguration;
import dev.perryplaysmc.dynamicconfigurations.IDynamicConfigurationSection;
import me.doublenico.hypegradients.api.log.GradientLogger;
import me.doublenico.hypegradients.api.configuration.ConfigurationManager;
import org.bukkit.command.CommandSender;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
            IDynamicConfiguration tags = ConfigurationManager.getInstance().getConfiguration("tags").getConfig();
            String replaced = message.replaceAll("<" + key + ">", "#" + color);
            if (tags == null) {
                new GradientLogger("Tags configuration is null using default system!").warn();
                message = replaced;
                continue;
            }
            if (tags.getBoolean("color.useDefault", true)) {
                message = replaced;
                continue;
            }
            String tag = tags.getString("color.tag");
            if (tag == null) {
                new GradientLogger("Tag is null using default system!").warn();
                message = replaced;
                continue;
            }
            if (!tag.contains("%tag%")) {
                new GradientLogger("Tag does not contain %tag% using default system!").warn();
                message = replaced;
                continue;
            }
            String regexPattern = "(.*)%tag%(.*)";
            Pattern pattern = Pattern.compile(regexPattern);
            Matcher matcher = pattern.matcher(tag);
            if (matcher.find()) {
                String prefix = matcher.group(1);
                String suffix = matcher.group(2);
                message = message.replace(prefix + key + suffix, "#" + color);
            }
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

    public String toString() {
        return "ColorChat{message=" + message + "}";
    }
}
