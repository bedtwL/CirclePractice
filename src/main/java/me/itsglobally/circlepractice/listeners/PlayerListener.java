package me.itsglobally.circlePractice.listeners;

import me.itsglobally.circlePractice.CirclePractice;
import me.itsglobally.circlePractice.data.Arena;
import me.itsglobally.circlePractice.data.Duel;
import me.itsglobally.circlePractice.data.Kit;
import me.itsglobally.circlePractice.data.PracticePlayer;
import me.itsglobally.circlePractice.utils.MessageUtil;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.ArrayList;
import java.util.List;
import top.nontage.nontagelib.annotations.AutoListener;

@AutoListener
public class PlayerListener implements Listener {

    private final CirclePractice plugin = CirclePractice.getInstance();

    List<Location> blockplaced = new ArrayList<>();
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        plugin.getPlayerManager().addPlayer(player);
        MessageUtil.sendTitle(player, "&cThis server is still in DEVELOPMENT!", "&aFeel free to report any bugs!");
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        PracticePlayer practicePlayer = plugin.getPlayerManager().getPlayer(player);

        if (practicePlayer != null) {
            // Handle leaving queue
            if (practicePlayer.isInQueue()) {
                plugin.getQueueManager().leaveQueue(player);
            }

            // Handle leaving duel
            if (practicePlayer.isInDuel()) {
                plugin.getDuelManager().endDuel(practicePlayer.getCurrentDuel(),
                        practicePlayer.getCurrentDuel().getOpponent(practicePlayer));
            }
        }

        plugin.getPlayerManager().removePlayer(player.getUniqueId());
    }

    @EventHandler
    public void onPlace(BlockPlaceEvent e) {
        Player player = e.getPlayer();
        PracticePlayer pP = plugin.getPlayerManager().getPlayer(player);
        if (pP.getState() == PracticePlayer.PlayerState.SPECTATING) {
            e.setCancelled(true);
            MessageUtil.sendActionBar(player, "&cYou cannot place blocks here!");
        }
        if (pP.getState() == PracticePlayer.PlayerState.DUEL) {
            Duel cD = plugin.getPlayerManager().getPlayer(player.getUniqueId()).getCurrentDuel();
            Kit k = plugin.getKitManager().getKit(cD.getKit());
            if (!k.canBuild()) {
                e.setCancelled(true);
                MessageUtil.sendActionBar(player, "&cYou cannot place blocks here!");
                return;
            }
            Material against = e.getBlockAgainst().getType();
            if (against == Material.WATER || against == Material.STATIONARY_WATER || against == Material.LAVA || against == Material.STATIONARY_LAVA) {
                MessageUtil.sendActionBar(player, "&cYou cannot place blocks here!");
                e.setCancelled(true);
                return;
            }
            blockplaced.add(e.getBlockPlaced().getLocation());
        }
    }

    @EventHandler
    public void onBreak(BlockBreakEvent e) {
        Player player = e.getPlayer();
        PracticePlayer pP = plugin.getPlayerManager().getPlayer(player);
        if (pP.getState() == PracticePlayer.PlayerState.SPECTATING) {
            e.setCancelled(true);
            MessageUtil.sendActionBar(player, "&cYou cannot place blocks here!");
        }
        if (pP.getState() == PracticePlayer.PlayerState.DUEL) {
            Duel cD = plugin.getPlayerManager().getPlayer(player.getUniqueId()).getCurrentDuel();
            Kit k = plugin.getKitManager().getKit(cD.getKit());
            if (!k.canBuild()) {
                e.setCancelled(true);
                MessageUtil.sendActionBar(player, "&cYou cannot place blocks here!");
                return;
            }
            if (!blockplaced.contains(e.getBlock().getLocation())) {
                e.setCancelled(true);
                MessageUtil.sendActionBar(player, "&cYou can only place blocks that placed by player!");
            }
        }
    }

    @EventHandler
    public void onMessage(AsyncPlayerChatEvent e) {
        e.setCancelled(true);
        Bukkit.broadcastMessage(MessageUtil.formatMessage(
                plugin.getPlayerManager().getPrefix(e.getPlayer())
                        + e.getPlayer().getName()
                        + "&r » "
                        + e.getMessage()
        )); // [RETARDED] Wilson_TW_awa » I AM GAY
    }

}