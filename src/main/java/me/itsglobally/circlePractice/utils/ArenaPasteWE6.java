package me.itsglobally.circlePractice.utils;

// ArenaPasteWE6.java

import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.bukkit.BukkitWorld;
import com.sk89q.worldedit.CuboidClipboard;
import com.sk89q.worldedit.schematic.MCEditSchematicFormat;
import org.bukkit.World;

import java.io.File;

public class ArenaPasteWE6 {

    public static void pasteSchematicAt(World bukkitWorld,
                                        File schematicFile,
                                        int x, int y, int z,
                                        boolean pasteAir) throws Exception {

        CuboidClipboard clipboard = MCEditSchematicFormat
                .getFormat(schematicFile)
                .load(schematicFile);

        EditSession editSession = new EditSession(new BukkitWorld(bukkitWorld), Integer.MAX_VALUE);

        // Align schematic origin to the target corner
        Vector pasteAt = new Vector(x, y, z).subtract(clipboard.getOffset());

        clipboard.paste(editSession, pasteAt, !pasteAir);
        editSession.flushQueue();
    }
}
