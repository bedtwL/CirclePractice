package me.itsglobally.circlePractice.aa;

import me.itsglobally.circlePractice.a;
import me.itsglobally.circlePractice.ff.f6;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import top.nontage.nontagelib.annotations.CommandInfo;
import top.nontage.nontagelib.command.NontageCommand;

@CommandInfo(name = "duel", description = "ga", override = true, shouldLoad = true)
public class a3 implements NontageCommand {

    private final a plugin = a.getInstance();

    @Override
    public void execute(CommandSender sender, String s, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("This command can only be used by players!");
            return;
        }

        if (args.length != 2) {
            f6.sendMessage(player, "&cUsage: /duel <player> <kit>");
            return;
        }

        String targetName = args[0];
        String kit = args[1];

        Player target = Bukkit.getPlayer(targetName);
        if (target == null || !target.isOnline()) {
            f6.sendMessage(player, "&cPlayer not found!");
            return;
        }

        if (target.equals(player)) {
            f6.sendMessage(player, "&cYou cannot duel yourself!");
            return;
        }

        if (!plugin.getKitManager().kitExists(kit)) {
            f6.sendMessage(player, "&cThat kit doesn't exist!");
            return;
        }

        plugin.getDuelManager().sendDuelRequest(player, target, kit);
    }
}