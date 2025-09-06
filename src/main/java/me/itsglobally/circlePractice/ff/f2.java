package me.itsglobally.circlePractice.ff;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class f2 {

    private final ItemStack item;
    private final ItemMeta meta;


    public f2(Material material) {
        this(material, 1);
    }

    public f2(Material material, int amount) {
        this.item = new ItemStack(material, amount);
        this.meta = this.item.getItemMeta();
    }

    public f2 setDisplayName(String name) {
        meta.setDisplayName(name);
        return this;
    }

    public f2 setLore(List<String> lore) {
        meta.setLore(lore);
        return this;
    }

    public f2 setDurability(short durability) {
        item.setDurability(durability);
        return this;
    }

    public f2 addEnchantment(Enchantment enchantment, int level) {
        meta.addEnchant(enchantment, level, true);
        return this;
    }

    public f2 setUnbreakable(boolean status) {
        meta.spigot().setUnbreakable(status);
        return this;
    }

    public ItemStack build() {
        item.setItemMeta(meta);
        return item;
    }
}
