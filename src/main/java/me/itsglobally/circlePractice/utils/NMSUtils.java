package me.itsglobally.circlePractice.utils;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

public class NMSUtils {
    private static final String VERSION;

    static {
        String name = Bukkit.getServer().getClass().getPackage().getName();
        VERSION = name.substring(name.lastIndexOf('.') + 1);
    }

    public static Class<?> getNMSClass(String className) throws ClassNotFoundException {
        return Class.forName("net.minecraft.server." + VERSION + "." + className);
    }

    public static Class<?> getCraftClass(String className) throws ClassNotFoundException {
        return Class.forName("org.bukkit.craftbukkit." + VERSION + "." + className);
    }

    public static String getVersion() {
        return VERSION;
    }

    public static void openPlayerInventory(Player player) {
        try {
            // Get CraftPlayer
            Class<?> craftPlayerClass = getCraftClass("entity.CraftPlayer");
            Object craftPlayer = craftPlayerClass.cast(player);

            // Get EntityPlayer (the handle)
            Method getHandle = craftPlayerClass.getMethod("getHandle");
            Object entityPlayer = getHandle.invoke(craftPlayer);

            // Get connection
            Object playerConnection = entityPlayer.getClass().getField("playerConnection").get(entityPlayer);

            // Build PacketPlayOutOpenWindow (MC 1.8 – 1.16)
            // Newer versions (1.17+) renamed it -> ClientboundOpenScreenPacket
            try {
                Class<?> packetClass = getNMSClass("PacketPlayOutOpenWindow");

                // Constructor: (int id, String type, IChatBaseComponent title)
                Constructor<?> constructor = packetClass.getConstructor(int.class, String.class,
                        getNMSClass("IChatBaseComponent"));

                // Build title
                Class<?> chatSerializer = getNMSClass("IChatBaseComponent$ChatSerializer");
                Method aMethod = chatSerializer.getMethod("a", String.class);
                Object title = aMethod.invoke(null, "{\"text\":\"Inventory\"}");

                // Packet (id=player.activeContainer.windowId, type=inventory, title)
                int windowId = (int) entityPlayer.getClass().getField("activeContainer").get(entityPlayer)
                        .getClass().getField("windowId").get(entityPlayer.getClass().getField("activeContainer").get(entityPlayer));

                Object packet = constructor.newInstance(windowId, "minecraft:inventory", title);

                // Send packet
                Method sendPacket = playerConnection.getClass().getMethod("sendPacket", getNMSClass("Packet"));
                sendPacket.invoke(playerConnection, packet);

            } catch (ClassNotFoundException ex) {
                // 1.17+ uses ClientboundOpenScreenPacket
                Class<?> packetClass = getNMSClass("network.protocol.game.ClientboundOpenScreenPacket");

                // Constructor(int containerId, MenuType<?> type, Component title)
                Constructor<?> constructor = packetClass.getConstructor(int.class,
                        getNMSClass("world.inventory.MenuType"),
                        getNMSClass("network.chat.Component"));

                // Use plain text component
                Class<?> chatComponent = getNMSClass("network.chat.Component");
                Method literal = chatComponent.getMethod("literal", String.class);
                Object title = literal.invoke(null, "Inventory");

                int containerId = (int) entityPlayer.getClass().getField("containerMenu").get(entityPlayer)
                        .getClass().getField("containerId").get(entityPlayer.getClass().getField("containerMenu").get(entityPlayer));

                // MenuType.GENERIC_9x4 (as example)
                Object menuType = getNMSClass("world.inventory.MenuType").getField("GENERIC_9x4").get(null);

                Object packet = constructor.newInstance(containerId, menuType, title);

                Method sendPacket = playerConnection.getClass().getMethod("sendPacket", getNMSClass("network.protocol.Packet"));
                sendPacket.invoke(playerConnection, packet);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

