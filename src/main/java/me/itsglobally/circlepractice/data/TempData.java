package me.itsglobally.circlePractice.data;

import java.util.HashMap;
import java.util.UUID;

public class TempData {
    private static final HashMap<UUID, UUID> lastHit = new HashMap<>();
    private static final HashMap<UUID, Long> ks = new HashMap<>();
    private static final HashMap<UUID, Boolean> build = new HashMap<>();

    public static UUID getLastHit(UUID vic) {
        return lastHit.getOrDefault(vic, null);
    }

    public static void setLastHit(UUID vic, UUID dmger) {
        lastHit.put(vic, dmger);
    }

    public static Long getKs(UUID vic) {
        return ks.getOrDefault(vic, 0L);
    }

    public static void setKs(UUID vic, long k) {
        ks.put(vic, k);
    }

    public static void addtKs(UUID vic, long k) {
        ks.put(vic, getKs(vic) + k);
    }

    public static Boolean getBuild(UUID vic) {
        return build.getOrDefault(vic, false);
    }

    public static void toggleBuild(UUID vic) {
        build.put(vic, !getBuild(vic));
    }
}
