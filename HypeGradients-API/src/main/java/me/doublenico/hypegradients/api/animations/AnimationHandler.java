package me.doublenico.hypegradients.api.animations;

import io.dynamicstudios.configurations.IDynamicConfiguration;
import me.doublenico.hypegradients.api.log.GradientLogger;
import me.doublenico.hypegradients.api.configuration.ConfigurationManager;

import java.util.ArrayList;
import java.util.List;

public class AnimationHandler {

    public List<Animation> getAnimations() {
        List<Animation> animations = new ArrayList<>();
        IDynamicConfiguration config = ConfigurationManager.getInstance().getConfiguration("animations").getConfig();
        if (config == null) return new GradientLogger("Animations configs are null!").warn(animations);
        config.getKeys(false).forEach(key -> {
            if (config.contains(key + ".frames")) {
                List<String> frames = config.getListString(key + ".frames");
                animations.add(new Animation(key, frames));
            }
        });
        return animations;
    }

    public Animation getAnimation(String name) {
        for (Animation animation : getAnimations()) {
            if (animation.name().equalsIgnoreCase(name)) {
                return animation;
            }
        }
        for (Animation animation : CustomAnimations.getInstance().getCustomAnimations()) {
            if (animation.name().equalsIgnoreCase(name)) {
                return animation;
            }
        }
        return null;
    }


}
