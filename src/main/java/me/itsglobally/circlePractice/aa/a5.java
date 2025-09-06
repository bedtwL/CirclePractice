package me.itsglobally.circlePractice.aa;

import me.itsglobally.circlePractice.a;
import me.itsglobally.circlePractice.bb.b4;
import me.itsglobally.circlePractice.ff.f6;
import me.itsglobally.circlePractice.ff.f1;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import top.nontage.nontagelib.annotations.CommandInfo;
import top.nontage.nontagelib.command.NontageCommand;

@CommandInfo(name = "kit", description = "ga", override = true, shouldLoad = true)
public class a5 implements NontageCommand {

    private final a plugin = a.getInstance();

    @Override
    public void execute(CommandSender sender, String s, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("This command can only be used by players!");
            return;
        }

        if (args.length == 0) {
            f6.sendMessage(player, "&cUsage: /kit <editor|load> [kit]");
            return;
        }

        String subCommand = args[0];

        if (subCommand.equalsIgnoreCase("editor")) {
            if (args.length != 2) {
                f6.sendMessage(player, "&cUsage: /kit editor <kit>");
                return;
            }

            String kit = args[1];
            if (!plugin.getKitManager().kitExists(kit)) {
                f6.sendMessage(player, "&cThat kit doesn't exist!");
                return;
            }

            b4 b4 = plugin.getPlayerManager().getPlayer(player);
            b4.setState(me.itsglobally.circlePractice.bb.b4.PlayerState.EDITING);

            ItemStack[] saved = b4.getKitContents(kit);

            if (saved == null) {
                String serialized = plugin.getFileDataManager().getKitContents(player.getUniqueId(), kit);
                if (serialized != null) {
                    ItemStack[][] deserialized = f1.deserializeInventory(serialized);
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

            b4.setQueuedKit(kit);

            f6.sendMessage(player, "&aYou are now editing the &e" + kit + " &akit!");
            f6.sendMessage(player, "&eClose your inventory to save changes!");

        } else if (subCommand.equalsIgnoreCase("load")) {
            if (args.length != 2) {
                f6.sendMessage(player, "&cUsage: /kit load <kit>");
                return;
            }

            String kit = args[1];
            b4 b4 = plugin.getPlayerManager().getPlayer(player);
            ItemStack[] saved = b4.getKitContents(kit);

            // Try loading from file storage if not in memory
            if (saved != null) {
                player.getInventory().setContents(java.util.Arrays.copyOf(saved, 36));
                player.getInventory().setArmorContents(java.util.Arrays.copyOfRange(saved, 36, 40));
                f6.sendMessage(player, "&aLoaded your saved &e" + kit + " &akit!");
            } else {
                String serialized = plugin.getFileDataManager().getKitContents(player.getUniqueId(), kit);
                if (serialized != null) {
                    ItemStack[][] deserialized = f1.deserializeInventory(serialized);
                    if (deserialized != null) {
                        player.getInventory().setContents(deserialized[0]);
                        player.getInventory().setArmorContents(deserialized[1]);
                        f6.sendMessage(player, "&aLoaded your saved &e" + kit + " &akit!");
                    } else {
                        f6.sendMessage(player, "&cYou don't have a saved kit for &e" + kit + "&c!");
                    }
                } else {
                    f6.sendMessage(player, "&cYou don't have a saved kit for &e" + kit + "&c!");
                }
            }
        }

    }
}