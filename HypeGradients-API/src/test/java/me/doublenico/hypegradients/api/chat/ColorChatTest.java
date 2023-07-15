package me.doublenico.hypegradients.api.chat;

import org.junit.jupiter.api.Test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ColorChatTest {

    @Test
    void testFirstChatTag() {
        String message = "<red>";
        String tag = message.replace("<", "").replace(">", "");
        assertEquals("red", tag);
    }

    @Test
    void testSecondChatTag() {
        String message = "<red>";
        String hex = "#ff0000";
        String tag = message.replace("<", "").replace(">", "").replace("red", hex);
        assertEquals("#ff0000", tag);
    }

    @Test
    void testThirdChatTag() {
        String message = "<color:red>";
        String hex = "#ff0000";
        String tag = message.replace("<color:", "").replace(">", "").replace("red", hex);
        assertEquals("#ff0000", tag);
    }

    @Test
    void testFourthChatTag() {
        String message = "<color:red>";
        String prefix = "<color:";
        String suffix = ">";
        String hex = "#ff0000";
        String tag = message.replace(prefix, "").replace(suffix, "").replace("red", hex);
        assertEquals("#ff0000", tag);
    }

    @Test
    void testFifthChatTag() {
        String message = "~red";
        String prefix = "~";
        String suffix = "";
        String hex = "#ff0000";
        String tag = message.replace(prefix, "").replace(suffix, "").replace("red", hex);
        assertEquals("#ff0000", tag);
    }

    @Test
    void testSixthChatTag() {
        String configValue = "<%tag%>";
        String message = configValue.replace("%tag%", "red");
        String prefix = message.substring(0, 1);
        String suffix = message.substring(message.length() - 1);
        String tag = message.replace(prefix, "").replace(suffix, "");
        assertEquals("red", tag);
    }

    @Test
    void testSeventhChatTag() {
        String message = "[color:%tag%]";
        String tag = "%tag%";
        String regexPattern = "(.*)\\" + tag + "(.*)";

        Pattern pattern = Pattern.compile(regexPattern);

        Matcher matcher = pattern.matcher(message);

        if (matcher.find()) {
            String prefix = matcher.group(1);
            String suffix = matcher.group(2);

            assertEquals("[color:", prefix);
            assertEquals("]", suffix);
            return;
        }
        System.out.println("No match found");
    }

    @Test
    void testEighthColorTag() {
        String message = "[%tag%]";
        String tag = "%tag%";
        String regexPattern = "(.*)" + tag + "(.*)";

        Pattern pattern = Pattern.compile(regexPattern);

        Matcher matcher = pattern.matcher(message);

        if (matcher.find()) {
            String prefix = matcher.group(1);
            String suffix = matcher.group(2);

            assertEquals("[", prefix);
            assertEquals("]", suffix);
            return;
        }
        System.out.println("No match found");
    }
}