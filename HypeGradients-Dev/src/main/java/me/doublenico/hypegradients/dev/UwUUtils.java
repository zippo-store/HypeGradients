package me.doublenico.hypegradients.dev;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class UwUUtils {

    private final Map<String, String> UWU_REPLACEMENTS = new HashMap<>() {{
        put("\\. ", "~ ");
        put(", ", "~ ");
        put("- ", "~ ");
        put("\\? ", "~ ");
        put("hurt", "hUWUrt");
        put("kill", "hwuwrt");
        put("you", "you<3");
        put("r", "w");
        put("l", "w");
        put("uwu", "UWU");
        put("owo", "OWO");
        put(";-;", "(-_-)");
        put("-_-", "(-_-)");
        put(":o", "※(^o^)/※");
        put(":0", "※(^o^)/※");
        put(":\\)", "(｡◕‿‿◕｡)");
        put(":>", "(｡◕‿‿◕｡)");
        put(":\\(", "(︶︹︶)");
        put(":<", "(︶︹︶)");
        put(":3", "(・3・)");
        put(":D", "(ﾉ◕ヮ◕)ﾉ*:・ﾟ✧");
        put("\\._\\.", "(っ´ω`c)");
        put("fuck", "fwick");
        put("shit", "*poops*");
        put("wtf", "whawt the fwick");
        put("wth", "whawt the hecc");
    }};

    /**
     * This is a feature but to lazy to add it :p
     *
     * @param text The message
     * @return UWU
     */
    public String uwuify(String text) {
        String uwuText = text;

        for (Map.Entry<String, String> entry : UWU_REPLACEMENTS.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            uwuText = uwuText.replaceAll("(?i)" + key, value);
        }

        return uwuText + getRandomUwUSuffix();
    }

    private String getRandomUwUSuffix() {
        List<String> uwuSuffixes = List.of(
                "~ uwu *nuzzles you*",
                "~ owo whats this",
                "~ *kisses you*",
                "~ *blushes*",
                "~ *hehe*",
                "~ meow",
                "~ owo",
                "~ uwu",
                " ;3",
                "~ *boops your nose*",
                "~ *snuggles with you*",
                "~ *giggles*",
                "~ *hugs you*"
        );

        Random random = new Random();
        int randInt = random.nextInt(uwuSuffixes.size() + 7);
        return randInt < uwuSuffixes.size() ? uwuSuffixes.get(randInt) : "~";
    }

}
