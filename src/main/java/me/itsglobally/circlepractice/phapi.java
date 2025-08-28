package me.itsglobally.circlePractice;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class phapi extends PlaceholderExpansion {
    private final CirclePractice plugin; //

    public phapi(CirclePractice plugin) {
        this.plugin = plugin;
    }
    @Override
    public String getAuthor() {
        return "ItsGlobally";
    }

    @Override
    public String getIdentifier() {
        return "CircleFFA";
    }

    @Override
    public String getVersion() {
        return "1.0.0";
    }

    @Override
    public String onRequest(OfflinePlayer player, String params) {
        List<String> requireOnline = List.of(
                "kills",
                "deaths",
                "ks",
                "xp",
                "stars"
        );
        if (requireOnline.contains(params)) {
            if (!(player instanceof Player p)) {
                return "";
            }
            return rqOp(p, params);
        } else {
            return nrqOp(player, params);
        }
    }

    @Override
    public String onPlaceholderRequest(Player player, String params) {
        return onRequest(player, params);
    }
    public static String rqOp(Player p, String params) {
        UUID u = p.getUniqueId();
        switch (params) {
            default -> {
                return "";
            }
        }
    }
    public static String nrqOp(OfflinePlayer p, String params) {
        switch (params) {
            default -> {
                return "";
            }
        }
    }

}