package me.itsglobally.circlePractice.listeners;

import me.itsglobally.circlePractice.CirclePractice;
import me.itsglobally.circlePractice.data.Duel;
import me.itsglobally.circlePractice.data.PracticePlayer;
import me.itsglobally.circlePractice.utils.MessageUtil;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

public class DuelListener implements Listener {
    
    private final CirclePractice plugin;
    
    public DuelListener(CirclePractice plugin) {
        this.plugin = plugin;
    }
    
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onEntityDamage(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Player)) return;
        
        Player player = (Player) event.getEntity();
        PracticePlayer practicePlayer = plugin.getPlayerManager().getPlayer(player);
        
        if (practicePlayer != null && practicePlayer.isInDuel()) {
            // Check if this damage would kill the player
            if (player.getHealth() - event.getFinalDamage() <= 0) {
                event.setCancelled(true);
                
                // Set player to 1 health to prevent death
                player.setHealth(1.0);
                
                // End the duel
                Duel duel = practicePlayer.getCurrentDuel();
                PracticePlayer winner = duel.getOpponent(practicePlayer);
                
                MessageUtil.sendMessage(player, "&cYou have been defeated!");
                plugin.getDuelManager().endDuel(duel, winner);
            }
        }
    }
    
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        PracticePlayer practicePlayer = plugin.getPlayerManager().getPlayer(player);
        
        if (practicePlayer != null && practicePlayer.isInDuel()) {
            // Cancel the death completely
            event.getEntity().spigot().respawn();
            event.setDeathMessage(null);
            event.getDrops().clear();
            event.setDroppedExp(0);
            
            // Set player to full health to prevent death screen
            player.setHealth(player.getMaxHealth());
            
            Duel duel = practicePlayer.getCurrentDuel();
            PracticePlayer winner = duel.getOpponent(practicePlayer);
            
            MessageUtil.sendMessage(player, "&cYou have been defeated!");
            plugin.getDuelManager().endDuel(duel, winner);
        }
    }
    
    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        Player player = event.getPlayer();
        PracticePlayer practicePlayer = plugin.getPlayerManager().getPlayer(player);
        
        if (practicePlayer != null && practicePlayer.getState() == PracticePlayer.PlayerState.SPAWN) {
            // Set respawn location to spawn
            // This will be handled by the SpawnCommand teleportToSpawn method
        }
    }
}