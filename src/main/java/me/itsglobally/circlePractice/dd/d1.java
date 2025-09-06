package me.itsglobally.circlePractice.dd;

import me.itsglobally.circlePractice.a;
import me.itsglobally.circlePractice.bb.b0;
import me.itsglobally.circlePractice.ff.f0;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

import java.io.File;
import java.util.*;

public class d1 {

    private final a plugin;
    private final Map<String, b0> arenas;
    private final Random rand;

    private final int arenaSize = 1000; // distance between arenas
    private final int yLevel = 50;
    private final int gridMin = 0; // min grid index
    private final int gridMax = 1000; // max grid index

    private final World world;

    public d1(a plugin) {
        this.plugin = plugin;
        this.arenas = new HashMap<>();
        this.rand = new Random();
        this.world = Bukkit.getWorld("ffa");
    }

    public void createArena(String name, Location pos1, Location pos2, Location spectator) {
        b0 b0 = new b0(name);
        b0.setPos1(pos1);
        b0.setPos2(pos2);
        b0.setSpectatorSpawn(spectator);
        arenas.put(name, b0);
    }

    public b0 getArena(String name) {
        return arenas.get(name);
    }

    public b0 getAvailableArena() {
        List<b0> freeB0s = arenas.values().stream()
                .filter(b0 -> !b0.isInUse() && b0.isComplete())
                .toList();

        if (!freeB0s.isEmpty()) {
            b0 chosen = freeB0s.get(rand.nextInt(freeB0s.size()));
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
        b0 b0 = new b0(arenaName);
        b0.setPos1(player1Spawn);
        b0.setPos2(player2Spawn);
        b0.setSpectatorSpawn(spectator);
        b0.setInUse(true);

        File schemname = new File(plugin.getDataFolder().getAbsoluteFile()+ "plugins/WorldEdit/schematics/duels.schematic");

        try {
            f0.pasteSchematicAt(world, schemname, x, y, z, true);
        } catch (Exception e) {
            e.printStackTrace();
        }

        arenas.put(arenaName, b0);
        return b0;

    }

    public Map<String, b0> getAllArenas() {
        return arenas;
    }

    public void releaseArena(b0 b0) {
        if (b0 != null) b0.setInUse(false);
    }
}
