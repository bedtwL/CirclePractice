package me.itsglobally.circlePractice;

import me.itsglobally.circlePractice.commands.*;
import me.itsglobally.circlePractice.data.FileDataManager;
import me.itsglobally.circlePractice.listeners.DuelListener;
import me.itsglobally.circlePractice.listeners.InventoryListener;
import me.itsglobally.circlePractice.listeners.PlayerListener;
import me.itsglobally.circlePractice.managers.*;
import me.itsglobally.circlePractice.utils.ConfigManager;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import top.nontage.nontagelib.command.NontageCommandLoader;
import top.nontage.nontagelib.listener.ListenerRegister;

import java.net.URI;

public class CirclePractice extends JavaPlugin {

    private static CirclePractice instance;
    private static BukkitAudiences adventure;
    private static LuckPerms luckPerms;
    private FileDataManager fileDataManager;
    private PlayerManager playerManager;
    private DuelManager duelManager;
    private QueueManager queueManager;
    private ArenaManager arenaManager;
    private KitManager kitManager;
    private ConfigManager configManager;
    private FFAManager ffaManager;
    private PluginManager pluginManager;

    public static CirclePractice getInstance() {
        return instance;
    }

    public static Audience audience(Player player) {
        return adventure.player(player);
    }

    public static String serverName = "practice";

    @Override
    public void onEnable() {

        DaemonManager wss;
        try {
            wss = new DaemonManager(new URI("ws://172.18.0.1:25502"));
            wss.connectBlocking();
        } catch (Exception e) {
            e.printStackTrace();
            this.getPluginLoader().disablePlugin(this);
        }
        if (Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
            new phapi(this).register();
            getLogger().info("reg papi");
        }

        instance = this;

        initManagers();

        NontageCommandLoader.registerAll(this);
        ListenerRegister.registerAll(this);
        getLogger().info("CirclePractice has been enabled!");
    }

    private void initManagers() {
        luckPerms = LuckPermsProvider.get();
        adventure = BukkitAudiences.create(this);

        configManager = new ConfigManager(this);
        configManager.setupConfig();

        fileDataManager = new FileDataManager(this);
        fileDataManager.initialize();

        playerManager = new PlayerManager(this);
        arenaManager = new ArenaManager(this);
        kitManager = new KitManager(this);
        duelManager = new DuelManager(this);
        queueManager = new QueueManager(this);
        ffaManager = new FFAManager(this);
        pluginManager = new PluginManager(this);
    }

    @Override
    public void onDisable() {
        getFileDataManager().saveAllCached();
        getLogger().info("CirclePractice has been disabled!");
    }

    public FileDataManager getFileDataManager() {
        return fileDataManager;
    }

    public PlayerManager getPlayerManager() {
        return playerManager;
    }

    public DuelManager getDuelManager() {
        return duelManager;
    }

    public QueueManager getQueueManager() {
        return queueManager;
    }

    public ArenaManager getArenaManager() {
        return arenaManager;
    }

    public KitManager getKitManager() {
        return kitManager;
    }

    public ConfigManager getConfigManager() {
        return configManager;
    }

    public FFAManager getFFAManager() {
        return ffaManager;
    }

    public PluginManager getPluginManager() {
        return pluginManager;
    }

    public LuckPerms getLuckPerms() {
        return luckPerms;
    }
}