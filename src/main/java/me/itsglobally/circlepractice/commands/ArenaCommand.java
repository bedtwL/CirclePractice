package me.itsglobally.circlePractice.commands;

import me.itsglobally.circlePractice.CirclePractice;
import me.itsglobally.circlePractice.data.Arena;
import me.itsglobally.circlePractice.utils.MessageUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ArenaCommand implements CommandExecutor {

    private final CirclePractice plugin;

    public ArenaCommand(CirclePractice plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("This command can only be used by players!");
            return true;
        }

        if (!player.hasPermission("circlepractice.admin")) {
            MessageUtil.sendMessage(player, "&cYou don't have permission to use this command!");
            return true;
        }

        if (args.length == 0) {
            MessageUtil.sendMessage(player, "&cUsage: /arena <create|delete|list|setpos1|setpos2|setspectator> [name]");
            return true;
        }

        String subCommand = args[0];
        // /arena create gay true
        if (subCommand.equalsIgnoreCase("create")) {

            if (args.length < 3) {
                MessageUtil.sendMessage(player, "&cUsage: /arena create <name> <canBuild>");
                return true;
            }

            String name = args[1];
            Boolean canBuild = false;
            try {
                canBuild = Boolean.getBoolean(args[2]);
            } catch (Exception e) {
                e.printStackTrace();
                MessageUtil.sendMessage(player, "&cUsage: /arena create <name> <canBuild>");
            }
            plugin.getArenaManager().createArena(name, canBuild);
            MessageUtil.sendMessage(player, "&aCreated arena &e" + name + "&a!");

        } else if (subCommand.equalsIgnoreCase("delete")) {
            if (args.length != 2) {
                MessageUtil.sendMessage(player, "&cUsage: /arena delete <name>");
                return true;
            }

            String name = args[1];
            plugin.getArenaManager().deleteArena(name);
            MessageUtil.sendMessage(player, "&aDeleted arena &e" + name + "&a!");

        } else if (subCommand.equalsIgnoreCase("list")) {
            MessageUtil.sendMessage(player, "&eArenas:");
            for (Arena arena : plugin.getArenaManager().getAllArenas().values()) {
                String status = arena.isComplete() ? "&aComplete" : "&cIncomplete";
                MessageUtil.sendMessage(player, "&f- &e" + arena.getName() + " " + status);
            }

        } else if (subCommand.equalsIgnoreCase("setpos1")) {
            if (args.length != 2) {
                MessageUtil.sendMessage(player, "&cUsage: /arena setpos1 <name>");
                return true;
            }

            String name = args[1];
            Arena arena = plugin.getArenaManager().getArena(name);
            if (arena == null) {
                MessageUtil.sendMessage(player, "&cArena not found!");
                return true;
            }

            arena.setPos1(player.getLocation());
            plugin.getArenaManager().saveArena(arena);
            MessageUtil.sendMessage(player, "&aSet position 1 for arena &e" + name + "&a!");

        } else if (subCommand.equalsIgnoreCase("setpos2")) {
            if (args.length != 2) {
                MessageUtil.sendMessage(player, "&cUsage: /arena setpos2 <name>");
                return true;
            }

            String name = args[1];
            Arena arena = plugin.getArenaManager().getArena(name);
            if (arena == null) {
                MessageUtil.sendMessage(player, "&cArena not found!");
                return true;
            }

            arena.setPos2(player.getLocation());
            plugin.getArenaManager().saveArena(arena);
            MessageUtil.sendMessage(player, "&aSet position 2 for arena &e" + name + "&a!");

        } else if (subCommand.equalsIgnoreCase("setspectator")) {
            if (args.length != 2) {
                MessageUtil.sendMessage(player, "&cUsage: /arena setspectator <name>");
                return true;
            }

            String name = args[1];
            Arena arena = plugin.getArenaManager().getArena(name);
            if (arena == null) {
                MessageUtil.sendMessage(player, "&cArena not found!");
                return true;
            }

            arena.setSpectatorSpawn(player.getLocation());
            plugin.getArenaManager().saveArena(arena);
            MessageUtil.sendMessage(player, "&aSet spectator spawn for arena &e" + name + "&a!");
        }

        return true;
    }
}