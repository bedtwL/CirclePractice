package me.itsglobally.circlePractice.data;

import me.itsglobally.circlePractice.CirclePractice;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class FileDataManager {

    private final CirclePractice plugin;
    private FileConfiguration playerData;
    private File playerDataFile;

    // cache system
    private final Map<UUID, CachedPlayerData> cache = new HashMap<>();

    public FileDataManager(CirclePractice plugin) {
        this.plugin = plugin;
    }

    public void initialize() {
        setupPlayerDataFile();
    }

    private void setupPlayerDataFile() {
        playerDataFile = new File(plugin.getDataFolder(), "playerdata.yml");
        if (!playerDataFile.exists()) {
            try {
                playerDataFile.createNewFile();
            } catch (IOException e) {
                plugin.getLogger().severe("Could not create playerdata.yml file: " + e.getMessage());
            }
        }
        playerData = YamlConfiguration.loadConfiguration(playerDataFile);
    }

    // ----------------- Caching -----------------

    public CachedPlayerData getCachedData(UUID uuid) {
        return cache.computeIfAbsent(uuid, this::loadFromFile);
    }

    private CachedPlayerData loadFromFile(UUID uuid) {
        String path = "players." + uuid.toString();
        String name = playerData.getString(path + ".name", "Unknown");

        CachedPlayerData data = new CachedPlayerData(uuid, name);

        // Load stats
        if (playerData.contains(path + ".stats")) {
            for (String kit : playerData.getConfigurationSection(path + ".stats").getKeys(false)) {
                int wins = playerData.getInt(path + ".stats." + kit + ".wins", 0);
                int losses = playerData.getInt(path + ".stats." + kit + ".losses", 0);
                int elo = playerData.getInt(path + ".stats." + kit + ".elo", 1000);
                data.getStats().put(kit, new PlayerStats(wins, losses, elo));
            }
        }

        // Load kits
        if (playerData.contains(path + ".kits")) {
            for (String kit : playerData.getConfigurationSection(path + ".kits").getKeys(false)) {
                data.getKits().put(kit, playerData.getString(path + ".kits." + kit));
            }
        }

        return data;
    }

    public void saveCachedData(UUID uuid) {
        CachedPlayerData data = cache.get(uuid);
        if (data == null) return;

        String path = "players." + uuid.toString();
        playerData.set(path + ".name", data.getName());
        playerData.set(path + ".first-join", data.getFirstJoin());
        playerData.set(path + ".last-seen", System.currentTimeMillis());

        // Save stats
        for (Map.Entry<String, PlayerStats> entry : data.getStats().entrySet()) {
            String kitPath = path + ".stats." + entry.getKey();
            playerData.set(kitPath + ".wins", entry.getValue().getWins());
            playerData.set(kitPath + ".losses", entry.getValue().getLosses());
            playerData.set(kitPath + ".elo", entry.getValue().getElo());
        }

        // Save kits
        for (Map.Entry<String, String> entry : data.getKits().entrySet()) {
            playerData.set(path + ".kits." + entry.getKey(), entry.getValue());
        }

        savePlayerDataFile();
    }

    public void saveAllCached() {
        for (UUID uuid : cache.keySet()) {
            saveCachedData(uuid);
        }
    }

    // ----------------- API -----------------

    public void savePlayerData(UUID uuid, String name) {
        CachedPlayerData data = getCachedData(uuid);
        data.setName(name);
        if (data.getFirstJoin() == 0L) {
            data.setFirstJoin(System.currentTimeMillis());
        }
        data.setLastSeen(System.currentTimeMillis());
    }

    public void updatePlayerStats(UUID uuid, String kit, boolean won, int eloChange) {
        CachedPlayerData data = getCachedData(uuid);
        PlayerStats stats = data.getStats().getOrDefault(kit, new PlayerStats());

        if (won) {
            stats = new PlayerStats(stats.getWins() + 1, stats.getLosses(), stats.getElo() + eloChange);
        } else {
            stats = new PlayerStats(stats.getWins(), stats.getLosses() + 1, stats.getElo() + eloChange);
        }

        data.getStats().put(kit, stats);
    }

    public PlayerStats getPlayerStats(UUID uuid, String kit) {
        CachedPlayerData data = getCachedData(uuid);
        return data.getStats().getOrDefault(kit, new PlayerStats());
    }

    public void saveKitContents(UUID uuid, String kit, String serializedContents) {
        CachedPlayerData data = getCachedData(uuid);
        data.getKits().put(kit, serializedContents);
    }

    public String getKitContents(UUID uuid, String kit) {
        CachedPlayerData data = getCachedData(uuid);
        return data.getKits().get(kit);
    }

    private void savePlayerDataFile() {
        try {
            playerData.save(playerDataFile);
        } catch (IOException e) {
            plugin.getLogger().severe("Could not save playerdata.yml: " + e.getMessage());
        }
    }

    public void reloadPlayerData() {
        playerData = YamlConfiguration.loadConfiguration(playerDataFile);
        cache.clear();
    }

    // ----------------- Inner Classes -----------------

    public static class PlayerStats {
        private final int wins;
        private final int losses;
        private final int elo;

        public PlayerStats() {
            this(0, 0, 1000);
        }

        public PlayerStats(int wins, int losses, int elo) {
            this.wins = wins;
            this.losses = losses;
            this.elo = elo;
        }

        public int getWins() { return wins; }
        public int getLosses() { return losses; }
        public int getElo() { return elo; }
        public double getWinRate() {
            int total = wins + losses;
            return total == 0 ? 0.0 : (double) wins / total * 100;
        }
    }

    public static class CachedPlayerData {
        private final UUID uuid;
        private String name;
        private long firstJoin;
        private long lastSeen;
        private final Map<String, PlayerStats> stats = new HashMap<>();
        private final Map<String, String> kits = new HashMap<>();

        public CachedPlayerData(UUID uuid, String name) {
            this.uuid = uuid;
            this.name = name;
        }

        public UUID getUuid() { return uuid; }
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public long getFirstJoin() { return firstJoin; }
        public void setFirstJoin(long firstJoin) { this.firstJoin = firstJoin; }
        public long getLastSeen() { return lastSeen; }
        public void setLastSeen(long lastSeen) { this.lastSeen = lastSeen; }
        public Map<String, PlayerStats> getStats() { return stats; }
        public Map<String, String> getKits() { return kits; }
    }
}
