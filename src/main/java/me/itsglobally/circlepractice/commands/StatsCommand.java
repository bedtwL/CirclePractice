package me.itsglobally.circlePractice.commands;

import me.itsglobally.circlePractice.CirclePractice;
import me.itsglobally.circlePractice.data.FileDataManager;
import me.itsglobally.circlePractice.utils.MessageUtil;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import top.nontage.nontagelib.annotations.CommandInfo;
import top.nontage.nontagelib.command.NontageCommand;

@CommandInfo(name = "stats", description = "ga")
public class StatsCommand implements NontageCommand {

    private final CirclePractice plugin = CirclePractice.getInstance();

    @Override
    public void execute(CommandSender sender, String s, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("This command can only be used by players!");
            return;
        }

        Player target = player;

        if (args.length == 1) {
            target = Bukkit.getPlayer(args[0]);
            if (target == null) {
                MessageUtil.sendMessage(player, "&cPlayer not found!");
                return;
            }
        }

        MessageUtil.sendMessage(player, "&e&m-----&r &6" + target.getName() + "'s Stats &e&m-----");

        for (String kit : plugin.getKitManager().getAllKits().keySet()) {
            FileDataManager.PlayerStats stats = plugin.getFileDataManager().getPlayerStats(target.getUniqueId(), kit);
            MessageUtil.sendMessage(player, "&e" + kit + ": &aW: " + stats.wins() +
                    " &cL: " + stats.losses() + " &bELO: " + stats.elo() +
                    " &dWR: " + String.format("%.1f", stats.getWinRate()) + "%");
        }

    }
}