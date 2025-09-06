package me.itsglobally.circlePractice.dd;

import me.itsglobally.circlePractice.a;
import me.itsglobally.circlePractice.bb.b0;
import me.itsglobally.circlePractice.bb.b1;
import me.itsglobally.circlePractice.bb.b4;
import me.itsglobally.circlePractice.ff.f6;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class d3 {

    private final a plugin;
    private final Map<UUID, b1> duels;
    private final Map<UUID, UUID> duelRequests; // requester -> target
    private final Map<UUID, String> duelRequestsKit;

    public d3(a plugin) {
        this.plugin = plugin;
        this.duelRequestsKit = new HashMap<>();
        this.duels = new HashMap<>();
        this.duelRequests = new HashMap<>();
    }

    public void sendDuelRequest(Player requester, Player target, String kit) {
        UUID requesterUuid = requester.getUniqueId();
        b4 PP = plugin.getPlayerManager().getPlayer(requesterUuid);

        if (PP.getState() != b4.PlayerState.SPAWN) {
            f6.sendActionBar(requester, "&cYou are not in the spawn!");
            requester.playSound(requester.getLocation(), Sound.ENDERMAN_TELEPORT, 1.0f, 1.0f);
            duelRequests.remove(requesterUuid);
            return;
        }

        UUID targetUuid = target.getUniqueId();

        duelRequests.put(requesterUuid, targetUuid);
        duelRequestsKit.put(requesterUuid, kit);
        f6.sendMessage(requester, "&aYou sent a duel request to &e" + target.getName() + " &afor kit &e" + kit);
        f6.sendMessage(target, "&e" + requester.getName() + " &ahas sent you a duel request for kit &e" + kit);
        f6.sendMessage(target, "&aType &e/accept &ato accept the duel!");

        // Remove request after 30 seconds
        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            if (duelRequests.get(requesterUuid) != null && duelRequests.get(requesterUuid).equals(targetUuid)) {
                duelRequests.remove(requesterUuid);
                f6.sendMessage(requester, "&cYour duel request to " + target.getName() + " has expired.");
                if (target.isOnline()) {
                    f6.sendMessage(target, "&cThe duel request from " + requester.getName() + " has expired.");
                }
            }
        }, 600L); // 30 seconds
    }

    public void acceptDuel(Player accepter) {
        UUID accepterUuid = accepter.getUniqueId();
        UUID requesterUuid = null;

        // Find the requester
        for (Map.Entry<UUID, UUID> entry : duelRequests.entrySet()) {
            if (entry.getValue().equals(accepterUuid)) {
                requesterUuid = entry.getKey();
                break;
            }
        }

        if (requesterUuid == null) {
            f6.sendMessage(accepter, "&cYou don't have any pending duel requests!");
            return;
        }

        Player requester = Bukkit.getPlayer(requesterUuid);
        if (requester == null || !requester.isOnline()) {
            f6.sendMessage(accepter, "&cThe player who sent you the duel request is no longer online!");
            duelRequests.remove(requesterUuid);
            return;
        }

        b4 PP = plugin.getPlayerManager().getPlayer(requesterUuid);

        if (PP.getState() != b4.PlayerState.SPAWN) {
            f6.sendMessage(accepter, "&cThe player is not in the spawn!");
            accepter.playSound(accepter.getLocation(), Sound.ENDERMAN_TELEPORT, 1.0f, 1.0f);
            duelRequests.remove(requesterUuid);
            return;
        }

        // Remove the request
        duelRequests.remove(requesterUuid);

        // Start the duel
        startDuel(requester, accepter, duelRequestsKit.get(requesterUuid));
    }

    public void startDuel(Player player1, Player player2, String kit) {
        b4 pp1 = plugin.getPlayerManager().getPlayer(player1);
        b4 pp2 = plugin.getPlayerManager().getPlayer(player2);
        // Check if players are available
        if (pp1.getState() != b4.PlayerState.SPAWN ||
                pp2.getState() != b4.PlayerState.SPAWN) {
            f6.sendMessage(player1, "&cOne of the players is not available for a duel!");
            f6.sendMessage(player2, "&cOne of the players is not available for a duel!");
            pp1.setState(b4.PlayerState.SPAWN);
            pp2.setState(b4.PlayerState.SPAWN);
            return;
        }

        // Get available arena
        b0 b0 = plugin.getArenaManager().getAvailableArena();
        if (b0 == null) {
            f6.sendMessage(player1, "&cNo arenas are available right now!");
            f6.sendMessage(player2, "&cNo arenas are available right now!");
            pp1.setState(b4.PlayerState.SPAWN);
            pp2.setState(b4.PlayerState.SPAWN);
            return;
        }

        b1 b1 = new b1(pp1, pp2, kit, b0);
        duels.put(b1.getId(), b1);

        pp1.setState(b4.PlayerState.DUEL);
        pp2.setState(b4.PlayerState.DUEL);
        pp1.setCurrentDuel(b1);
        pp2.setCurrentDuel(b1);

        b0.setInUse(true);

        pp1.saveInventory(player1);
        pp2.saveInventory(player2);

        player1.teleport(b0.getPos1());
        player2.teleport(b0.getPos2());

        plugin.getKitManager().applyKit(player1, kit);
        plugin.getKitManager().applyKit(player2, kit);

        startCountdown(b1);
    }

    private void startCountdown(b1 b1) {
        new BukkitRunnable() {
            @Override
            public void run() {
                if (b1.getState() != me.itsglobally.circlePractice.bb.b1.DuelState.STARTING) {
                    cancel();
                    return;
                }

                Player p1 = Bukkit.getPlayer(b1.getPlayer1().getUuid());
                Player p2 = Bukkit.getPlayer(b1.getPlayer2().getUuid());


                p1.playSound(p1.getLocation(), Sound.CLICK, 1.0f, 1.0f);
                p2.playSound(p2.getLocation(), Sound.CLICK, 1.0f, 1.0f);

                if (p1 == null || p2 == null) {
                    endDuel(b1, null);
                    cancel();
                    return;
                }

                int countdown = b1.getCountdown();
                if (countdown > 0) {
                    f6.sendMessage(p1, "&eDuel starting in &c" + countdown + "&e...");
                    f6.sendMessage(p2, "&eDuel starting in &c" + countdown + "&e...");
                    b1.setCountdown(countdown - 1);
                } else {
                    f6.sendMessage(p1, "&aFight!");
                    f6.sendMessage(p2, "&aFight!");
                    p1.playSound(p1.getLocation(), Sound.FIREWORK_BLAST, 1.0f, 1.0f);
                    p2.playSound(p2.getLocation(), Sound.FIREWORK_BLAST, 1.0f, 1.0f);
                    b1.setState(me.itsglobally.circlePractice.bb.b1.DuelState.ACTIVE);
                    cancel();
                }
            }
        }.runTaskTimer(plugin, 0L, 20L);
    }

    public void endDuel(b1 b1, b4 winner) {
        b1.setState(me.itsglobally.circlePractice.bb.b1.DuelState.FINISHED);

        Player p1 = Bukkit.getPlayer(b1.getPlayer1().getUuid());
        Player p2 = Bukkit.getPlayer(b1.getPlayer2().getUuid());

        // Reset player states
        b1.getPlayer1().setState(b4.PlayerState.SPAWN);
        b1.getPlayer2().setState(b4.PlayerState.SPAWN);
        b1.getPlayer1().setCurrentDuel(null);
        b1.getPlayer2().setCurrentDuel(null);

        // Free arena
        b1.getArena().setInUse(false);

        // Teleport to spawn and restore inventories
        if (p1 != null) {
            plugin.getConfigManager().teleportToSpawn(p1);
            b1.getPlayer1().restoreInventory(p1);
        }
        if (p2 != null) {
            plugin.getConfigManager().teleportToSpawn(p2);
            b1.getPlayer2().restoreInventory(p2);
        }

        // Announce winner
        if (winner != null) {
            String winnerName = winner.getName();
            String loserName = b1.getOpponent(winner).getName();

            if (p1 != null) f6.sendMessage(p1, "&e" + winnerName + " &awon the duel!");
            if (p2 != null) f6.sendMessage(p2, "&e" + winnerName + " &awon the duel!");

            // Update stats in file storage
            boolean p1Won = winner.equals(b1.getPlayer1());

            // Reward coins for winning
            Player winnerPlayer = Bukkit.getPlayer(winner.getUuid());
            if (winnerPlayer != null) {
                plugin.getEconomyManager().rewardWin(winnerPlayer, b1.getKit());
            }

            plugin.getFileDataManager().updatePlayerStats(winner.getUuid(), b1.getKit(), true, 0);
            plugin.getFileDataManager().updatePlayerStats(b1.getOpponent(winner).getUuid(), b1.getKit(), false, 0);
        }

        // Remove duel
        duels.remove(b1.getId());
    }

    public b1 getDuel(UUID playerId) {
        for (b1 b1 : duels.values()) {
            if (b1.containsPlayer(playerId)) {
                return b1;
            }
        }
        return null;
    }

    public boolean hasPendingRequest(UUID playerId) {
        return duelRequests.containsValue(playerId);
    }

    public Map<UUID, b1> getAllDuels() {
        return duels;
    }
}