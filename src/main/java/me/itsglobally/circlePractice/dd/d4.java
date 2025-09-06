package me.itsglobally.circlePractice.dd;

import me.itsglobally.circlePractice.a;
import me.itsglobally.circlePractice.bb.b4;
import me.itsglobally.circlePractice.bb.b5;
import me.itsglobally.circlePractice.ff.f6;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

public record d4(a plugin) {

    public void joinFFA(Player p) {
        b4 pp = plugin.getPlayerManager().getPlayer(p.getUniqueId());
        if (pp.getState() != b4.PlayerState.SPAWN) {
            f6.sendActionBar(p, "&cYou are not in the spawn!");
            p.playSound(p.getLocation(), Sound.ENDERMAN_TELEPORT, 1.0f, 1.0f);
            return;
        }
        pp.setState(b4.PlayerState.FFA);
        spawn(p);
    }

    public void leaveFFA(Player p) {
        b4 pp = plugin.getPlayerManager().getPlayer(p.getUniqueId());
        if (pp.getState() != b4.PlayerState.FFA) {
            f6.sendActionBar(p, "&cYou are not in the ffa!");
            p.playSound(p.getLocation(), Sound.ENDERMAN_TELEPORT, 1.0f, 1.0f);
            return;
        }
        pp.setState(b4.PlayerState.FFA);
        plugin.getConfigManager().teleportToSpawn(p);
        pp.restoreInventory(p);
    }

    public void spawn(Player p) {
        b4 pp = plugin.getPlayerManager().getPlayer(p.getUniqueId());
        pp.saveInventory(p);
        plugin.getKitManager().applyKit(p, "FFA");

    }

    public void kill(Player vic, Player klr) {
        if (vic == klr) {
            b5.setKs(vic.getUniqueId(), 0L);
            return;
        }
        if (klr == null) return;

        b5.setLastHit(vic.getUniqueId(), null);
        if (b5.getLastHit(klr.getUniqueId()) == vic.getUniqueId()) b5.setLastHit(klr.getUniqueId(), null);

        b5.setKs(vic.getUniqueId(), 0);
        b5.addKs(klr.getUniqueId(), 1);

        plugin.getFileDataManager().updateFfaStats(klr.getUniqueId(), 1, 0);
        plugin.getFileDataManager().updateFfaStats(vic.getUniqueId(), 0, 1);

        // Reward coins for kill
        plugin.getEconomyManager().rewardKill(klr);

        f6.sendActionBar(klr, plugin.getPlayerManager().getPrefix(klr) + klr.getName() + "&rhas killed " + plugin.getPlayerManager().getPrefix(vic) + vic.getName() + "&r!");

        klr.setHealth(20.0);

        klr.playSound(klr.getLocation(), Sound.ORB_PICKUP, 1.0f, 1.0f);


        if (vic != null) {
            vic.playSound(vic.getLocation(), Sound.ORB_PICKUP, 1.0f, 1.0f);
            f6.sendActionBar(vic, plugin.getPlayerManager().getPrefix(klr) + klr.getName() + "&rhas killed " + plugin.getPlayerManager().getPrefix(vic) + vic.getName() + "&r!");
        }

        long streak = b5.getKs(klr.getUniqueId());
        if (streak >= 10 && streak % 5 == 0) {
            // Reward killstreak bonus
            plugin.getEconomyManager().rewardKillstreak(klr, streak);
            
            for (Player op : Bukkit.getOnlinePlayers()) {
                op.playSound(op.getLocation(), Sound.ENDERDRAGON_GROWL, 0.75f, 2.0f);
            }
            Bukkit.broadcastMessage(plugin.getPlayerManager().getPrefix(klr) + klr.getName() + " §ahas reached " + streak + " §akillstreaks!");
        }

    }

}
