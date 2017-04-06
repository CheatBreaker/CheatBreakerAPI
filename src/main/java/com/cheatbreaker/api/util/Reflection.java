package com.cheatbreaker.api.util;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Reflection {

    private static final Class<?> craftPlayerClass;

    private static final Method getHandleMethod;
    private static final Method getProfileMethod;
    private static final Method entriesMethod;

    private static final Field delegateField;
    private static final Field propertiesField;

    static {
        try {
            craftPlayerClass = getCraftClass("entity.CraftPlayer");

            getHandleMethod = craftPlayerClass.getDeclaredMethod("getHandle");
            getHandleMethod.setAccessible(true);

            getProfileMethod = getMinecraftClass("EntityHuman").getDeclaredMethod("getProfile");
            getProfileMethod.setAccessible(true);

            propertiesField = getProfileMethod.getReturnType().getDeclaredField("properties");
            propertiesField.setAccessible(true);

            delegateField = propertiesField.getType().getDeclaredField("properties");
            delegateField.setAccessible(true);

            entriesMethod = delegateField.getType().getDeclaredMethod("entries");
            entriesMethod.setAccessible(true);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static Map<String, Collection<Object>> getPropertyMap(Player player) {
        try {
            Object craftPlayer = getHandleMethod.invoke(player);
            Object profile = getProfileMethod.invoke(craftPlayer);
            Object properties = propertiesField.get(profile);

            Set<Map.Entry<String, Collection<Object>>> entries = (Set<Map.Entry<String, Collection<Object>>>) entriesMethod.invoke(delegateField.get(properties));

            HashMap<String, Collection<Object>> map = new HashMap<>();

            for (Map.Entry<String, Collection<Object>> entry : entries) {
                map.put(entry.getKey(), entry.getValue());
            }

            return map;
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static Class<?> getMinecraftClass(String nmsClassString) throws ClassNotFoundException {
        String version = Bukkit.getServer().getClass().getPackage().getName().replace(".", ",").split(",")[3] + ".";
        String name = "net.minecraft.server." + version + nmsClassString;
        return Class.forName(name);
    }

    private static Class<?> getCraftClass(String craftClassString) throws ClassNotFoundException {
        String version = Bukkit.getServer().getClass().getPackage().getName().replace(".", ",").split(",")[3] + ".";
        String name = "org.bukkit.craftbukkit." + version + craftClassString;
        return Class.forName(name);
    }

}
