package me.itsglobally.circlePractice.aa;

import me.itsglobally.circlePractice.a;
import org.bukkit.command.CommandSender;
import top.nontage.nontagelib.annotations.CommandInfo;
import top.nontage.nontagelib.command.NontageCommand;

@CommandInfo(name = "ccrl", permission = "circlepractice.admin", override = true, shouldLoad = true)
public class a0 implements NontageCommand {

    @Override
    public void execute(CommandSender commandSender, String s, String[] strings) {
        a.getInstance().getPluginManager().reload();
        commandSender.sendMessage("reloaded");
    }
}
