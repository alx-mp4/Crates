package io.github.aleksandarharalanov.crates.ui;

import io.github.aleksandarharalanov.crates.crate.CrateConfig;
import io.github.aleksandarharalanov.crates.crate.CrateConfig.RewardEntry;
import net.minecraft.server.EntityPlayer;
import net.minecraft.server.ICrafting;
import net.minecraft.server.IInventory;
import net.minecraft.server.InventoryLargeChest;
import net.minecraft.server.ItemStack;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.util.List;

public final class PreviewNmsMenu {

    private PreviewNmsMenu() {}

    public static final String TITLE = "Crate Rewards Preview";

    public static void open(Player player) {
        IInventory backing = buildBacking54();
        List<RewardEntry> rewards = CrateConfig.getRewards();
        open(player, backing, rewards);
    }

    public static void open(Player player, IInventory backing, List<RewardEntry> rewards) {
        EntityPlayer playerHandle = ((CraftPlayer) player).getHandle();

        playerHandle.a(backing);
        int winId = playerHandle.activeContainer.windowId;

        PagedReadOnlyContainerChest ro =
                new PagedReadOnlyContainerChest(playerHandle, playerHandle.inventory, backing, rewards);
        ro.windowId = winId;
        ro.a((ICrafting) playerHandle);
        playerHandle.activeContainer = ro;

        ro.render();
        playerHandle.updateInventory(ro);
    }

    private static IInventory buildBacking54() {
        return new InventoryLargeChest(
                TITLE,
                new SimpleInventory(TITLE, 27),
                new SimpleInventory(TITLE, 27)
        );
    }

    // Helper if prefilled arbitrary inventories elsewhere are to be considered in the future
    public static void fill(IInventory inv, List<RewardEntry> rewards, int from, int to) {
        int slot = 0;
        for (int i = from; i < rewards.size() && i < to; i++) {
            RewardEntry r = rewards.get(i);
            int id = r.material.getId();
            int amt = Math.max(1, r.amount);
            int data = r.data;
            inv.setItem(slot++, new ItemStack(id, amt, data));
        }
    }
}
