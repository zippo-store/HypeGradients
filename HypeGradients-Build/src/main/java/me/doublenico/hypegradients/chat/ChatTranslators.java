package me.doublenico.hypegradients.chat;

import dev.dynamicstudios.json.data.util.CColor;

public class ChatTranslators {
    public ChatTranslators() {
        CColor.registerColorTranslator(string -> string.replace("[warn]", "#AF1212"));
        CColor.registerColorTranslator(string -> string.replace("[error]", "#FF0000"));
        CColor.registerColorTranslator(string -> string.replace("[info]", "#00FF00"));
        CColor.registerColorTranslator(string -> string.replace("[important]", "#511DFF"));
        CColor.registerColorTranslator(string -> string.replace("[argument]", "#7F54E7"));
        CColor.registerColorTranslator(string -> string.replace("[optional]", "#5560AF"));
        CColor.registerColorTranslator(string -> string.replace("[required]", "#231274"));
        CColor.registerColorTranslator(string -> string.replace("[description]", "#323232"));
    }
}
