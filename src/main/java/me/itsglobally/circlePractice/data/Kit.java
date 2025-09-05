package me.itsglobally.circlePractice.data;

import org.bukkit.inventory.ItemStack;

public class Kit {

    private final String name;
    private ItemStack[] contents;
    private ItemStack[] armor;
    private boolean enabled;
    private boolean canBuild;

    public Kit(String name) {
        this.name = name;
        this.enabled = true;
        this.canBuild = false;
    }

    public String getName() {
        return name;
    }

    public ItemStack[] getContents() {
        return contents;
    }

    public void setContents(ItemStack[] contents) {
        this.contents = contents;
    }

    public ItemStack[] getArmor() {
        return armor;
    }

    public void setArmor(ItemStack[] armor) {
        this.armor = armor;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }


    public boolean canBuild() {
        return canBuild;
    }

    public void setCanBuild(boolean canBuild) {
        this.canBuild = canBuild;
    }
}