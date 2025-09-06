package me.itsglobally.circlePractice.aa;

import me.itsglobally.circlePractice.ee.e0;
import me.itsglobally.circlePractice.ff.f6;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import top.nontage.nontagelib.annotations.CommandInfo;
import top.nontage.nontagelib.command.NontageCommand;

@CommandInfo(name = "menu")
public class a8 implements NontageCommand {

    @Override
    public void execute(CommandSender commandSender, String s, String[] strings) {
        if (!(commandSender instanceof Player p)) {
            return;
        }
        if (strings.length < 1) {
            f6.sendMessage(p, "&cUsage: /menu <name>");
            return;
        }
        String menuName = strings[0];

        switch (menuName) {
            case "gamemode" -> e0.open(p);
            default -> f6.sendMessage(p, "&cMenu not found!");
        }
    }
}
