package me.itsglobally.circlePractice.ff;

import me.itsglobally.circlePractice.a;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;

public class f3 {

    private final a plugin;
    private FileConfiguration config;
    private FileConfiguration arenas;
    private FileConfiguration kits;

    private File configFile;
    private File arenasFile;
    private File kitsFile;

    public f3(a plugin) {
        this.plugin = plugin;
    }

    public void setupConfig() {
        // Main config
        configFile = new File(plugin.getDataFolder(), "config.yml");
        if (!configFile.exists()) {
            plugin.saveDefaultConfig();
        }
        config = plugin.getConfig();

        // Arenas config
        arenasFile = new File(plugin.getDataFolder(), "arenas.yml");
        if (!arenasFile.exists()) {
            try {
                arenasFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        arenas = YamlConfiguration.loadConfiguration(arenasFile);

        // Kits config
        kitsFile = new File(plugin.getDataFolder(), "kits.yml");
        if (!kitsFile.exists()) {
            try {
                kitsFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        kits = YamlConfiguration.loadConfiguration(kitsFile);

        setupDefaults();
    }

    private void setupDefaults() {
        // Default configuration values
        if (!config.contains("spawn")) {
            config.set("spawn.world", "world");
            config.set("spawn.x", 0.5);
            config.set("spawn.y", 100);
            config.set("spawn.z", 0.5);
            config.set("spawn.yaw", 0);
            config.set("spawn.pitch", 0);
        }

        if (!config.contains("settings.queue-time")) {
            config.set("settings.queue-time", 60);
            config.set("settings.duel-time", 300);
            config.set("settings.spectator-enabled", true);
        }

        saveConfig();
    }

    public void saveConfig() {
        try {
            config.save(configFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void saveArenas() {
        try {
            arenas.save(arenasFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void saveKits() {
        try {
            kits.save(kitsFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public FileConfiguration getConfig() {
        return config;
    }

    public FileConfiguration getArenas() {
        return arenas;
    }

    public FileConfiguration getKits() {
        return kits;
    }

    public void teleportToSpawn(Player player) {
        String world = config.getString("spawn.world", "world");
        double x = config.getDouble("spawn.x", 0.5);
        double y = config.getDouble("spawn.y", 100);
        double z = config.getDouble("spawn.z", 0.5);
        float yaw = (float) config.getDouble("spawn.yaw", 0);
        float pitch = (float) config.getDouble("spawn.pitch", 0);

        Location spawn = new Location(Bukkit.getWorld(world), x, y, z, yaw, pitch);
        player.teleport(spawn);
    }
}