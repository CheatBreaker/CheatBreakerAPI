package com.cheatbreaker.api.net;

import com.cheatbreaker.api.CheatBreakerAPI;
import com.cheatbreaker.api.voice.VoiceChannel;
import com.cheatbreaker.nethandler.client.CBPacketClientVoice;
import com.cheatbreaker.nethandler.client.CBPacketVoiceChannelSwitch;
import com.cheatbreaker.nethandler.client.CBPacketVoiceMute;
import com.cheatbreaker.nethandler.server.CBPacketVoice;
import com.cheatbreaker.nethandler.server.ICBNetHandlerServer;
import org.bukkit.entity.Player;

import java.util.UUID;

public abstract class CBNetHandler implements ICBNetHandlerServer
{
    @Override
    public void handleVoice(CBPacketClientVoice packet) {
        Player player = packet.getAttachment();
        VoiceChannel channel = CheatBreakerAPI.getInstance().getPlayerActiveChannels().get(player.getUniqueId());
        if (channel == null) return;

        channel.getPlayersListening().stream().filter(p -> p != player && !CheatBreakerAPI.getInstance().playerHasPlayerMuted(player, p)).forEach(other ->
                CheatBreakerAPI.getInstance().sendPacket(other, new CBPacketVoice(player.getUniqueId(), packet.getData())));
    }

    @Override
    public void handleVoiceChannelSwitch(CBPacketVoiceChannelSwitch packet) {
        Player player = packet.getAttachment();
        CheatBreakerAPI.getInstance().setActiveChannel(player, packet.getSwitchingTo());
    }

    @Override
    public void handleVoiceMute(CBPacketVoiceMute packet) {
        Player player = packet.getAttachment();
        UUID muting = packet.getMuting();

        VoiceChannel channel = CheatBreakerAPI.getInstance().getPlayerActiveChannels().get(player.getUniqueId());
        if (channel == null) return;

        CheatBreakerAPI.getInstance().toggleVoiceMute(player, muting);
    }
}
