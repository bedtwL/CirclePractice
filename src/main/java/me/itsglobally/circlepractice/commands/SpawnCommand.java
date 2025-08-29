package me.itsglobally.circlePractice.commands;

import me.itsglobally.circlePractice.CirclePractice;
import me.itsglobally.circlePractice.data.Duel;
import me.itsglobally.circlePractice.data.PracticePlayer;
import me.itsglobally.circlePractice.utils.MessageUtil;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class SpawnCommand implements CommandExecutor {
    
    private final CirclePractice plugin;
    
    public SpawnCommand(CirclePractice plugin) {
        this.plugin = plugin;
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("This command can only be used by players!");
            return true;
        }
        PracticePlayer pp = plugin.getPlayerManager().getPlayer(player.getUniqueId());
        Duel duel = plugin.getDuelManager().getDuel(player.getUniqueId());
        if (duel != null) {
            plugin.getDuelManager().endDuel(duel, duel.getOpponent(pp));
            return true;
        }
        if (pp.isInFFA()) {
            plugin.getFFAManager().leaveFFA(player);
        }
        plugin.getConfigManager().teleportToSpawn(player);
        MessageUtil.sendMessage(player, "&aTeleported to spawn!");
        return true;
    }
}