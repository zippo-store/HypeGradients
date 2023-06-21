package me.doublenico.hypegradients.api.animations;

public record AnimationHolder(AnimationCache animationCache) {

    private static int currentFrame = 0;


    public String getNextFrame(String name) {
        Animation animation = animationCache.animationHandler().getAnimation(name);
        if (animation != null) {
            if (currentFrame == animation.frames().size() - 1) currentFrame = 0;
            else currentFrame++;
            return animation.frames().get(currentFrame);
        }
        return null;
    }
}
