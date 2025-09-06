package me.itsglobally.circlePractice.aa;

import me.itsglobally.circlePractice.a;
import me.itsglobally.circlePractice.bb.b1;
import me.itsglobally.circlePractice.bb.b4;
import me.itsglobally.circlePractice.ff.f6;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import top.nontage.nontagelib.annotations.CommandInfo;
import top.nontage.nontagelib.command.NontageCommand;

@CommandInfo(name = "spawn", description = "ga", override = true, shouldLoad = true)
public class aa implements NontageCommand {

    private final a plugin = a.getInstance();

    @Override
    public void execute(CommandSender sender, String s, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("This command can only be used by players!");
            return;
        }
        b4 pp = plugin.getPlayerManager().getPlayer(player.getUniqueId());
        b1 b1 = plugin.getDuelManager().getDuel(player.getUniqueId());
        if (b1 != null) {
            plugin.getDuelManager().endDuel(b1, b1.getOpponent(pp));
            return;
        }
        if (pp.isInFFA()) {
            plugin.getFFAManager().leaveFFA(player);
        }
        plugin.getConfigManager().teleportToSpawn(player);
        f6.sendMessage(player, "&aTeleported to spawn!");
    }
}