package me.itsglobally.circlePractice;

import me.itsglobally.circlePractice.bb.b2;
import me.itsglobally.circlePractice.dd.*;
import me.itsglobally.circlePractice.ff.f3;
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

public class a extends JavaPlugin {

    public static String serverName = "practice";
    private static a instance;
    private static BukkitAudiences adventure;
    private static LuckPerms luckPerms;
    private b2 b2;
    private d5 d5;
    private d3 d3;
    private d7 d7;
    private d1 d1;
    private d6 d6;
    private f3 f3;
    private d4 d4;
    private d9 d9;
    private d8 d8;

    public static a getInstance() {
        return instance;
    }

    public static Audience audience(Player player) {
        return adventure.player(player);
    }

    @Override
    public void onEnable() {

        d2 wss;
        try {
            wss = new d2(new URI("ws://172.18.0.1:25502"));
            wss.connectBlocking();
        } catch (Exception e) {
            e.printStackTrace();
            this.getPluginLoader().disablePlugin(this);
        }
        if (Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
            new b().register();
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

        f3 = new f3(this);
        f3.setupConfig();

        b2 = new b2(this);
        b2.initialize();

        d5 = new d5(this);
        d1 = new d1(this);
        d6 = new d6(this);
        d3 = new d3(this);
        d7 = new d7(this);
        d4 = new d4(this);
        d9 = new d9(this);
        d8 = new d8(this);
    }

    @Override
    public void onDisable() {
        getFileDataManager().saveAllCached();
        getLogger().info("CirclePractice has been disabled!");
    }

    public b2 getFileDataManager() {
        return b2;
    }

    public d5 getPlayerManager() {
        return d5;
    }

    public d3 getDuelManager() {
        return d3;
    }

    public d7 getQueueManager() {
        return d7;
    }

    public d1 getArenaManager() {
        return d1;
    }

    public d6 getKitManager() {
        return d6;
    }

    public f3 getConfigManager() {
        return f3;
    }

    public d4 getFFAManager() {
        return d4;
    }

    public d9 getPluginManager() {
        return d9;
    }

    public d8 getEconomyManager() {
        return d8;
    }

    public LuckPerms getLuckPerms() {
        return luckPerms;
    }
}