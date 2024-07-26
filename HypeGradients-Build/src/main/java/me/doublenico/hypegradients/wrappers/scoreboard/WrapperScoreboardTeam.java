package me.doublenico.hypegradients.wrappers.scoreboard;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.EnumWrappers;
import com.comphenix.protocol.wrappers.WrappedChatComponent;
import com.comphenix.protocol.wrappers.WrappedTeamParameters;
import me.doublenico.hypegradients.api.packet.AbstractPacket;

import java.util.Optional;

public class WrapperScoreboardTeam extends AbstractPacket {
    public static final PacketType TYPE =
        PacketType.Play.Server.SCOREBOARD_TEAM;
    public WrappedChatComponent displayName;
    public WrappedChatComponent prefix;
    public WrappedChatComponent suffix;
    public String nametagVisibility;
    public String collisionRule;
    public EnumWrappers.ChatFormatting color;
    public int options;
    public WrappedTeamParameters.Builder teamParameters;

    public WrapperScoreboardTeam() {
        super(new PacketContainer(TYPE), TYPE);
        handle.getModifier().writeDefaults();
    }

    public WrapperScoreboardTeam(PacketContainer packet) {
        super(packet, TYPE);
    }

    public Optional<WrappedTeamParameters> getTeamParameters() {
        if (!WrappedTeamParameters.isSupported()) return Optional.empty();
        if  (handle.getOptionalTeamParameters().readSafely(0).isEmpty()) return Optional.empty();
        return handle.getOptionalTeamParameters().read(0);
    }

    public void setTeamParameters(WrappedTeamParameters wrappedTeamParameters) {
        if (!WrappedTeamParameters.isSupported()) return;
        handle.getOptionalTeamParameters().write(0, Optional.of(wrappedTeamParameters));
    }

    public WrappedChatComponent getDisplayName(WrappedTeamParameters wrappedTeamParameters) {
        if (!WrappedTeamParameters.isSupported()) return null;
        return wrappedTeamParameters.getDisplayName();
    }


    public WrappedChatComponent getPrefix(WrappedTeamParameters wrappedTeamParameters) {
        if (!WrappedTeamParameters.isSupported()) return null;
        return wrappedTeamParameters.getPrefix();
    }


    public WrappedChatComponent getSuffix(WrappedTeamParameters wrappedTeamParameters) {
        if (!WrappedTeamParameters.isSupported()) return null;
        return wrappedTeamParameters.getSuffix();
    }

    public String getNametagVisibility(WrappedTeamParameters wrappedTeamParameters){
        if (!WrappedTeamParameters.isSupported()) return null;
        return wrappedTeamParameters.getNametagVisibility();
    }

    public String getCollisionRule(WrappedTeamParameters wrappedTeamParameters){
        if (!WrappedTeamParameters.isSupported()) return null;
        return wrappedTeamParameters.getCollisionRule();
    }

    public EnumWrappers.ChatFormatting getColor(WrappedTeamParameters wrappedTeamParameters){
        if (!WrappedTeamParameters.isSupported()) return null;
        return wrappedTeamParameters.getColor();
    }

    public int getOptions(WrappedTeamParameters wrappedTeamParameters, int options){
        if (!WrappedTeamParameters.isSupported()) return 0;
        return wrappedTeamParameters.getOptions();
    }

    public WrapperScoreboardTeam builder(WrappedTeamParameters wrappedTeamParameters){
        teamParameters = WrappedTeamParameters.newBuilder(wrappedTeamParameters);
        return this;
    }

    public WrapperScoreboardTeam setDisplayName(WrappedChatComponent displayName){
        teamParameters.displayName(displayName);
        return this;
    }

    public WrapperScoreboardTeam setPrefix(WrappedChatComponent prefix){
        teamParameters.prefix(prefix);
        return this;
    }

    public WrapperScoreboardTeam setSuffix(WrappedChatComponent suffix){
        teamParameters.suffix(suffix);
        return this;
    }

    public WrapperScoreboardTeam setNametagVisibility(String nametagVisibility){
        teamParameters.nametagVisibility(nametagVisibility);
        return this;
    }

    public WrapperScoreboardTeam setCollisionRule(String collisionRule){
        teamParameters.collisionRule(collisionRule);
        return this;
    }

    public WrapperScoreboardTeam setColor(EnumWrappers.ChatFormatting color){
        teamParameters.color(color);
        return this;
    }

    public WrapperScoreboardTeam setOptions(int options){
        teamParameters.options(options);
        return this;
    }


    public WrappedTeamParameters build(){
        return teamParameters.build();
    }

}