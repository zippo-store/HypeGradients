package me.doublenico.hypegradients.config;

import dev.perryplaysmc.dynamicconfigurations.utils.DynamicConfigurationDirectory;
import me.doublenico.hypegradients.api.configuration.Configuration;

import java.util.ArrayList;

public class AnimationsConfig extends Configuration {
    public AnimationsConfig(DynamicConfigurationDirectory configDirectory, String configName, boolean firstTime) {
        super(configDirectory, configName, false);
        if (firstTime) {
            addDefault("name.frames", new ArrayList<String>() {{
                add("<red>RAINBOW");
                add("<orange>RAINBOW");
                add("<yellow>RAINBOW");
                add("<green>RAINBOW");
                add("<blue>RAINBOW");
                add("<indigo>RAINBOW");
                add("<violet>RAINBOW");
            }});
            getConfig().comment(
                    "Animations",
                    "Animations are used to create a gradient effect on the chat, title, subtitle, scoreboard and gui items.",
                    "Animations are made up of frames, each frame is a color or gradient",
                    "You can create your own animations, you can put any text in the frames ",
                    "",
                    "If you want to use to use it, you will need to use PlaceholderAPI and put the name of the animation in the placeholder.",
                    "Example: %hypegradients_animation_name%",
                    "You can use color tags or placeholders in the frames.",
                    "Also, you can create a custom animation that can get text from the placeholder.",
                    "Example: %hypegradients_animation_custom_Hello &l{player_name}%",
                    "Now to modify the text from the placeholder, you can use %text% in the frames, and it will be replaced by the text from the placeholder.",
                    "",
                    "Exemple of animation:",
                    "name:",
                    "  frames:",
                    "    - \"<red>RAINBOW\"",
                    "    - \"<orange>RAINBOW\"",
                    "    - \"<yellow>RAINBOW\"",
                    "    - \"<green>RAINBOW\"",
                    "    - \"<blue>RAINBOW\"",
                    "    - \"<indigo>RAINBOW\"",
                    "    - \"<violet>RAINBOW\"",
                    "",
                    "Exemple of animation with placeholder:",
                    "custom:",
                    "  frames:",
                    "    - \"<red>%text%\"",
                    "    - \"<orange>%text%\"",
                    "    - \"<yellow>%text%\"",
                    "    - \"<green>%text%\"",
                    "    - \"<blue>%text%\"",
                    "    - \"<indigo>%text%\"",
                    "    - \"<violet>%text%\"",
                    ""
            );
            addDefault("custom.frames", new ArrayList<String>() {{
                add("<red>%text%");
                add("<orange>%text%");
                add("<yellow>%text%");
                add("<green>%text%");
                add("<blue>%text%");
                add("<indigo>%text%");
                add("<violet>%text%");
            }});

        }

    }
}
