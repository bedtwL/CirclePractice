package me.itsglobally.circlePractice.managers;

import me.itsglobally.circlePractice.CirclePractice;
import me.itsglobally.circlePractice.data.Duel;
import org.bukkit.Bukkit;

public record PluginManager(CirclePractice plugin) {
    public void reload() {
        Bukkit.broadcastMessage("plugin reload LOL");
        for (Duel d : plugin.getDuelManager().getAllDuels().values()) {
            plugin.getDuelManager().endDuel(d, d.getPlayer1());
        }
        plugin.getKitManager().loadKits();
        plugin.getFileDataManager().initialize();
    }
}
