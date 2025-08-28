package me.itsglobally.circlePractice.utils;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import me.itsglobally.circlePractice.CirclePractice;

public class MessageUtil {

    private static final MiniMessage MINI = MiniMessage.miniMessage();

    public static void sendMessage(Player player, String message) {
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
    }

    public static void sendActionBar(Player player, String message) {
        Component component = MINI.deserialize(ChatColor.translateAlternateColorCodes('&', message));
        CirclePractice.audience(player).sendActionBar(component);
    }

    public static String formatMessage(String message) {
        return ChatColor.translateAlternateColorCodes('&', message);
    }
}
