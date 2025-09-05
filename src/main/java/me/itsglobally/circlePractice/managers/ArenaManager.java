package me.itsglobally.circlePractice.managers;

import me.itsglobally.circlePractice.CirclePractice;
import me.itsglobally.circlePractice.data.Arena;
import me.itsglobally.circlePractice.utils.ArenaPasteWE6;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

import java.util.*;

public class ArenaManager {

    private final CirclePractice plugin;
    private final Map<String, Arena> arenas;
    private final Random rand;

    private final int arenaSize = 1000; // distance between arenas
    private final int yLevel = 50;
    private final int gridMin = 0; // min grid index
    private final int gridMax = 9; // max grid index

    private final World world;

    public ArenaManager(CirclePractice plugin) {
        this.plugin = plugin;
        this.arenas = new HashMap<>();
        this.rand = new Random();
        this.world = Bukkit.getWorld("ffa");
    }

    public void createArena(String name, Location pos1, Location pos2, Location spectator) {
        Arena arena = new Arena(name);
        arena.setPos1(pos1);
        arena.setPos2(pos2);
        arena.setSpectatorSpawn(spectator);
        arenas.put(name, arena);
    }

    public Arena getArena(String name) {
        return arenas.get(name);
    }

    public Arena getAvailableArena() {
        List<Arena> freeArenas = arenas.values().stream()
                .filter(arena -> !arena.isInUse() && arena.isComplete())
                .toList();

        if (!freeArenas.isEmpty()) {
            Arena chosen = freeArenas.get(rand.nextInt(freeArenas.size()));
            chosen.setInUse(true);
            return chosen;
        }

        int xIndex = rand.nextInt(gridMax - gridMin + 1) + gridMin;
        int zIndex = rand.nextInt(gridMax - gridMin + 1) + gridMin;

        int x = xIndex * arenaSize;
        int z = zIndex * arenaSize;
        int y = yLevel;
        Location spectator = new Location(world, x, y, z);
        Location player1Spawn = new Location(world, x - 25, y, z);
        Location player2Spawn = new Location(world, x + 25, y, z);

        String arenaName = "Arena-" + xIndex + "-" + zIndex;
        Arena arena = new Arena(arenaName);
        arena.setPos1(player1Spawn);
        arena.setPos2(player2Spawn);
        arena.setSpectatorSpawn(spectator);
        arena.setInUse(true);



        ArenaPasteWE6.pasteSchematicAt(world);

        arenas.put(arenaName, arena);
        return arena;

    }

    public Map<String, Arena> getAllArenas() {
        return arenas;
    }

    public void releaseArena(Arena arena) {
        if (arena != null) arena.setInUse(false);
    }
}
