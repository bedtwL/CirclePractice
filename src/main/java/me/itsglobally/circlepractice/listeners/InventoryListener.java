package me.itsglobally.circlePractice.listeners;

import me.itsglobally.circlePractice.CirclePractice;
import me.itsglobally.circlePractice.data.PracticePlayer;
import me.itsglobally.circlePractice.utils.MessageUtil;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import top.nontage.nontagelib.annotations.AutoListener;

@AutoListener
public class InventoryListener implements Listener {

    private final CirclePractice plugin;

    public InventoryListener(CirclePractice plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        if (!(event.getPlayer() instanceof Player player)) return;

        PracticePlayer practicePlayer = plugin.getPlayerManager().getPlayer(player);

        if (practicePlayer != null && practicePlayer.getState() == PracticePlayer.PlayerState.EDITING) {
            // Save kit contents
            String kit = practicePlayer.getQueuedKit(); // Get the kit being edited
            if (kit != null) {
                practicePlayer.saveKitContents(kit,
                        player.getInventory().getContents(),
                        player.getInventory().getArmorContents());

                // Also save to file storage
                plugin.getFileDataManager().saveKitContents(
                        player.getUniqueId(),
                        kit,
                        me.itsglobally.circlePractice.utils.InventorySerializer.serializeInventory(
                                player.getInventory().getContents(),
                                player.getInventory().getArmorContents()
                        )
                );

                MessageUtil.sendMessage(player, "&aKit &e" + kit + " &asaved successfully!");
                player.getInventory().clear();
            }

            practicePlayer.setState(PracticePlayer.PlayerState.SPAWN);
        }
    }
}