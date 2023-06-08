package me.doublenico.hypegradients.placeholder;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import me.doublenico.hypegradients.HypeGradients;
import me.doublenico.hypegradients.animations.Animation;
import me.doublenico.hypegradients.animations.AnimationCache;
import me.doublenico.hypegradients.animations.AnimationHandler;
import me.doublenico.hypegradients.chat.ColorChat;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.UUID;

public class AnimationPlaceholder extends PlaceholderExpansion {

    private final HypeGradients plugin;
    private final AnimationHandler animationHandler = new AnimationHandler();
    private final HashMap<UUID, AnimationCache> animationCache = new HashMap<>();

    public AnimationPlaceholder(HypeGradients plugin) {
        this.plugin = plugin;
    }

    @Override
    public @NotNull String getIdentifier() {
        return "hypeanimations";
    }

    @Override
    public @NotNull String getAuthor() {
        return "DoubleNico";
    }

    @Override
    public @NotNull String getVersion() {
        return "1.0.0";
    }

    public boolean persist() {
        return true;
    }

    @Override
    public String onPlaceholderRequest(Player player, @NotNull String identifier) {
        // %hypeanimations_<animation>_<text>%
        String[] args = identifier.split("_");
        if (args.length < 1) return null;
        String animation = args[0];
        AnimationCache cache = getAnimationCache(player);
        Animation anim = cache.animationHandler().getAnimation(plugin, animation);
        if (anim == null) return null;
        String text = cache.animationHandler().getNextFrame(plugin, animation);
        if (args.length > 1) {
            if (!text.contains("%text%")) return "Text does not contain %text%";
            for (int i = 1; i < args.length; i++) {
                text = text.replace("%text%", args[i]);
            }
        }
        return new ColorChat(text).toFormattedString(plugin);
    }

    private AnimationCache getAnimationCache(Player player) {
        AnimationCache cache = animationCache.get(player.getUniqueId());
        if (cache == null) {
            cache = new AnimationCache(player, animationHandler);
            animationCache.put(player.getUniqueId(), cache);
        }
        return cache;
    }

    public void removeAnimationCache(Player player) {
        animationCache.remove(player.getUniqueId());
    }
}
