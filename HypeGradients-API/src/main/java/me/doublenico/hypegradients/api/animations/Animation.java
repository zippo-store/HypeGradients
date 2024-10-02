package me.doublenico.hypegradients.api.animations;

import java.util.Arrays;
import java.util.List;

public record Animation(String name, List<String> frames) {
    public String toString() {
        return "Animation{name=" + this.name + ", frames=" + Arrays.toString(this.frames.toArray()) + "}";
    }
}
