package io.github.aleksandarharalanov.crates.ui.container;

import io.github.aleksandarharalanov.crates.crate.reward.Reward;
import net.minecraft.server.ContainerChest;
import net.minecraft.server.EntityHuman;
import net.minecraft.server.EntityPlayer;
import net.minecraft.server.IInventory;
import net.minecraft.server.ItemStack;
import org.bukkit.Effect;
import org.bukkit.entity.Player;

import java.util.List;

public final class ContainerPreview extends ContainerChest {

    private final EntityPlayer viewer;
    private final IInventory backing;
    private final List<Reward> rewards;
    private int page = 0;

    // Layout
    private static final int PAGE_CAPACITY = 45; // 5 Rows for rewards (0..44)
    private static final int CONTROL_FROM = 45;  // Bottom row (45..53)
    private static final int CONTROL_TO = 54;

    // Controls
    private static final int SLOT_INFO = 49;     // Center
    private static final int SLOT_PREV = 45;     // Bottom-left
    private static final int SLOT_NEXT = 53;     // Bottom-right

    public ContainerPreview(EntityPlayer viewer,
                            IInventory playerInv,
                            IInventory backing,
                            List<Reward> rewards) {
        super(playerInv, backing);
        this.viewer  = viewer;
        this.backing = backing;
        this.rewards = rewards;
    }

    private int pageCount() {
        return Math.max(1, (rewards.size() + PAGE_CAPACITY - 1) / PAGE_CAPACITY);
    }

    private void setSlot(int slot, int id, int amount, int data) {
        backing.setItem(slot, new ItemStack(id, Math.max(1, amount), data));
    }

    private void clearRange(int fromInclusive, int toExclusive) {
        for (int i = fromInclusive; i < toExclusive; i++) backing.setItem(i, null);
    }

    public void render() {
        clearRange(0, PAGE_CAPACITY);
        int start = page * PAGE_CAPACITY;
        int end = Math.min(rewards.size(), start + PAGE_CAPACITY);
        int slot = 0;
        for (int i = start; i < end; i++) {
            Reward reward = rewards.get(i);
            setSlot(slot++, reward.material.getId(), Math.max(1, reward.amount), reward.data);
        }

        clearRange(CONTROL_FROM, CONTROL_TO);
        boolean hasPrev = page > 0;
        boolean hasNext = page < pageCount() - 1;
        setSlot(SLOT_INFO, 339, page + 1, 0);
        setSlot(SLOT_PREV, hasPrev ? 35 : 36, 1, hasPrev ? 14 : 0);
        setSlot(SLOT_NEXT, hasNext ? 35 : 36, 1, hasNext ? 13 : 0);
        for (int fill : new int[] { 46, 47, 48, 50, 51, 52 }) setSlot(fill, 36, 1, 0);

    }

    @Override
    public ItemStack a(int slot, int button, boolean shift, EntityHuman who) {
        Player player = (Player) viewer.getBukkitEntity();
        switch (slot) {
            case SLOT_PREV:
                if (page > 0) {
                    page--;
                    render();
                    viewer.updateInventory(this);
                    player.playEffect(player.getLocation(), Effect.CLICK2, 0);
                }
                return null;
            case SLOT_NEXT:
                if (page < pageCount() - 1) {
                    page++;
                    render();
                    viewer.updateInventory(this);
                    player.playEffect(player.getLocation(), Effect.CLICK1, 0);
                }
                return null;
            default: return null;
        }
    }

    @Override public ItemStack a(int slotIndex) { return null; }

    @Override public boolean b(EntityHuman human) { return true; }
}
