package me.itsglobally.circlePractice.utils;

import org.bukkit.inventory.ItemStack;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;

public class InventorySerializer {
    
    public static String serializeInventory(ItemStack[] contents, ItemStack[] armor) {
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            BukkitObjectOutputStream dataOutput = new BukkitObjectOutputStream(outputStream);
            
            // Write contents length and contents
            dataOutput.writeInt(contents.length);
            for (ItemStack item : contents) {
                dataOutput.writeObject(item);
            }
            
            // Write armor length and armor
            dataOutput.writeInt(armor.length);
            for (ItemStack item : armor) {
                dataOutput.writeObject(item);
            }
            
            dataOutput.close();
            return Base64.getEncoder().encodeToString(outputStream.toByteArray());
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public static ItemStack[][] deserializeInventory(String data) {
        if (data == null || data.isEmpty()) {
            return null;
        }
        
        try {
            ByteArrayInputStream inputStream = new ByteArrayInputStream(Base64.getDecoder().decode(data));
            BukkitObjectInputStream dataInput = new BukkitObjectInputStream(inputStream);
            
            // Read contents
            int contentsLength = dataInput.readInt();
            ItemStack[] contents = new ItemStack[contentsLength];
            for (int i = 0; i < contentsLength; i++) {
                contents[i] = (ItemStack) dataInput.readObject();
            }
            
            // Read armor
            int armorLength = dataInput.readInt();
            ItemStack[] armor = new ItemStack[armorLength];
            for (int i = 0; i < armorLength; i++) {
                armor[i] = (ItemStack) dataInput.readObject();
            }
            
            dataInput.close();
            return new ItemStack[][]{contents, armor};
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }
}