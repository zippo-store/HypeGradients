package me.doublenico.hypegradients.api.chat;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class ChatGradientTest {

    @Test
    void testFirstRegexCase() {
        String regex = ".+?((?:(?=#[a-fA-F\\d]{6}[;\\]].)#[a-fA-F\\d]{6}[^\\]]?)+)](.+?)\\[\\/gradient\\]";
        String test = "[gradient:#ff0000;#00ff00;#0000ff]Hello World![/gradient]";
        assertTrue(test.matches(regex));
    }

    @Test
    void testSecondRegexCase() {
        String regex = ".+?((?:(?=#[a-fA-F\\d]{6}[;\\}].)#[a-fA-F\\d]{6}[^\\}]?)+)}(.+?)\\{\\/beep\\}";
        String test = "{beep:#ff0000;#00ff00;#0000ff}Hello World!{/beep}";
        assertTrue(test.matches(regex));
    }

    @Test
    void testThirdRegexCase() {
        String prefix = "<gradient:";
        String prefixEnd = "]";
        String separator = ";";
        String suffix = "<\\/gradient>";
        String regex = prefix + "((?:(?=#[a-fA-F\\d]{6}[" + separator + "\\" + prefixEnd + "].)#[a-fA-F\\d]{6}[^\\" + prefixEnd + "]?)+)" + prefixEnd + "(.+?)" + suffix;
        String test = "<gradient:#ff0000;#00ff00;#0000ff]Hello World!</gradient>";
        assertTrue(test.matches(regex));
    }
    

}
