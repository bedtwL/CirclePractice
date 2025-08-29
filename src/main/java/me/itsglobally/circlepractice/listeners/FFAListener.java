package me.itsglobally.circlePractice.listeners;

import me.itsglobally.circlePractice.CirclePractice;
import me.itsglobally.circlePractice.data.TempData;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;

public class FFAListener implements Listener {

    private final CirclePractice plugin;

    public FFAListener(CirclePractice plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent e) {

    }
    @EventHandler
    public void onBlockBreak(BlockBreakEvent e) {

    }
    @EventHandler
    public void onDamage(EntityDamageEvent e) {
        if (e.getCause() == EntityDamageEvent.DamageCause.FALL) {
            e.setCancelled(true);
            return;
        }
        if (e instanceof EntityDamageByEntityEvent edbee) {
            Entity dmgere = edbee.getDamager();
            Entity vice = e.getEntity();
            if (!(vice instanceof Player vic)) {
                e.setCancelled(true);
                return;
            }
            if (!(dmgere instanceof Player dmger)) {
                e.setCancelled(true);
                return;
            }
            TempData.setLastHit(vic.getUniqueId(), dmger.getUniqueId());
            TempData.setLastHit(dmger.getUniqueId(), vic.getUniqueId());
            if (vic.getHealth() < e.getFinalDamage()) {
                e.setCancelled(true);
                plugin.getFFAManager().kill(vic, dmger);
            }
        }
    }
}
