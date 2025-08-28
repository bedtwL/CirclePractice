package me.itsglobally.circlePractice.commands;

import me.itsglobally.circlePractice.CirclePractice;
import me.itsglobally.circlePractice.utils.MessageUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class LeaderboardCommand implements CommandExecutor {
    
    private final CirclePractice plugin;
    
    public LeaderboardCommand(CirclePractice plugin) {
        this.plugin = plugin;
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("This command can only be used by players!");
            return true;
        }
        
        Player player = (Player) sender;
        
        if (args.length != 1) {
            MessageUtil.sendMessage(player, "&cUsage: /leaderboard <kit>");
            return true;
        }
        
        String kit = args[0];
        
        if (!plugin.getKitManager().kitExists(kit)) {
            MessageUtil.sendMessage(player, "&cThat kit doesn't exist!");
            return true;
        }
        
        MessageUtil.sendMessage(player, "&e&m-----&r &6" + kit + " Leaderboard &e&m-----");
        MessageUtil.sendMessage(player, "&cLeaderboard feature coming soon!");
        
        return true;
    }
}