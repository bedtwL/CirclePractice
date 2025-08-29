package me.itsglobally.circlePractice.managers;

import me.itsglobally.circlePractice.CirclePractice;
import me.itsglobally.circlePractice.data.PracticePlayer;
import me.itsglobally.circlePractice.data.TempData;
import me.itsglobally.circlePractice.utils.MessageUtil;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public class FFAManager {

    private final CirclePractice plugin;

    public FFAManager(CirclePractice plugin) {
        this.plugin = plugin;
    }

    public void joinFFA(Player p) {
        PracticePlayer pp = plugin.getPlayerManager().getPlayer(p.getUniqueId());
        if (pp.getState() != PracticePlayer.PlayerState.SPAWN) {
            MessageUtil.sendActionBar(p, "&cYou are not in the spawn!");
            p.playSound(p.getLocation(), Sound.ENDERMAN_TELEPORT,1.0f, 1.0f);
            return;
        }
    }
    public void leaveFFA(Player p) {
        PracticePlayer pp = plugin.getPlayerManager().getPlayer(p.getUniqueId());
        pp.restoreInventory(p);
    }
    public void spawn(Player p) {
        PracticePlayer pp = plugin.getPlayerManager().getPlayer(p.getUniqueId());
        pp.saveInventory(p);
        p.getInventory().clear();

    }
    public void kill(Player vic, Player klr) {
        if (vic == klr) {
            TempData.setKs(vic.getUniqueId(), 0L);
            return;
        }
        if (klr == null) return;

        TempData.setLastHit(vic.getUniqueId(), null);
        if (TempData.getLastHit(klr.getUniqueId()) == vic.getUniqueId()) TempData.setLastHit(klr.getUniqueId(), null);

        TempData.setKs(vic.getUniqueId(), 0);
        TempData.addtKs(klr.getUniqueId(), 1);

        plugin.getFileDataManager().updateFfaStats(klr.getUniqueId(), 1, 0);
        plugin.getFileDataManager().updateFfaStats(vic.getUniqueId(), 0, 1);

        MessageUtil.sendActionBar(klr, plugin.getPlayerManager().getPrefix(klr) + klr.getName() + "&rhas killed " + plugin.getPlayerManager().getPrefix(vic) + vic.getName() + "&r!");

        klr.setHealth(20.0);

        klr.playSound(klr.getLocation(), Sound.ORB_PICKUP, 1.0f, 1.0f);


        if (vic != null) {
            vic.playSound(vic.getLocation(), Sound.ORB_PICKUP, 1.0f, 1.0f);
            MessageUtil.sendActionBar(vic, plugin.getPlayerManager().getPrefix(klr) + klr.getName() + "&rhas killed " + plugin.getPlayerManager().getPrefix(vic) + vic.getName() + "&r!");
        }

        long streak = TempData.getKs(klr.getUniqueId());
        if (streak >= 10 && streak % 5 == 0) {
            for (Player op : Bukkit.getOnlinePlayers()) {
                op.playSound(op.getLocation(), Sound.ENDERDRAGON_GROWL, 0.75f, 2.0f);
            }
            Bukkit.broadcastMessage(plugin.getPlayerManager().getPrefix(klr) + klr.getName() + " §ahas reached " + streak + " §akillstreaks!");
        }

    }

}
