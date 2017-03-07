package com.cheatbreaker.api;

import com.cheatbreaker.api.message.CBMessage;
import com.cheatbreaker.api.message.SendNotificationMessage;

import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.messaging.Messenger;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Consumer;

public final class CheatBreakerApi extends JavaPlugin implements Listener {

    private static final String PLUGIN_MESSAGE_CHANNEL = "MC_CLIENT";

    @Override
    public void onEnable() {
        saveDefaultConfig();

        Messenger messenger = getServer().getMessenger();

        messenger.registerOutgoingPluginChannel(this, PLUGIN_MESSAGE_CHANNEL);
        messenger.registerIncomingPluginChannel(this, PLUGIN_MESSAGE_CHANNEL, (channel, player, bytes) -> {

        });
    }

    public boolean isUsingClient(Player player) {
        return true;
    }

    public void isBanned(UUID playerUuid, Consumer<Boolean> resultListener) {
        resultListener.accept(false);
    }

    public void sendNotification(Player player, CBNotification notification) {
        sendMessage(player, new SendNotificationMessage(notification));
    }

    public void sendNotificationOrFallback(Player player, CBNotification notification, Runnable fallback) {
        if (isUsingClient(player)) {
            sendNotification(player, notification);
        } else {
            fallback.run();
        }
    }

    private void sendMessage(Player player, CBMessage message) {
        Map<String, Object> data = new HashMap<>();
        data.put("action", message.getClass().getSimpleName());
        data.putAll(message.toMap());

        byte[] bytes;

        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            new ObjectOutputStream(out).writeObject(data);

            bytes = out.toByteArray();
        } catch (IOException ex) {
            // just rethrow whatever we catch, we should never
            // run into an IOException while writing to a byte array
            throw new RuntimeException(ex);
        }

        player.sendPluginMessage(this, PLUGIN_MESSAGE_CHANNEL, bytes);
    }

}