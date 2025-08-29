package me.itsglobally.circlePractice.commands;

import me.itsglobally.circlePractice.CirclePractice;
import me.itsglobally.circlePractice.data.FileDataManager;
import me.itsglobally.circlePractice.data.TempData;
import me.itsglobally.circlePractice.utils.MessageUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class FFACommand implements CommandExecutor {

    private final CirclePractice plugin;

    public FFACommand(CirclePractice plugin) {
        this.plugin = plugin;
    }


    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (!(commandSender instanceof Player p)) {
            return true;
        }

        if (strings.length < 1) {
            MessageUtil.sendMessage(p, "&c/ffa [join/leave/build/stats]");
            return true;
        }

        switch (strings[0]) {
            case "join" -> {
                plugin.getFFAManager().joinFFA(p);
            }
            case "leave" -> {
                plugin.getFFAManager().leaveFFA(p);
            }
            case "build" -> {
                TempData.toggleBuild(p.getUniqueId());
                MessageUtil.sendActionBar(p, "&7Changed your build mode to " + TempData.getBuild(p.getUniqueId()));
            }
            case "stats" -> {
                FileDataManager.FfaStats stats = plugin.getFileDataManager().getFfaStats(p.getUniqueId());
                MessageUtil.sendMessage(p, "&cKills&r:" + stats.getKills() + "\n&cDeaths&r: " + stats.getDeaths());
            }
            default -> {
                MessageUtil.sendMessage(p, "&c/ffa [join/leave/build/stats]");
                return true;
            }
        }

        return true;
    }
}
