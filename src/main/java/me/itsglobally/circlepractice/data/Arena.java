package me.itsglobally.circlePractice.data;

import org.bukkit.Location;

public class Arena {
    
    private final String name;
    private Location pos1;
    private Location pos2;
    private Location spectatorSpawn;
    private boolean inUse;
    
    public Arena(String name) {
        this.name = name;
        this.inUse = false;
    }
    
    public String getName() { return name; }
    public Location getPos1() { return pos1; }
    public void setPos1(Location pos1) { this.pos1 = pos1; }
    public Location getPos2() { return pos2; }
    public void setPos2(Location pos2) { this.pos2 = pos2; }
    public Location getSpectatorSpawn() { return spectatorSpawn; }
    public void setSpectatorSpawn(Location spectatorSpawn) { this.spectatorSpawn = spectatorSpawn; }
    public boolean isInUse() { return inUse; }
    public void setInUse(boolean inUse) { this.inUse = inUse; }
    
    public boolean isComplete() {
        return pos1 != null && pos2 != null && spectatorSpawn != null;
    }
}