```java
CheatBreakerApi cbApi = (CheatBreakerApi) Bukkit.getPluginManager().getPlugin("CheatBreakerAPI");
String message = ChatColor.GREEN + "You have joined the HCTeams queue.";

cbApi.sendNotificationOrFallback(
    event.getPlayer(),
    new CBNotification(message, 1, TimeUnit.SECONDS),
    () -> event.getPlayer().sendMessage(message)
);
```
