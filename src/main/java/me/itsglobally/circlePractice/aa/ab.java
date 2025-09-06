package me.itsglobally.circlePractice.aa;

import me.itsglobally.circlePractice.a;
import me.itsglobally.circlePractice.bb.b2;
import me.itsglobally.circlePractice.ff.f6;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import top.nontage.nontagelib.annotations.CommandInfo;
import top.nontage.nontagelib.command.NontageCommand;

@CommandInfo(name = "stats", description = "ga", override = true, shouldLoad = true)
public class ab implements NontageCommand {

    private final a plugin = a.getInstance();

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
                f6.sendMessage(player, "&cPlayer not found!");
                return;
            }
        }

        f6.sendMessage(player, "&e&m-----&r &6" + target.getName() + "'s Stats &e&m-----");

        for (String kit : plugin.getKitManager().getAllKits().keySet()) {
            b2.PlayerStats stats = plugin.getFileDataManager().getPlayerStats(target.getUniqueId(), kit);
            f6.sendMessage(player, "&e" + kit + ": &aWins: " + stats.wins() +
                    " &cLosses: " + stats.losses() +
                    " &dWin Rate: " + String.format("%.1f", stats.getWinRate()) + "%");
        }

    }
}