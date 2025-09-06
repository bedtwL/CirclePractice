package me.itsglobally.circlePractice.aa;

import me.itsglobally.circlePractice.a;
import me.itsglobally.circlePractice.ff.f6;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import top.nontage.nontagelib.command.NontageCommand;

// @CommandInfo(name = "leaderboard", description = "ga", override = true, shouldLoad = true)
public class a6 implements NontageCommand {

    private final a plugin = a.getInstance();

    @Override
    public void execute(CommandSender sender, String s, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("This command can only be used by players!");
            return;
        }

        if (args.length != 1) {
            f6.sendMessage(player, "&cUsage: /leaderboard <kit>");
            return;
        }

        String kit = args[0];

        if (!plugin.getKitManager().kitExists(kit)) {
            f6.sendMessage(player, "&cThat kit doesn't exist!");
            return;
        }

        f6.sendMessage(player, "&e&m-----&r &6" + kit + " Leaderboard &e&m-----");
        f6.sendMessage(player, "&cLeaderboard feature coming soon!");

    }
}