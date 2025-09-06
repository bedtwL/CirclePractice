package me.itsglobally.circlePractice.aa;

import me.itsglobally.circlePractice.a;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import top.nontage.nontagelib.annotations.CommandInfo;
import top.nontage.nontagelib.command.NontageCommand;

@CommandInfo(name = "accept", description = "ga", override = true, shouldLoad = true)
public class a1 implements NontageCommand {

    private final a plugin = a.getInstance();

    @Override
    public void execute(CommandSender sender, String s, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("This command can only be used by players!");
            return;
        }

        plugin.getDuelManager().acceptDuel(player);
    }
}