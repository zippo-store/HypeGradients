package me.doublenico.hypegradients.dev.util;

import com.comphenix.protocol.wrappers.AdventureComponentConverter;
import com.comphenix.protocol.wrappers.WrappedChatComponent;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;

public class AdventureComponent {

  public String convertToString(String message) {
    if (message == null) return "";
    return LegacyComponentSerializer.legacySection().serialize(AdventureComponentConverter.fromWrapper(WrappedChatComponent.fromJson(message)));
  }

  public String convertToJson(String message) {
    if (message == null) return "";
    return AdventureComponentConverter.fromComponent(LegacyComponentSerializer.legacySection().deserialize(message)).getJson();
  }
}
