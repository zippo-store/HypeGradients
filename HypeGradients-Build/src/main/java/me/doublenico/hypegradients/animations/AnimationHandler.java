package me.doublenico.hypegradients.animations;

import dev.perryplaysmc.dynamicconfigurations.IDynamicConfiguration;
import me.doublenico.hypegradients.HypeGradients;

import java.util.ArrayList;
import java.util.List;

public class AnimationHandler {

    private int currentFrame = 0;

    public List<Animation> getAnimations(HypeGradients plugin) {
        List<Animation> animations = new ArrayList<>();
        IDynamicConfiguration config = plugin.getAnimationsConfig().getConfig();
        config.getKeys(false).forEach(key -> {
            if (config.contains(key + ".frames")) {
                List<String> frames = config.getListString(key + ".frames");
                animations.add(new Animation(key, frames));
            }
        });
        return animations;
    }

    public Animation getAnimation(HypeGradients plugin, String name) {
        for (Animation animation : getAnimations(plugin)) {
            if (animation.name().equalsIgnoreCase(name)) {
                return animation;
            }
        }
        return null;
    }

    public String getNextFrame(HypeGradients plugin, String name) {
        Animation animation = getAnimation(plugin, name);
        if (animation != null) {
            if (currentFrame == animation.frames().size() - 1) currentFrame = 0;
            else currentFrame++;
            return animation.frames().get(currentFrame);
        }
        return null;
    }


}
