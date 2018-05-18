package com.cheatbreaker.api;

import com.cheatbreaker.nethandler.client.CBPacketClientVoice;
import com.cheatbreaker.nethandler.client.CBPacketWaypointUpdate;
import com.cheatbreaker.nethandler.server.ICBNetHandlerServer;
import com.cheatbreaker.nethandler.shared.CBPacketAddWaypoint;
import com.cheatbreaker.nethandler.shared.CBPacketRemoveWaypoint;

public class CBNetHandler implements ICBNetHandlerServer
{
    @Override
    public void handleWaypointUpdate(CBPacketWaypointUpdate packet)
    {

    }

    @Override
    public void handleVoice(CBPacketClientVoice packet)
    {

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
