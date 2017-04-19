package com.cheatbreaker.api.message;

import com.google.common.collect.ImmutableMap;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class RemoveTeammatesMessage implements CBMessage {

    private final List<UUID> uuidList = new ArrayList<>();

    public RemoveTeammatesMessage(Iterable<Player> players) {
        for (Player player :  players) {
            uuidList.add(player.getUniqueId());
        }
    }

    @Override
    public String getAction() {
        return "removeteammates";
    }

    @Override
    public Map<String, Object> toMap() {
        return ImmutableMap.of(
                "uuid-list", uuidList
        );
    }

}
