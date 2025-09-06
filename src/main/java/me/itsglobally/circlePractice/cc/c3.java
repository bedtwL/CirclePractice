package me.itsglobally.circlePractice.cc;

import me.itsglobally.circlePractice.a;
import me.itsglobally.circlePractice.bb.b4;
import me.itsglobally.circlePractice.ff.f6;
import me.itsglobally.circlePractice.ff.f1;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import top.nontage.nontagelib.annotations.AutoListener;

@AutoListener
public class c3 implements Listener {

    private final a plugin = a.getInstance();

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        if (!(event.getPlayer() instanceof Player player)) return;

        b4 b4 = plugin.getPlayerManager().getPlayer(player);

        if (b4 != null && b4.getState() == me.itsglobally.circlePractice.bb.b4.PlayerState.EDITING) {
            // Save kit contents
            String kit = b4.getQueuedKit(); // Get the kit being edited
            if (kit != null) {
                b4.saveKitContents(kit,
                        player.getInventory().getContents(),
                        player.getInventory().getArmorContents());

                // Also save to file storage
                plugin.getFileDataManager().saveKitContents(
                        player.getUniqueId(),
                        kit,
                        f1.serializeInventory(
                                player.getInventory().getContents(),
                                player.getInventory().getArmorContents()
                        )
                );

                f6.sendMessage(player, "&aKit &e" + kit + " &asaved successfully!");
                player.getInventory().clear();
            }

            b4.setState(me.itsglobally.circlePractice.bb.b4.PlayerState.SPAWN);
        }
    }
}