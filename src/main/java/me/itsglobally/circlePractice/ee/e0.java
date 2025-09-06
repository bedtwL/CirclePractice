package me.itsglobally.circlePractice.ee;

import me.itsglobally.circlePractice.a;
import me.itsglobally.circlePractice.bb.b4;
import me.itsglobally.circlePractice.ff.f2;
import me.itsglobally.circlePractice.ff.f6;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import top.nontage.nontagelib.utils.inventory.InventoryBuilder;

public class e0 {
    public static void open(Player p) {
        b4 pp = a.getInstance().getPlayerManager().getPlayer(p.getUniqueId());
        if (pp.getState() != b4.PlayerState.SPAWN) {
            p.playSound(p.getLocation(), Sound.ENDERMAN_TELEPORT, 1.0f, 1.0f);
            f6.sendMessage(p, "&cYou are not in the spawn!");
            return;
        }
        InventoryBuilder inv = new InventoryBuilder(27, "Game modes");
        inv.setItem(new f2(Material.IRON_AXE)
                        .setDisplayName("&aFFA")
                        .build(), clickInventoryEvent -> {
                    InventoryClickEvent e = clickInventoryEvent.getEvent();
                    e.setCancelled(true);

                    a.getInstance().getFFAManager().spawn(p);

                }, 13
        );
        p.openInventory(inv.getInventory());
    }
}
