package me.itsglobally.circlePractice.dd;

import me.itsglobally.circlePractice.a;
import me.itsglobally.circlePractice.bb.b4;
import me.itsglobally.circlePractice.ff.f6;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

public class d7 {

    private final a plugin;
    private final Map<String, Queue<b4>> queues; // kit -> queue

    public d7(a plugin) {
        this.plugin = plugin;
        this.queues = new HashMap<>();

        startMatchmaking();
    }

    public void joinQueue(Player player, String kit) {
        b4 b4 = plugin.getPlayerManager().getPlayer(player);

        if (b4.getState() != me.itsglobally.circlePractice.bb.b4.PlayerState.SPAWN) {
            f6.sendMessage(player, "&cYou cannot join a queue right now!");
            return;
        }

        if (!plugin.getKitManager().kitExists(kit)) {
            f6.sendMessage(player, "&cThat kit doesn't exist!");
            return;
        }

        // Add to queue
        queues.computeIfAbsent(kit, k -> new LinkedList<>()).add(b4);
        b4.setState(me.itsglobally.circlePractice.bb.b4.PlayerState.QUEUE);
        b4.setQueuedKit(kit);
        b4.setQueueStartTime(System.currentTimeMillis());

        f6.sendMessage(player, "&aYou joined the &e" + kit + " &aqueue!");
        updateQueueInfo(player, kit);
    }

    public void leaveQueue(Player player) {
        b4 b4 = plugin.getPlayerManager().getPlayer(player);

        if (!b4.isInQueue()) {
            f6.sendMessage(player, "&cYou're not in a queue!");
            return;
        }

        String kit = b4.getQueuedKit();
        Queue<b4> queue = queues.get(kit);
        if (queue != null) {
            queue.remove(b4);
        }

        b4.setState(me.itsglobally.circlePractice.bb.b4.PlayerState.SPAWN);
        b4.setQueuedKit(null);
        b4.setQueueStartTime(0);

        f6.sendMessage(player, "&cYou left the queue!");
    }

    private void startMatchmaking() {
        new BukkitRunnable() {
            @Override
            public void run() {
                for (Map.Entry<String, Queue<b4>> entry : queues.entrySet()) {
                    String kit = entry.getKey();
                    Queue<b4> queue = entry.getValue();

                    // Remove offline players
                    queue.removeIf(pp -> {
                        Player p = Bukkit.getPlayer(pp.getUuid());
                        return p == null || !p.isOnline();
                    });

                    // Match players
                    while (queue.size() >= 2) {
                        b4 p1 = queue.poll();
                        b4 p2 = queue.poll();

                        Player player1 = Bukkit.getPlayer(p1.getUuid());
                        Player player2 = Bukkit.getPlayer(p2.getUuid());

                        if (player1 != null && player2 != null && player1.isOnline() && player2.isOnline()) {
                            plugin.getDuelManager().startDuel(player1, player2, kit);
                        }
                    }

                    // Update queue info for remaining players
                    for (b4 pp : queue) {
                        Player p = Bukkit.getPlayer(pp.getUuid());
                        if (p != null && p.isOnline()) {
                            updateQueueInfo(p, kit);
                        }
                    }
                }
            }
        }.runTaskTimer(plugin, 0L, 20L); // Run every second
    }

    private void updateQueueInfo(Player player, String kit) {
        Queue<b4> queue = queues.get(kit);
        if (queue == null) return;
    }
}