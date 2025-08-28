package me.itsglobally.circlePractice.commands;

import me.itsglobally.circlePractice.CirclePractice;
import me.itsglobally.circlePractice.utils.MessageUtil;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class DuelCommand implements CommandExecutor {
    
    private final CirclePractice plugin;
    
    public DuelCommand(CirclePractice plugin) {
        this.plugin = plugin;
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("This command can only be used by players!");
            return true;
        }
        
        Player player = (Player) sender;
        
        if (args.length != 2) {
            MessageUtil.sendMessage(player, "&cUsage: /duel <player> <kit>");
            return true;
        }
        
        String targetName = args[0];
        String kit = args[1];
        
        Player target = Bukkit.getPlayer(targetName);
        if (target == null || !target.isOnline()) {
            MessageUtil.sendMessage(player, "&cPlayer not found!");
            return true;
        }
        
        if (target.equals(player)) {
            MessageUtil.sendMessage(player, "&cYou cannot duel yourself!");
            return true;
        }
        
        if (!plugin.getKitManager().kitExists(kit)) {
            MessageUtil.sendMessage(player, "&cThat kit doesn't exist!");
            return true;
        }
        
        plugin.getDuelManager().sendDuelRequest(player, target, kit);
        return true;
    }
}