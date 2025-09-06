package me.itsglobally.circlePractice.cc;

import me.itsglobally.circlePractice.a;
import me.itsglobally.circlePractice.bb.b1;
import me.itsglobally.circlePractice.bb.b4;
import me.itsglobally.circlePractice.ff.f6;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import top.nontage.nontagelib.annotations.AutoListener;

@AutoListener
public class c0 implements Listener {

    private final a plugin = a.getInstance();

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onEntityDamage(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Player player)) return;

        b4 b4 = plugin.getPlayerManager().getPlayer(player);

        if (b4 != null && b4.isInDuel()) {
            if (player.getHealth() - event.getFinalDamage() <= 0) {
                EntityDamageByEntityEvent edbe = (EntityDamageByEntityEvent) event;
                event.setCancelled(true);

                player.setHealth(20.0);

                // End the duel
                b1 b1 = b4.getCurrentDuel();
                b4 winner = b1.getOpponent(b4);
                winner.getPlayer().setHealth(20.0);


                f6.sendTitle(player, "&cDEFEAT!", "You have been defeated by " + winner.getName());
                f6.sendTitle(winner.getPlayer(), "&aVICTORY!", "You have defeated " + b4.getName());
                plugin.getDuelManager().endDuel(b1, winner);
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        b4 b4 = plugin.getPlayerManager().getPlayer(player);

        if (b4 != null && b4.isInDuel()) {
            // Cancel the death completely
            event.getEntity().spigot().respawn();
            event.setDeathMessage(null);
            event.getDrops().clear();
            event.setDroppedExp(0);

            // Set player to full health to prevent death screen
            player.setHealth(player.getMaxHealth());

            b1 b1 = b4.getCurrentDuel();
            b4 winner = b1.getOpponent(b4);

            f6.sendTitle(player, "&cDEFEAT!", "You have been defeated by " + winner.getName());
            f6.sendTitle(winner.getPlayer(), "&aVICTORY!", "You have defeated " + b4.getName());
            plugin.getDuelManager().endDuel(b1, winner);
        }
    }

    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        Player player = event.getPlayer();
        b4 b4 = plugin.getPlayerManager().getPlayer(player);

        if (b4 != null && b4.getState() == me.itsglobally.circlePractice.bb.b4.PlayerState.SPAWN) {
            // Set respawn location to spawn
            // This will be handled by the SpawnCommand teleportToSpawn method
        }
    }

    @EventHandler
    public void onMove(PlayerMoveEvent e) {
        Player p = e.getPlayer();
        b4 pp = plugin.getPlayerManager().getPlayer(p.getUniqueId());
        if (pp.isInDuel() && plugin.getDuelManager().getDuel(p.getUniqueId()).getState() == b1.DuelState.STARTING)
            e.setCancelled(true);
    }

}