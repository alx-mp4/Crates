package io.github.aleksandarharalanov.crates.crate;

import org.bukkit.Material;

public final class Reward {
    public final Material material;
    public final byte data;
    public final int amount;
    public final int weight;
    public final RewardTier tier;

    Reward(Material m, byte d, int a, int w, RewardTier t) {
        material = m; data = d; amount = a; weight = w; tier = t;
    }
}