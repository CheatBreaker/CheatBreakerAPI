package com.cheatbreaker.api.event;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import lombok.Getter;

/**
 * Called whenever a player registers the CB plugin channel
 */
public final class PlayerRegisterCBEvent extends Event
{
    @Getter private static HandlerList handlerList = new HandlerList();

    @Getter private final Player player;

    public PlayerRegisterCBEvent(Player player) {
        this.player = player;
    }

    @Override
    public HandlerList getHandlers() {
        return handlerList;
    }

}