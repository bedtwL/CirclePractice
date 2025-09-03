package me.itsglobally.circlePractice.commands;

import me.itsglobally.circlePractice.CirclePractice;
import org.bukkit.command.CommandSender;
import top.nontage.nontagelib.annotations.CommandInfo;
import top.nontage.nontagelib.command.NontageCommand;

@CommandInfo(name = "ccrl", permission = "circlepractice.admin", override = true, shouldLoad = true)
public class ReloadCommand implements NontageCommand {

    @Override
    public void execute(CommandSender commandSender, String s, String[] strings) {
        CirclePractice.getInstance().getPluginManager().reload();
        commandSender.sendMessage("reloaded");
    }
}
