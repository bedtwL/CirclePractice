package me.itsglobally.circlePractice.utils;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class ItemBuilder {
    
    private ItemStack item;
    private ItemMeta meta;
    
    public ItemBuilder(Material material) {
        this(material, 1);
    }
    
    public ItemBuilder(Material material, int amount) {
        item = new ItemStack(material, amount);
        meta = item.getItemMeta();
    }
    
    public ItemBuilder setDisplayName(String name) {
        meta.setDisplayName(name);
        return this;
    }
    
    public ItemBuilder setLore(List<String> lore) {
        meta.setLore(lore);
        return this;
    }
    
    public ItemBuilder setDurability(short durability) {
        item.setDurability(durability);
        return this;
    }
    
    public ItemBuilder addEnchantment(Enchantment enchantment, int level) {
        meta.addEnchant(enchantment, level, true);
        return this;
    }
    
    public ItemStack build() {
        item.setItemMeta(meta);
        return item;
    }
}