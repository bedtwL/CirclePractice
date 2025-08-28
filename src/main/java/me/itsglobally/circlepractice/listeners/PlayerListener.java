package me.itsglobally.circlePractice.listeners;

import me.itsglobally.circlePractice.CirclePractice;
import me.itsglobally.circlePractice.data.PracticePlayer;
import me.itsglobally.circlePractice.utils.MessageUtil;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerListener implements Listener {
    
    private final CirclePractice plugin;
    
    public PlayerListener(CirclePractice plugin) {
        this.plugin = plugin;
    }
    
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
}