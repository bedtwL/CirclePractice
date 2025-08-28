package me.itsglobally.circlePractice.data;

import me.itsglobally.circlePractice.CirclePractice;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

public class FileDataManager {
    
    private final CirclePractice plugin;
    private FileConfiguration playerData;
    private File playerDataFile;
    
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
    
    public void savePlayerData(UUID uuid, String name) {
        String path = "players." + uuid.toString();
        playerData.set(path + ".name", name);
        playerData.set(path + ".first-join", System.currentTimeMillis());
        playerData.set(path + ".last-seen", System.currentTimeMillis());
        savePlayerDataFile();
    }
    
    public void updatePlayerStats(UUID uuid, String kit, boolean won, int eloChange) {
        String path = "players." + uuid.toString() + ".stats." + kit;
        
        int currentWins = playerData.getInt(path + ".wins", 0);
        int currentLosses = playerData.getInt(path + ".losses", 0);
        int currentElo = playerData.getInt(path + ".elo", 1000);
        
        if (won) {
            playerData.set(path + ".wins", currentWins + 1);
        } else {
            playerData.set(path + ".losses", currentLosses + 1);
        }
        
        playerData.set(path + ".elo", currentElo + eloChange);
        savePlayerDataFile();
    }
    
    public PlayerStats getPlayerStats(UUID uuid, String kit) {
        String path = "players." + uuid.toString() + ".stats." + kit;
        
        int wins = playerData.getInt(path + ".wins", 0);
        int losses = playerData.getInt(path + ".losses", 0);
        int elo = playerData.getInt(path + ".elo", 1000);
        
        return new PlayerStats(wins, losses, elo);
    }
    
    public void saveKitContents(UUID uuid, String kit, String serializedContents) {
        String path = "players." + uuid.toString() + ".kits." + kit;
        playerData.set(path, serializedContents);
        savePlayerDataFile();
    }
    
    public String getKitContents(UUID uuid, String kit) {
        String path = "players." + uuid.toString() + ".kits." + kit;
        return playerData.getString(path);
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
    }
    
    public static class PlayerStats {
        private int wins;
        private int losses;
        private int elo;
        
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
}