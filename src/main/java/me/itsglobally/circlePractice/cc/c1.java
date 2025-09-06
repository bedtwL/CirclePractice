package me.itsglobally.circlePractice.cc;

import me.itsglobally.circlePractice.a;
import me.itsglobally.circlePractice.bb.b4;
import me.itsglobally.circlePractice.bb.b5;
import me.itsglobally.circlePractice.ff.f6;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.scheduler.BukkitRunnable;
import top.nontage.nontagelib.annotations.AutoListener;

import java.util.ArrayList;
import java.util.List;

@AutoListener
public class c1 implements Listener {

    private final a plugin = a.getInstance();
    List<Location> blockplaced = new ArrayList<>();


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
            if (vic.getLocation().getY() <= 200 || dmger.getLocation().getY() <= 200) {
                e.setCancelled(true);
                return;
            }
            b5.setLastHit(vic.getUniqueId(), dmger.getUniqueId());
            b5.setLastHit(dmger.getUniqueId(), vic.getUniqueId());
            if (vic.getHealth() < e.getFinalDamage()) {
                e.setCancelled(true);
                plugin.getFFAManager().kill(vic, dmger);
            }
        }
    }

    @EventHandler
    public void onPlace(BlockPlaceEvent e) {
        Player player = e.getPlayer();
        b4 pP = plugin.getPlayerManager().getPlayer(player);
        if (pP.getState() == b4.PlayerState.FFA) {
            if (e.getBlockPlaced().getY() <= 200) {
                e.setCancelled(true);
                f6.sendActionBar(player, "&cYou cannot place blocks here!");
            }
            Material against = e.getBlockAgainst().getType();
            if (against == Material.WATER || against == Material.STATIONARY_WATER || against == Material.LAVA || against == Material.STATIONARY_LAVA) {
                f6.sendActionBar(player, "&cYou cannot place blocks here!");
                e.setCancelled(true);
                return;
            }
            blockplaced.add(e.getBlockPlaced().getLocation());
            new BukkitRunnable() {
                @Override
                public void run() {
                    e.getBlockPlaced().setType(Material.AIR);
                    blockplaced.remove(e.getBlockPlaced().getLocation());
                }
            }.runTaskLater(plugin, 8 * 20L);
        }
    }

    @EventHandler
    public void onBreak(BlockBreakEvent e) {
        Player player = e.getPlayer();
        b4 pP = plugin.getPlayerManager().getPlayer(player);
        if (pP.getState() == b4.PlayerState.FFA) {
            if (e.getBlock().getY() <= 200) {
                e.setCancelled(true);
                f6.sendActionBar(player, "&cYou cannot break blocks here!");
            }
            if (!blockplaced.contains(e.getBlock().getLocation())) {
                e.setCancelled(true);
                f6.sendActionBar(player, "&cYou cannot break this block!");
            }
            blockplaced.remove(e.getBlock().getLocation());
        }
    }
}
