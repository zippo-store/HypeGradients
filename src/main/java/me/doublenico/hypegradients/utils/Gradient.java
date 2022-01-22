package me.doublenico.hypegradients.utils;

import com.google.common.base.Preconditions;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Gradient {

    private static final Map<Character, Gradient> BY_CHAR = new HashMap<>();
    private static final Map<String, Gradient> BY_NAME = new HashMap<>();

    private final String toString;
    private final String name;
    private final int ordinal;
    private final Color color;

    private Gradient(String name, String toString, int rgb) {
        this.name = name;
        this.toString = toString;
        this.ordinal = -1;
        this.color = new Color(rgb);
    }

    public String toString() { return this.toString; }


    public static boolean similarTo(Color c1, Color c2){
        double distance = (c1.getRed() - c2.getRed())*(c1.getRed() - c2.getRed()) +
                (c1.getGreen() - c2.getGreen())*(c1.getGreen() - c2.getGreen()) +
                (c1.getBlue() - c2.getBlue())*(c1.getBlue() - c2.getBlue());
        return distance <= 20;
    }

    public static List<Color> createGradient(int steps, Color start, List<Gradient> gradients) {
        List<Color> gradientList = new ArrayList<>();
        Color color1 = start;
        Color color2 = gradients.get(0).getColor();
        int index = 0;
        steps = (int) (steps/(gradients.size()/1.5));
        A:for(int i = 0; i <= steps; i++) {
            float ratio = (float) i / (float) steps;
            int green = (int) (color2.getGreen() * ratio + color1.getGreen() * (1 - ratio));
            int blue = (int) (color2.getBlue() * ratio + color1.getBlue() * (1 - ratio));
            int red = (int) (color2.getRed() * ratio + color1.getRed() * (1 - ratio));
            Color stepColor = new Color(red, green, blue);
            if(i == steps-1) {
                if(index+1 >= gradients.size()) break;
                color1 = color2;
                color2 = gradients.get(++index).getColor();
                i = 0;
                continue;
            }
            for(Color gradient : gradientList)
                if(similarTo(stepColor,gradient)) continue A;
            if(!gradientList.contains(stepColor))
                gradientList.add(stepColor);
        }
        return gradientList;
    }

    public static Gradient of(Color color) {
        return fromHex("#" + String.format("%08x", color.getRGB()).substring(2));
    }

    public static Gradient fromHex(String string) {
        Preconditions.checkArgument(string != null, "string cannot be null");
        if(string.startsWith("#") && string.length() == 7) {
            int rgb;
            try {
                rgb = Integer.parseInt(string.substring(1), 16);
            } catch (NumberFormatException var7) {
                throw new IllegalArgumentException("Illegal hex string " + string);
            }
            StringBuilder magic = new StringBuilder("§x");
            char[] chars = string.substring(1).toCharArray();
            for(char c : chars) magic.append('§').append(c);
            return new Gradient(string, magic.toString(), rgb);
        } else {
            Gradient defined = BY_NAME.get(string.toUpperCase());
            if(defined != null) return defined;
            defined = BY_CHAR.get(string.length() == 2 ? string.charAt(1) : string.charAt(0));
            if(defined != null) return defined;
            else throw new IllegalArgumentException("Could not parse CColor " + string);
        }
    }

    /**
     * @deprecated
     */
    @Deprecated
    public static Gradient[] values() {
        return BY_CHAR.values().toArray(new Gradient[BY_CHAR.values().size()]);
    }

    /**
     * @deprecated
     */
    @Deprecated
    public String name() {
        return this.getName().toUpperCase();
    }

    /**
     * @deprecated
     */
    @Deprecated
    public int ordinal() {
        Preconditions.checkArgument(this.ordinal >= 0, "Cannot get ordinal of hex color");
        return this.ordinal;
    }

    public String getName() {
        return this.name;
    }

    public Color getColor() {
        return this.color;
    }



}
