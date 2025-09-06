package me.itsglobally.circlePractice.bb;

import me.itsglobally.circlePractice.ff.f1;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class b4 {

    private final UUID uuid;
    private final String name;
    private final Map<String, String> kitContents; // Now stores serialized data
    private PlayerState state;
    private b1 currentB1;
    private String queuedKit;
    private long queueStartTime;
    private ItemStack[] previousInventory;
    private ItemStack[] previousArmor;

    public b4(UUID uuid, String name) {
        this.uuid = uuid;
        this.name = name;
        this.state = PlayerState.SPAWN;
        this.kitContents = new HashMap<>();
    }

    public UUID getUuid() {
        return uuid;
    }

    public String getName() {
        return name;
    }

    public PlayerState getState() {
        return state;
    }

    public void setState(PlayerState state) {
        this.state = state;
    }

    public b1 getCurrentDuel() {
        return currentB1;
    }

    public void setCurrentDuel(b1 b1) {
        this.currentB1 = b1;
    }

    public String getQueuedKit() {
        return queuedKit;
    }

    public void setQueuedKit(String kit) {
        this.queuedKit = kit;
    }

    public long getQueueStartTime() {
        return queueStartTime;
    }

    public void setQueueStartTime(long time) {
        this.queueStartTime = time;
    }

    public boolean isInQueue() {
        return state == PlayerState.QUEUE;
    }

    public boolean isInDuel() {
        return state == PlayerState.DUEL;
    }

    public boolean isInFFA() {
        return state == PlayerState.FFA;
    }

    public boolean isSpectating() {
        return state == PlayerState.SPECTATING;
    }

    public void saveInventory(Player player) {
        previousInventory = player.getInventory().getContents().clone();
        previousArmor = player.getInventory().getArmorContents().clone();
    }

    public void restoreInventory(Player player) {
        if (previousInventory != null) {
            player.getInventory().setContents(previousInventory);
        }
        if (previousArmor != null) {
            player.getInventory().setArmorContents(previousArmor);
        }
    }

    public void saveKitContents(String kit, ItemStack[] contents, ItemStack[] armor) {
        String serialized = f1.serializeInventory(contents, armor);
        if (serialized != null) {
            kitContents.put(kit, serialized);
        }
    }

    public ItemStack[] getKitContents(String kit) {
        String serialized = kitContents.get(kit);
        if (serialized != null) {
            ItemStack[][] deserialized = f1.deserializeInventory(serialized);
            if (deserialized != null) {
                // Combine contents and armor into single array for backward compatibility
                ItemStack[] combined = new ItemStack[deserialized[0].length + deserialized[1].length];
                System.arraycopy(deserialized[0], 0, combined, 0, deserialized[0].length);
                System.arraycopy(deserialized[1], 0, combined, deserialized[0].length, deserialized[1].length);
                return combined;
            }
        }
        return null;
    }

    public Player getPlayer() {
        return Bukkit.getPlayer(uuid);
    }

    public enum PlayerState {
        SPAWN, QUEUE, DUEL, SPECTATING, EDITING, FFA
    }
}