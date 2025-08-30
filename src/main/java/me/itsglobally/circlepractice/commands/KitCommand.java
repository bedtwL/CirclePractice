package me.itsglobally.circlePractice.commands;

import me.itsglobally.circlePractice.CirclePractice;
import me.itsglobally.circlePractice.data.PracticePlayer;
import me.itsglobally.circlePractice.utils.MessageUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class KitCommand implements CommandExecutor {

    private final CirclePractice plugin;

    public KitCommand(CirclePractice plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("This command can only be used by players!");
            return true;
        }

        if (args.length == 0) {
            MessageUtil.sendMessage(player, "&cUsage: /kit <editor|load> [kit]");
            return true;
        }

        String subCommand = args[0];

        if (subCommand.equalsIgnoreCase("editor")) {
            if (args.length != 2) {
                MessageUtil.sendMessage(player, "&cUsage: /kit editor <kit>");
                return true;
            }

            String kit = args[1];
            if (!plugin.getKitManager().kitExists(kit)) {
                MessageUtil.sendMessage(player, "&cThat kit doesn't exist!");
                return true;
            }

            PracticePlayer practicePlayer = plugin.getPlayerManager().getPlayer(player);
            practicePlayer.setState(PracticePlayer.PlayerState.EDITING);

            ItemStack[] saved = practicePlayer.getKitContents(kit);

            if (saved == null) {
                String serialized = plugin.getFileDataManager().getKitContents(player.getUniqueId(), kit);
                if (serialized != null) {
                    ItemStack[][] deserialized = me.itsglobally.circlePractice.utils.InventorySerializer.deserializeInventory(serialized);
                    if (deserialized != null) {
                        player.getInventory().setContents(deserialized[0]);
                        player.getInventory().setArmorContents(deserialized[1]);
                    } else {
                        plugin.getKitManager().applyKit(player, kit);
                    }
                } else {
                    plugin.getKitManager().applyKit(player, kit);
                }
            } else {
                player.getInventory().setContents(java.util.Arrays.copyOf(saved, 36));
                player.getInventory().setArmorContents(java.util.Arrays.copyOfRange(saved, 36, 40));
            }

            practicePlayer.setQueuedKit(kit);

            MessageUtil.sendMessage(player, "&aYou are now editing the &e" + kit + " &akit!");
            MessageUtil.sendMessage(player, "&eClose your inventory to save changes!");

        } else if (subCommand.equalsIgnoreCase("load")) {
            if (args.length != 2) {
                MessageUtil.sendMessage(player, "&cUsage: /kit load <kit>");
                return true;
            }

            String kit = args[1];
            PracticePlayer practicePlayer = plugin.getPlayerManager().getPlayer(player);
            ItemStack[] saved = practicePlayer.getKitContents(kit);

            // Try loading from file storage if not in memory
            if (saved != null) {
                player.getInventory().setContents(java.util.Arrays.copyOf(saved, 36));
                player.getInventory().setArmorContents(java.util.Arrays.copyOfRange(saved, 36, 40));
                MessageUtil.sendMessage(player, "&aLoaded your saved &e" + kit + " &akit!");
            } else {
                String serialized = plugin.getFileDataManager().getKitContents(player.getUniqueId(), kit);
                if (serialized != null) {
                    ItemStack[][] deserialized = me.itsglobally.circlePractice.utils.InventorySerializer.deserializeInventory(serialized);
                    if (deserialized != null) {
                        player.getInventory().setContents(deserialized[0]);
                        player.getInventory().setArmorContents(deserialized[1]);
                        MessageUtil.sendMessage(player, "&aLoaded your saved &e" + kit + " &akit!");
                    } else {
                        MessageUtil.sendMessage(player, "&cYou don't have a saved kit for &e" + kit + "&c!");
                    }
                } else {
                    MessageUtil.sendMessage(player, "&cYou don't have a saved kit for &e" + kit + "&c!");
                }
            }
        }

        return true;
    }
}