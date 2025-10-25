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

    public Reward(Material material, byte data, int amount, int weight, RewardTier tier) {
        this.itemStack = new ItemStack(material, amount, (short) 0, data);
        this.material = material;
        this.data = data;
        this.amount = amount;
        this.weight = weight;
        this.tier = tier;
    }
}