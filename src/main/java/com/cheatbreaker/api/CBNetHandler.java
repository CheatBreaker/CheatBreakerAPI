package com.cheatbreaker.api;

import com.cheatbreaker.nethandler.client.CBPacketClientVoice;
import com.cheatbreaker.nethandler.client.CBPacketWaypointUpdate;
import com.cheatbreaker.nethandler.server.CBPacketVoice;
import com.cheatbreaker.nethandler.server.ICBNetHandlerServer;
import com.cheatbreaker.nethandler.shared.CBPacketAddWaypoint;
import com.cheatbreaker.nethandler.shared.CBPacketRemoveWaypoint;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class CBNetHandler implements ICBNetHandlerServer
{
    @Override
    public void handleWaypointUpdate(CBPacketWaypointUpdate packet)
    {

    }

    @Override
    public void handleVoice(CBPacketClientVoice packet)
    {
        Player from = packet.getAttachment();

        for (Player ply : Bukkit.getServer().getOnlinePlayers())
        {
            CheatBreakerAPI.getInstance().sendMessage(ply, new CBPacketVoice(
                    from.getUniqueId(),
                    from.getDisplayName(),
                    packet.getData()
            ));
        }
    }

    @Override
    public void handleAddWaypoint(CBPacketAddWaypoint packet)
    {

    }

    @Override
    public void handleRemoveWaypoint(CBPacketRemoveWaypoint packet)
    {

    }
}
