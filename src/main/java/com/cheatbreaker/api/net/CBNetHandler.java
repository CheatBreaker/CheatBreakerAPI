/*
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

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

        channel.getPlayersListening().stream().filter(p -> p != player && !CheatBreakerAPI.getInstance().playerHasPlayerMuted(p, p)
                && !CheatBreakerAPI.getInstance().playerHasPlayerMuted(player, p)).forEach(other ->
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
