package me.itsglobally.circlePractice.dd;

import me.itsglobally.circlePractice.a;
import me.itsglobally.circlePractice.bb.b3;
import me.itsglobally.circlePractice.ff.f2;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class d6 {

    private final a plugin;
    private final Map<String, b3> kits;

    public d6(a plugin) {
        this.plugin = plugin;
        this.kits = new HashMap<>();
        loadKits();
        createDefaultKits();
    }

    public void loadKits() {
        FileConfiguration config = plugin.getConfigManager().getKits();
        ConfigurationSection kitsSection = config.getConfigurationSection("kits");

        if (kitsSection == null) return;

        for (String name : kitsSection.getKeys(false)) {
            b3 b3 = new b3(name);
            ConfigurationSection kitSection = kitsSection.getConfigurationSection(name);

            if (kitSection.contains("contents")) {
                List<ItemStack> contentsList = (List<ItemStack>) kitSection.getList("contents");
                if (contentsList != null) b3.setContents(contentsList.toArray(new ItemStack[0]));
            }

            if (kitSection.contains("armor")) {
                List<ItemStack> armorList = (List<ItemStack>) kitSection.getList("armor");
                if (armorList != null) b3.setArmor(armorList.toArray(new ItemStack[0]));
            }

            b3.setEnabled(kitSection.getBoolean("enabled", true));
            b3.setCanBuild(kitSection.getBoolean("canBuild", true));

            kits.put(name, b3);
        }
    }


    private void createDefaultKits() {
        if (!kits.containsKey("NoDebuff")) {
            b3 noDebuff = new b3("NoDebuff");

            // Inventory contents
            ItemStack[] contents = new ItemStack[36];
            // Sword in slot 0
            contents[0] = new f2(Material.DIAMOND_SWORD)
                    .addEnchantment(org.bukkit.enchantments.Enchantment.DAMAGE_ALL, 3) // Sharp 3
                    .addEnchantment(org.bukkit.enchantments.Enchantment.DURABILITY, 3)  // Unbreaking 3
                    .setUnbreakable(true)
                    .build();

            // Slots 1 → instant health potion
            contents[1] = new f2(Material.POTION)
                    .setDurability((short) 16421) // Instant Health II
                    .build();

            // Slots 2 → Speed potion
            contents[2] = new f2(Material.POTION)
                    .setDurability((short) 8194) // Speed II
                    .build();

            // Slots 3 → Fire Resistance potion
            contents[3] = new f2(Material.POTION)
                    .setDurability((short) 8195) // Fire Resistance
                    .build();

            // Remaining slots → instant health potion
            for (int i = 4; i < 36; i++) {
                contents[i] = new f2(Material.POTION)
                        .setDurability((short) 16421)
                        .build();
            }

            // Armor
            ItemStack[] armor = new ItemStack[4];
            armor[3] = new f2(Material.DIAMOND_HELMET)
                    .addEnchantment(org.bukkit.enchantments.Enchantment.PROTECTION_ENVIRONMENTAL, 2)
                    .addEnchantment(org.bukkit.enchantments.Enchantment.DURABILITY, 3)
                    .setUnbreakable(true)
                    .build();
            armor[2] = new f2(Material.DIAMOND_CHESTPLATE)
                    .addEnchantment(org.bukkit.enchantments.Enchantment.PROTECTION_ENVIRONMENTAL, 2)
                    .addEnchantment(org.bukkit.enchantments.Enchantment.DURABILITY, 3)
                    .setUnbreakable(true)
                    .build();
            armor[1] = new f2(Material.DIAMOND_LEGGINGS)
                    .addEnchantment(org.bukkit.enchantments.Enchantment.PROTECTION_ENVIRONMENTAL, 2)
                    .addEnchantment(org.bukkit.enchantments.Enchantment.DURABILITY, 3)
                    .setUnbreakable(true)
                    .build();
            armor[0] = new f2(Material.DIAMOND_BOOTS)
                    .addEnchantment(org.bukkit.enchantments.Enchantment.PROTECTION_ENVIRONMENTAL, 2)
                    .addEnchantment(org.bukkit.enchantments.Enchantment.DURABILITY, 3)
                    .setUnbreakable(true)
                    .build();

            noDebuff.setContents(contents);
            noDebuff.setArmor(armor);
            noDebuff.setCanBuild(false);
            kits.put("NoDebuff", noDebuff);
        }
        if (!kits.containsKey("FFA")) {
            b3 FFA = new b3("FFA");


            ItemStack[] contents = new ItemStack[36];

            contents[0] = new f2(Material.WOOD_SWORD)
                    .addEnchantment(org.bukkit.enchantments.Enchantment.DAMAGE_ALL, 2)
                    .addEnchantment(org.bukkit.enchantments.Enchantment.DURABILITY, 3)
                    .build();
            contents[1] = new f2(Material.BOW)
                    .addEnchantment(Enchantment.ARROW_KNOCKBACK, 1)
                    .addEnchantment(Enchantment.ARROW_DAMAGE, 1)
                    .setUnbreakable(true)
                    .build();
            contents[2] = new f2(Material.ARROW, 16)
                    .build();
            contents[3] = new f2(Material.ENDER_PEARL)
                    .build();
            contents[4] = new f2(Material.SANDSTONE, 64)
                    .build();
            contents[5] = new f2(Material.STONE_PICKAXE)
                    .addEnchantment(Enchantment.DIG_SPEED, 2)
                    .setUnbreakable(true)
                    .build();

            ItemStack[] armor = new ItemStack[4];
            armor[3] = new f2(Material.LEATHER_HELMET)
                    .addEnchantment(org.bukkit.enchantments.Enchantment.PROTECTION_ENVIRONMENTAL, 2)
                    .addEnchantment(org.bukkit.enchantments.Enchantment.DURABILITY, 3)
                    .setUnbreakable(true)
                    .build();
            armor[2] = new f2(Material.LEATHER_CHESTPLATE)
                    .addEnchantment(org.bukkit.enchantments.Enchantment.PROTECTION_ENVIRONMENTAL, 2)
                    .addEnchantment(org.bukkit.enchantments.Enchantment.DURABILITY, 3)
                    .setUnbreakable(true)
                    .build();
            armor[1] = new f2(Material.LEATHER_LEGGINGS)
                    .addEnchantment(org.bukkit.enchantments.Enchantment.PROTECTION_ENVIRONMENTAL, 2)
                    .addEnchantment(org.bukkit.enchantments.Enchantment.DURABILITY, 3)
                    .setUnbreakable(true)
                    .build();
            armor[0] = new f2(Material.LEATHER_BOOTS)
                    .addEnchantment(org.bukkit.enchantments.Enchantment.PROTECTION_ENVIRONMENTAL, 2)
                    .addEnchantment(org.bukkit.enchantments.Enchantment.DURABILITY, 3)
                    .setUnbreakable(true)
                    .build();

            FFA.setContents(contents);
            FFA.setArmor(armor);
            FFA.setCanBuild(true);
            kits.put("FFA", FFA);
        }
        saveKits();
    }

    private void saveKits() {
        FileConfiguration config = plugin.getConfigManager().getKits();

        for (b3 b3 : kits.values()) {
            String path = "kits." + b3.getName();
            config.set(path + ".contents", b3.getContents());
            config.set(path + ".armor", b3.getArmor());
            config.set(path + ".enabled", b3.isEnabled());
            config.set(path + ".canBuild", b3.canBuild());
        }

        plugin.getConfigManager().saveKits();
    }

    public void applyKit(Player player, String kitName) {
        b3 b3 = kits.get(kitName);
        if (b3 == null) return;

        player.getInventory().clear();
        player.getInventory().setArmorContents(null);

        if (b3.getContents() != null) {
            player.getInventory().setContents(b3.getContents());
        }
        if (b3.getArmor() != null) {
            player.getInventory().setArmorContents(b3.getArmor());
        }

        player.updateInventory();
    }

    public boolean kitExists(String name) {
        return kits.containsKey(name);
    }

    public b3 getKit(String name) {
        return kits.get(name);
    }

    public Map<String, b3> getAllKits() {
        return kits;
    }

    public void saveKit(String name, ItemStack[] contents, ItemStack[] armor) {
        b3 b3 = kits.getOrDefault(name, new b3(name));
        b3.setContents(contents);
        b3.setArmor(armor);
        kits.put(name, b3);
        saveKits();
    }
}