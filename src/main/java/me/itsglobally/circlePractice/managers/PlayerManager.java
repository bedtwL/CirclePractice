package me.itsglobally.circlePractice.managers;

import me.itsglobally.circlePractice.CirclePractice;
import me.itsglobally.circlePractice.data.PracticePlayer;
import net.luckperms.api.model.user.User;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayerManager {

    private final CirclePractice plugin;
    private final Map<UUID, PracticePlayer> players;

    public PlayerManager(CirclePractice plugin) {
        this.plugin = plugin;
        this.players = new HashMap<>();
    }

    public PracticePlayer getPlayer(UUID uuid) {
        return players.get(uuid);
    }

    public PracticePlayer getPlayer(Player player) {
        return getPlayer(player.getUniqueId());
    }

    public void addPlayer(Player player) {
        UUID uuid = player.getUniqueId();
        if (!players.containsKey(uuid)) {
            players.put(uuid, new PracticePlayer(uuid, player.getName()));
            plugin.getFileDataManager().savePlayerData(uuid, player.getName());
        }
    }

    public void removePlayer(UUID uuid) {
        players.remove(uuid);
    }

    public Map<UUID, PracticePlayer> getAllPlayers() {
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