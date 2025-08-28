package me.itsglobally.circlePractice.commands;

import me.itsglobally.circlePractice.CirclePractice;
import me.itsglobally.circlePractice.data.PracticePlayer;
import me.itsglobally.circlePractice.utils.MessageUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class LeaveCommand implements CommandExecutor {
    
    private final CirclePractice plugin;
    
    public LeaveCommand(CirclePractice plugin) {
        this.plugin = plugin;
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("This command can only be used by players!");
            return true;
        }
        
        Player player = (Player) sender;
        PracticePlayer practicePlayer = plugin.getPlayerManager().getPlayer(player);
        
        if (practicePlayer.isInQueue()) {
            plugin.getQueueManager().leaveQueue(player);
        } else if (practicePlayer.isInDuel()) {
            // End duel with opponent as winner
            plugin.getDuelManager().endDuel(practicePlayer.getCurrentDuel(), 
                practicePlayer.getCurrentDuel().getOpponent(practicePlayer));
            MessageUtil.sendMessage(player, "&cYou left the duel!");
        } else {
            MessageUtil.sendMessage(player, "&cYou're not in a queue or duel!");
        }
        
        return true;
    }
}