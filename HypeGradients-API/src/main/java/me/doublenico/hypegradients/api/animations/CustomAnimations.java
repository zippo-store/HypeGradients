package me.doublenico.hypegradients.api.animations;

import java.util.ArrayList;
import java.util.List;

public class CustomAnimations {

    private final List<Animation> customAnimations = new ArrayList<>();

    private CustomAnimations() {
    }

    public static CustomAnimations getInstance() {
        return CustomAnimations.InstanceHolder.instance;
    }

    public void createAnimation(String name, List<String> frames) {
        customAnimations.add(new Animation(name, frames));
    }

    public List<Animation> getCustomAnimations() {
        return customAnimations;
    }

    private static final class InstanceHolder {
        private static final CustomAnimations instance = new CustomAnimations();
    }

}
