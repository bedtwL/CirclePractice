package me.itsglobally.circlePractice.managers;

import me.itsglobally.circlePractice.CirclePractice;
import me.itsglobally.circlePractice.data.Arena;
import me.itsglobally.circlePractice.utils.LocationUtil;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.HashMap;
import java.util.Map;

public class ArenaManager {

    private final CirclePractice plugin;
    private final Map<String, Arena> arenas;

    public ArenaManager(CirclePractice plugin) {
        this.plugin = plugin;
        this.arenas = new HashMap<>();
        loadArenas();
    }

    public void loadArenas() {
        FileConfiguration config = plugin.getConfigManager().getArenas();
        ConfigurationSection arenasSection = config.getConfigurationSection("arenas");

        if (arenasSection == null) return;

        for (String name : arenasSection.getKeys(false)) {
            Arena arena = new Arena(name);
            ConfigurationSection arenaSection = arenasSection.getConfigurationSection(name);

            if (arenaSection.contains("pos1")) {
                arena.setPos1(LocationUtil.deserializeLocation(arenaSection.getString("pos1")));
            }
            if (arenaSection.contains("pos2")) {
                arena.setPos2(LocationUtil.deserializeLocation(arenaSection.getString("pos2")));
            }
            if (arenaSection.contains("spectator")) {
                arena.setSpectatorSpawn(LocationUtil.deserializeLocation(arenaSection.getString("spectator")));
            } else {
            }

            arenas.put(name, arena);
        }
    }

    public void createArena(String name) {
        Arena arena = new Arena(name);
        arenas.put(name, arena);
        saveArena(arena); // save immediately
    }

    public void deleteArena(String name) {
        arenas.remove(name);

        FileConfiguration config = plugin.getConfigManager().getArenas();
        config.set("arenas." + name, null);
        plugin.getConfigManager().saveArenas();
    }

    public void saveArena(Arena arena) {
        FileConfiguration config = plugin.getConfigManager().getArenas();
        String path = "arenas." + arena.getName();

        if (arena.getPos1() != null) {
            config.set(path + ".pos1", LocationUtil.serializeLocation(arena.getPos1()));
        }
        if (arena.getPos2() != null) {
            config.set(path + ".pos2", LocationUtil.serializeLocation(arena.getPos2()));
        }
        if (arena.getSpectatorSpawn() != null) {
            config.set(path + ".spectator", LocationUtil.serializeLocation(arena.getSpectatorSpawn()));
        }

        plugin.getConfigManager().saveArenas();
    }

    public Arena getArena(String name) {
        return arenas.get(name);
    }

    public Arena getAvailableArena() {
        return arenas.values().stream()
                .filter(arena -> !arena.isInUse() && arena.isComplete())
                .findFirst()
                .orElse(null);
    }

    public Map<String, Arena> getAllArenas() {
        return arenas;
    }
}
