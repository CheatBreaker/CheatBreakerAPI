# Bukkit-CheatBreakerAPI
The CheatBreaker API will allow you to detect when a player is running CheatBreaker, enable and disable HUD modules, allow x-ray for your staff, and much more.

# CheatBreaker Client Checks

## Running CheatBreaker Client

The CheatBreaker API allows you to check if a player is running on a CheatBreaker Client. To check if a player is using the CheatBreaker Client, use:

`CheatBreakerAPI.getInstance().isRunningCheatBreaker(Player player)`

This will return a boolean of whether or not the client says they are running CheatBreaker. This should not yet be relied on for anticheat purposes as any hacked or legitimate CheatBreaker client can initiate the PluginMessageChannel and is not the same verification method used in protected servers.

## CheatBreaker Banned

The last thing any of us want is cheaters. We're here to help stop them. To make sure somebody isn't CheatBreaker banned, use:

`CheatBreakerAPI.getInstance().isCheatBreakerBanned(UUID playerUUID)`

**Note:** Make sure that you obtain a CheatBreaker API key and that the API key is set in your config.

# Notifications, Waypoints, Cooldown Timers, and GUIs

With the CheatBreaker API, you can display Notifications, Waypoints, Cooldown Timers, and GUIs to players who are running the CheatBreaker client.

## Waypoints

### Sending Waypoints
To send a client a server waypoint you will use `CheatBreakerAPI.getInstance().getWaypointManager().sendWaypoint(Player player, Loc waypointLocation)`

### Sending Forced Waypoints
Alternatively, if you wish to send the client a waypoint that is forced and cannot be removed, use `CheatBreakerAPI.getInstance().getWaypointManager().sendForcedWaypoint(Player player, Loc waypointLocation)`

### Retrieving Server Waypoints
To retrieve server waypoints currently being displayed to a player, use `CheatBreakerAPI.getInstance().getWaypointManager().getWaypoints(Player player)`


### Deleting Server Waypoints
To retrieve server waypoints currently being displayed to a player, use `CheatBreakerAPI.getInstance().getWaypointManager().deleteWaypoint(Player player, Waypoint waypoint)`