package me.itsglobally.circlePractice.aa;

import me.itsglobally.circlePractice.a;
import me.itsglobally.circlePractice.bb.b2;
import me.itsglobally.circlePractice.bb.b5;
import me.itsglobally.circlePractice.ff.f6;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import top.nontage.nontagelib.annotations.CommandInfo;
import top.nontage.nontagelib.command.NontageCommand;

@CommandInfo(name = "ffa", description = "?", override = true, shouldLoad = true)
public class a4 implements NontageCommand {

    private final a plugin = a.getInstance();


    @Override
    public void execute(CommandSender sender, String s, String[] args) {
        if (!(sender instanceof Player p)) {
            return;
        }

        if (args.length < 1) {
            f6.sendMessage(p, "&c/ffa [join/leave/build/stats]");
            return;
        }

        switch (args[0]) {
            case "join" -> {
                plugin.getFFAManager().joinFFA(p);
            }
            case "leave" -> {
                plugin.getFFAManager().leaveFFA(p);
            }
            case "build" -> {
                b5.toggleBuild(p.getUniqueId());
                f6.sendActionBar(p, "&7Changed your build mode to " + b5.getBuild(p.getUniqueId()));
            }
            case "stats" -> {
                b2.FfaStats stats = plugin.getFileDataManager().getFfaStats(p.getUniqueId());
                f6.sendMessage(p, "&cKills&r:" + stats.kills() + "\n&cDeaths&r: " + stats.deaths());
            }
            default -> {
                f6.sendMessage(p, "&c/ffa [join/leave/build/stats]");
            }
        }
    }
}
