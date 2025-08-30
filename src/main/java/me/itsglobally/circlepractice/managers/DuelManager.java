package me.itsglobally.circlePractice.managers;

import me.itsglobally.circlePractice.CirclePractice;
import me.itsglobally.circlePractice.data.Arena;
import me.itsglobally.circlePractice.data.Duel;
import me.itsglobally.circlePractice.data.PracticePlayer;
import me.itsglobally.circlePractice.utils.MessageUtil;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class DuelManager {

    private final CirclePractice plugin;
    private final Map<UUID, Duel> duels;
    private final Map<UUID, UUID> duelRequests; // requester -> target
    private final Map<UUID, String> duelRequestsKit;

    public DuelManager(CirclePractice plugin) {
        this.plugin = plugin;
        this.duelRequestsKit = new HashMap<>();
        this.duels = new HashMap<>();
        this.duelRequests = new HashMap<>();
    }

    public void sendDuelRequest(Player requester, Player target, String kit) {
        UUID requesterUuid = requester.getUniqueId();
        PracticePlayer PP = plugin.getPlayerManager().getPlayer(requesterUuid);

        if (PP.getState() != PracticePlayer.PlayerState.SPAWN) {
            MessageUtil.sendActionBar(requester, "&cYou are not in the spawn!");
            requester.playSound(requester.getLocation(), Sound.ENDERMAN_TELEPORT, 1.0f, 1.0f);
            duelRequests.remove(requesterUuid);
            return;
        }

        UUID targetUuid = target.getUniqueId();

        duelRequests.put(requesterUuid, targetUuid);
        duelRequestsKit.put(requesterUuid, kit);
        MessageUtil.sendMessage(requester, "&aYou sent a duel request to &e" + target.getName() + " &afor kit &e" + kit);
        MessageUtil.sendMessage(target, "&e" + requester.getName() + " &ahas sent you a duel request for kit &e" + kit);
        MessageUtil.sendMessage(target, "&aType &e/accept &ato accept the duel!");

        // Remove request after 30 seconds
        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            if (duelRequests.get(requesterUuid) != null && duelRequests.get(requesterUuid).equals(targetUuid)) {
                duelRequests.remove(requesterUuid);
                MessageUtil.sendMessage(requester, "&cYour duel request to " + target.getName() + " has expired.");
                if (target.isOnline()) {
                    MessageUtil.sendMessage(target, "&cThe duel request from " + requester.getName() + " has expired.");
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
            MessageUtil.sendMessage(accepter, "&cYou don't have any pending duel requests!");
            return;
        }

        Player requester = Bukkit.getPlayer(requesterUuid);
        if (requester == null || !requester.isOnline()) {
            MessageUtil.sendMessage(accepter, "&cThe player who sent you the duel request is no longer online!");
            duelRequests.remove(requesterUuid);
            return;
        }

        PracticePlayer PP = plugin.getPlayerManager().getPlayer(requesterUuid);

        if (PP.getState() != PracticePlayer.PlayerState.SPAWN) {
            MessageUtil.sendMessage(accepter, "&cThe player is not in the spawn!");
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
        PracticePlayer pp1 = plugin.getPlayerManager().getPlayer(player1);
        PracticePlayer pp2 = plugin.getPlayerManager().getPlayer(player2);
        // Check if players are available
        if (pp1.getState() != PracticePlayer.PlayerState.SPAWN ||
                pp2.getState() != PracticePlayer.PlayerState.SPAWN) {
            MessageUtil.sendMessage(player1, "&cOne of the players is not available for a duel!");
            MessageUtil.sendMessage(player2, "&cOne of the players is not available for a duel!");
            pp1.setState(PracticePlayer.PlayerState.SPAWN);
            pp2.setState(PracticePlayer.PlayerState.SPAWN);
            return;
        }

        // Get available arena
        Arena arena = plugin.getArenaManager().getAvailableArena();
        if (arena == null) {
            MessageUtil.sendMessage(player1, "&cNo arenas are available right now!");
            MessageUtil.sendMessage(player2, "&cNo arenas are available right now!");
            pp1.setState(PracticePlayer.PlayerState.SPAWN);
            pp2.setState(PracticePlayer.PlayerState.SPAWN);
            return;
        }

        // Create duel
        Duel duel = new Duel(pp1, pp2, kit, arena);
        duels.put(duel.getId(), duel);

        // Set player states
        pp1.setState(PracticePlayer.PlayerState.DUEL);
        pp2.setState(PracticePlayer.PlayerState.DUEL);
        pp1.setCurrentDuel(duel);
        pp2.setCurrentDuel(duel);

        // Mark arena as in use
        arena.setInUse(true);

        // Save inventories
        pp1.saveInventory(player1);
        pp2.saveInventory(player2);

        // Teleport players
        player1.teleport(arena.getPos1());
        player2.teleport(arena.getPos2());

        // Apply kit
        plugin.getKitManager().applyKit(player1, kit);
        plugin.getKitManager().applyKit(player2, kit);

        /*ItemStack[] saved1 = pp1.getKitContents(kit);

        if (saved1 == null) {
            String serialized = plugin.getFileDataManager().getKitContents(player1.getUniqueId(), kit);
            if (serialized != null) {
                ItemStack[][] deserialized = me.itsglobally.circlePractice.utils.InventorySerializer.deserializeInventory(serialized);
                if (deserialized != null) {
                    player1.getInventory().setContents(deserialized[0]);
                    player1.getInventory().setArmorContents(deserialized[1]);
                } else {
                    plugin.getKitManager().applyKit(player1, kit);
                }
            } else {
                plugin.getKitManager().applyKit(player1, kit);
            }
        } else {
            player1.getInventory().setContents(java.util.Arrays.copyOf(saved1, 36));
            player1.getInventory().setArmorContents(java.util.Arrays.copyOfRange(saved1, 36, 40));
        }

        ItemStack[] saved2 = pp1.getKitContents(kit);

        if (saved2 == null) {
            String serialized = plugin.getFileDataManager().getKitContents(player2.getUniqueId(), kit);
            if (serialized != null) {
                ItemStack[][] deserialized = me.itsglobally.circlePractice.utils.InventorySerializer.deserializeInventory(serialized);
                if (deserialized != null) {
                    player2.getInventory().setContents(deserialized[0]);
                    player2.getInventory().setArmorContents(deserialized[1]);
                } else {
                    plugin.getKitManager().applyKit(player2, kit);
                }
            } else {
                plugin.getKitManager().applyKit(player2, kit);
            }
        } else {
            player2.getInventory().setContents(java.util.Arrays.copyOf(saved2, 36));
            player2.getInventory().setArmorContents(java.util.Arrays.copyOfRange(saved2, 36, 40));
        }*/


        // Start countdown
        startCountdown(duel);
    }

    private void startCountdown(Duel duel) {
        new BukkitRunnable() {
            @Override
            public void run() {
                if (duel.getState() != Duel.DuelState.STARTING) {
                    cancel();
                    return;
                }

                Player p1 = Bukkit.getPlayer(duel.getPlayer1().getUuid());
                Player p2 = Bukkit.getPlayer(duel.getPlayer2().getUuid());


                p1.playSound(p1.getLocation(), Sound.CLICK, 1.0f, 1.0f);
                p2.playSound(p2.getLocation(), Sound.CLICK, 1.0f, 1.0f);

                if (p1 == null || p2 == null) {
                    endDuel(duel, null);
                    cancel();
                    return;
                }

                int countdown = duel.getCountdown();
                if (countdown > 0) {
                    MessageUtil.sendMessage(p1, "&eDuel starting in &c" + countdown + "&e...");
                    MessageUtil.sendMessage(p2, "&eDuel starting in &c" + countdown + "&e...");
                    duel.setCountdown(countdown - 1);
                } else {
                    MessageUtil.sendMessage(p1, "&aFight!");
                    MessageUtil.sendMessage(p2, "&aFight!");
                    p1.playSound(p1.getLocation(), Sound.FIREWORK_BLAST, 1.0f, 1.0f);
                    p2.playSound(p2.getLocation(), Sound.FIREWORK_BLAST, 1.0f, 1.0f);
                    duel.setState(Duel.DuelState.ACTIVE);
                    cancel();
                }
            }
        }.runTaskTimer(plugin, 0L, 20L);
    }

    public void endDuel(Duel duel, PracticePlayer winner) {
        duel.setState(Duel.DuelState.FINISHED);

        Player p1 = Bukkit.getPlayer(duel.getPlayer1().getUuid());
        Player p2 = Bukkit.getPlayer(duel.getPlayer2().getUuid());

        // Reset player states
        duel.getPlayer1().setState(PracticePlayer.PlayerState.SPAWN);
        duel.getPlayer2().setState(PracticePlayer.PlayerState.SPAWN);
        duel.getPlayer1().setCurrentDuel(null);
        duel.getPlayer2().setCurrentDuel(null);

        // Free arena
        duel.getArena().setInUse(false);

        // Teleport to spawn and restore inventories
        if (p1 != null) {
            plugin.getConfigManager().teleportToSpawn(p1);
            duel.getPlayer1().restoreInventory(p1);
        }
        if (p2 != null) {
            plugin.getConfigManager().teleportToSpawn(p2);
            duel.getPlayer2().restoreInventory(p2);
        }

        // Announce winner
        if (winner != null) {
            String winnerName = winner.getName();
            String loserName = duel.getOpponent(winner).getName();

            if (p1 != null) MessageUtil.sendMessage(p1, "&e" + winnerName + " &awon the duel!");
            if (p2 != null) MessageUtil.sendMessage(p2, "&e" + winnerName + " &awon the duel!");

            // Update stats in file storage
            boolean p1Won = winner.equals(duel.getPlayer1());
            int eloChange = calculateEloChange(duel.getPlayer1(), duel.getPlayer2(), p1Won);

            plugin.getFileDataManager().updatePlayerStats(winner.getUuid(), duel.getKit(), true, eloChange);
            plugin.getFileDataManager().updatePlayerStats(duel.getOpponent(winner).getUuid(), duel.getKit(), false, -eloChange);
        }

        // Remove duel
        duels.remove(duel.getId());
    }

    private int calculateEloChange(PracticePlayer p1, PracticePlayer p2, boolean p1Won) {
        // Basic ELO calculation - can be improved
        return 25;
    }

    public Duel getDuel(UUID playerId) {
        for (Duel duel : duels.values()) {
            if (duel.containsPlayer(playerId)) {
                return duel;
            }
        }
        return null;
    }

    public boolean hasPendingRequest(UUID playerId) {
        return duelRequests.containsValue(playerId);
    }

    public Map<UUID, Duel> getAllDuels() {
        return duels;
    }
}