package io.github.aleksandarharalanov.crates.crate.reward;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public final class Reward {

    public final ItemStack itemStack;
    public final Material material;
    public final byte data;
    public final int amount;
    public final int weight;
    public final RewardTier tier;

    Reward(Material m, byte d, int a, int w, RewardTier t) {
        itemStack = new ItemStack(m, a, (short) 0, d);
        material = m;
        data = d;
        amount = a;
        weight = w;
        tier = t;
    }
}