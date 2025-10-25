package io.github.aleksandarharalanov.crates.crate;

import org.bukkit.block.Block;

import java.util.HashSet;
import java.util.Set;

public class CrateManager {

    private CrateManager() {}

    private static final Set<String> CRATES = new HashSet<>();

    private static String key(Block block) {
        return block.getWorld().getName() + ":" + block.getX() + "," + block.getY() + "," + block.getZ();
    }

    public static boolean isBusy(Block block) {
        return CRATES.contains(key(block));
    }

    public static void add(Block block) {
        CRATES.add(key(block));
    }
    
    public static void remove(Block block) {
        CRATES.remove(key(block));
    }
}
