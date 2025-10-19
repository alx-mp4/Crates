package io.github.aleksandarharalanov.crates.crate;

import org.bukkit.block.Block;

import java.util.HashSet;
import java.util.Set;

public final class CrateManager {

    private CrateManager() { }

    private static final Set<String> locked = new HashSet<>();
    private static final Set<String> opened = new HashSet<>();

    private static String key(Block b) {
        return b.getWorld().getName() + ":" + b.getX() + "," + b.getY() + "," + b.getZ();
    }

    public static boolean isLocked(Block b) { return locked.contains(key(b)); }
    public static boolean isOpened(Block b) { return opened.contains(key(b)); }

    public static void lock(Block b)   { locked.add(key(b)); }
    public static void unlock(Block b) { locked.remove(key(b)); }

    public static void markOpened(Block b) {
        opened.add(key(b));
    }

    public static void clear(Block b) {
        String k = key(b);
        opened.remove(k);
        locked.remove(k);
    }
}
