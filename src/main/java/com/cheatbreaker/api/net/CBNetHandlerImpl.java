package com.cheatbreaker.api.net;

import com.cheatbreaker.api.net.event.CBPacketReceivedEvent;
import com.cheatbreaker.nethandler.CBPacket;
import com.cheatbreaker.nethandler.client.CBPacketWaypointUpdate;
import com.cheatbreaker.nethandler.shared.CBPacketAddWaypoint;
import com.cheatbreaker.nethandler.shared.CBPacketRemoveWaypoint;
import com.cheatbreaker.nethandler.shared.CBPacketUpdateWaypoint;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class CBNetHandlerImpl extends CBNetHandler
{
    @Override
    public void handleWaypointUpdate(CBPacketWaypointUpdate cbPacketWaypointUpdate) {
        callEvent(cbPacketWaypointUpdate);
    }

    @Override
    public void handleAddWaypoint(CBPacketAddWaypoint cbPacketAddWaypoint) {
        callEvent(cbPacketAddWaypoint);
    }

    @Override
    public void handleRemoveWaypoint(CBPacketRemoveWaypoint cbPacketRemoveWaypoint) {
        callEvent(cbPacketRemoveWaypoint);
    }

    @Override
    public void handleWaypointUpdate(CBPacketUpdateWaypoint cbPacketUpdateWaypoint)
    {
        callEvent(cbPacketUpdateWaypoint);
    }

    private void callEvent(CBPacket packet) {
        Player player = packet.getAttachment();

        Bukkit.getPluginManager().callEvent(new CBPacketReceivedEvent(player, packet));
    }
}
