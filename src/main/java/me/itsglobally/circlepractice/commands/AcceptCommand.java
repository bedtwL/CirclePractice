package me.itsglobally.circlePractice.commands;

import me.itsglobally.circlePractice.CirclePractice;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class AcceptCommand implements CommandExecutor {
    
    private final CirclePractice plugin;
    
    public AcceptCommand(CirclePractice plugin) {
        this.plugin = plugin;
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("This command can only be used by players!");
            return true;
        }
        
        Player player = (Player) sender;
        plugin.getDuelManager().acceptDuel(player);
        return true;
    }
}