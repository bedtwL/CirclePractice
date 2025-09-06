package me.itsglobally.circlePractice.dd;

import me.itsglobally.circlePractice.a;
import me.itsglobally.circlePractice.bb.b4;
import net.luckperms.api.model.user.User;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class d5 {

    private final a plugin;
    private final Map<UUID, b4> players;

    public d5(a plugin) {
        this.plugin = plugin;
        this.players = new HashMap<>();
    }

    public b4 getPlayer(UUID uuid) {
        return players.get(uuid);
    }

    public b4 getPlayer(Player player) {
        return getPlayer(player.getUniqueId());
    }

    public void addPlayer(Player player) {
        UUID uuid = player.getUniqueId();
        if (!players.containsKey(uuid)) {
            players.put(uuid, new b4(uuid, player.getName()));
            plugin.getFileDataManager().savePlayerData(uuid, player.getName());
        }
    }

    public void removePlayer(UUID uuid) {
        players.remove(uuid);
    }

    public Map<UUID, b4> getAllPlayers() {
        return players;
    }

    public String getPrefix(Player p) {
        User user = plugin.getLuckPerms().getUserManager().getUser(p.getUniqueId());
        return user.getCachedData().getMetaData().getPrefix();
    }

    public String getPrefixColor(Player p) {
        User user = plugin.getLuckPerms().getUserManager().getUser(p.getUniqueId());
        return (user.getCachedData().getMetaData().getMetaValue("prefixcolor") == null) ? user.getCachedData().getMetaData().getMetaValue("prefixcolor") : getPrefix(p).substring(0, 2);
    }

    public String getPrefixedName(Player p) {
        return getPrefix(p) + p.getName();
    }



}