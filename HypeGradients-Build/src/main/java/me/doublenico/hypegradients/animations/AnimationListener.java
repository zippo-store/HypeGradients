package me.doublenico.hypegradients.animations;

import me.doublenico.hypegradients.placeholder.AnimationPlaceholder;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class AnimationListener implements Listener {

    private final AnimationPlaceholder animationPlaceholder;

    public AnimationListener(AnimationPlaceholder animationPlaceholder) {
        this.animationPlaceholder = animationPlaceholder;
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        animationPlaceholder.removeAnimationCache(event.getPlayer());
    }
}
