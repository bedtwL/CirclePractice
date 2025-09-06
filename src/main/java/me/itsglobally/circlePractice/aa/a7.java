package me.itsglobally.circlePractice.aa;

import me.itsglobally.circlePractice.a;
import me.itsglobally.circlePractice.bb.b4;
import me.itsglobally.circlePractice.ff.f6;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import top.nontage.nontagelib.annotations.CommandInfo;
import top.nontage.nontagelib.command.NontageCommand;

@CommandInfo(name = "leave", description = "ga", aliases = {"l"}, override = true, shouldLoad = true)
public class a7 implements NontageCommand {

    private final a plugin = a.getInstance();

    @Override
    public void execute(CommandSender sender, String s, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("This command can only be used by players!");
            return;
        }

        b4 b4 = plugin.getPlayerManager().getPlayer(player);

        if (b4.isInQueue()) {
            plugin.getQueueManager().leaveQueue(player);
        } else if (b4.isInDuel()) {
            plugin.getDuelManager().endDuel(b4.getCurrentDuel(),
                    b4.getCurrentDuel().getOpponent(b4));
        } else {
            f6.sendMessage(player, "&cYou're not in a queue or duel!");
        }

    }
}