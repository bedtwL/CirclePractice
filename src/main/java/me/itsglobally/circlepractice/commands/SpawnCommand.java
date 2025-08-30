package me.itsglobally.circlePractice.commands;

import me.itsglobally.circlePractice.CirclePractice;
import me.itsglobally.circlePractice.data.Duel;
import me.itsglobally.circlePractice.data.PracticePlayer;
import me.itsglobally.circlePractice.utils.MessageUtil;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import top.nontage.nontagelib.annotations.CommandInfo;
import top.nontage.nontagelib.command.NontageCommand;

@CommandInfo(name = "spawn", description = "ga")
public class SpawnCommand implements NontageCommand {

    private final CirclePractice plugin = CirclePractice.getInstance();

    @Override
    public void execute(CommandSender sender, String s, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("This command can only be used by players!");
            return;
        }
        PracticePlayer pp = plugin.getPlayerManager().getPlayer(player.getUniqueId());
        Duel duel = plugin.getDuelManager().getDuel(player.getUniqueId());
        if (duel != null) {
            plugin.getDuelManager().endDuel(duel, duel.getOpponent(pp));
            return;
        }
        if (pp.isInFFA()) {
            plugin.getFFAManager().leaveFFA(player);
        }
        plugin.getConfigManager().teleportToSpawn(player);
        MessageUtil.sendMessage(player, "&aTeleported to spawn!");
    }
}