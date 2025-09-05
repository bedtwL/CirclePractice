package me.itsglobally.circlePractice;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.UUID;

public class phapi extends PlaceholderExpansion {

    public static String rqOp(Player p, String params) {
        UUID u = p.getUniqueId();
        switch (params) {
            case "coins" -> {
                return String.valueOf(CirclePractice.getInstance().getEconomyManager().getCoins(u));
            }
            default -> {
                return "";
            }
        }
    }

    public static String nrqOp(OfflinePlayer p, String params) {
        switch (params) {
            case "coins" -> {
                return String.valueOf(CirclePractice.getInstance().getEconomyManager().getCoins(p.getUniqueId()));
            }
            default -> {
                return "";
            }
        }
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
                "stars",
                "coins"
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

}