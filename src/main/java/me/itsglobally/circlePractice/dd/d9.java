package me.itsglobally.circlePractice.dd;

import me.itsglobally.circlePractice.a;
import me.itsglobally.circlePractice.bb.b1;
import org.bukkit.Bukkit;

public record d9(a plugin) {
    public void reload() {
        Bukkit.broadcastMessage("plugin reload LOL");
        for (b1 d : plugin.getDuelManager().getAllDuels().values()) {
            plugin.getDuelManager().endDuel(d, d.getPlayer1());
        }
        plugin.getKitManager().loadKits();
        plugin.getFileDataManager().initialize();
    }
}
