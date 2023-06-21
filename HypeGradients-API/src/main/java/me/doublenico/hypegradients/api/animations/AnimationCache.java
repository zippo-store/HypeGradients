package me.doublenico.hypegradients.api.animations;

import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public record AnimationCache(Player player, AnimationHandler animationHandler) {

    private final static Map<String, AnimationHolder> animations = new HashMap<>();

    public AnimationHolder getAnimationHolder(String animation) {
        AnimationHolder animationHolder = animations.get(animation);
        if (animationHolder == null) {
            animationHolder = new AnimationHolder(this);
            animations.put(animation, animationHolder);
        }
        return animationHolder;
    }
}
