package io.github.aleksandarharalanov.crates.ui;

import io.github.aleksandarharalanov.crates.crate.CrateConfig.RewardEntry;
import net.minecraft.server.ContainerChest;
import net.minecraft.server.EntityHuman;
import net.minecraft.server.EntityPlayer;
import net.minecraft.server.IInventory;
import net.minecraft.server.ItemStack;

import java.util.List;

public final class PagedReadOnlyContainerChest extends ContainerChest {

    private final EntityPlayer viewer;
    private final IInventory backing;
    private final List<RewardEntry> rewards;
    private int page = 0;

    // Layout
    private static final int PAGE_CAPACITY = 45;       // 5 rows for rewards (0..44)
    private static final int CONTROL_FROM  = 45;       // bottom row (45..53)
    private static final int CONTROL_TO    = 54;

    // Controls
    private static final int SLOT_INFO = 49;           // center
    private static final int SLOT_PREV = 52;           // bottom-right-1
    private static final int SLOT_NEXT = 53;           // bottom-right

    public PagedReadOnlyContainerChest(EntityPlayer viewer,
                                       IInventory playerInv,
                                       IInventory backing,
                                       List<RewardEntry> rewards) {
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
        int end   = Math.min(rewards.size(), start + PAGE_CAPACITY);
        int slot  = 0;
        for (int i = start; i < end; i++) {
            RewardEntry r = rewards.get(i);
            setSlot(slot++, r.material.getId(), Math.max(1, r.amount), r.data);
        }

        clearRange(CONTROL_FROM, CONTROL_TO);
        boolean hasPrev = page > 0;
        boolean hasNext = page < pageCount() - 1;
        setSlot(SLOT_INFO, 340, page + 1, 0);
        if (hasPrev) setSlot(SLOT_PREV, 35, 1, 14);
        if (hasNext) setSlot(SLOT_NEXT, 35, 1, 5);
    }

    @Override
    public ItemStack a(int slot, int button, boolean shift, EntityHuman who) {
        switch (slot) {
            case SLOT_PREV:
                if (page > 0) {
                    page--; render();
                    viewer.updateInventory(this);
                }
                return null;
            case SLOT_NEXT:
                if (page < pageCount() - 1) {
                    page++;
                    render();
                    viewer.updateInventory(this);
                }
                return null;
            default: return null;
        }
    }

    @Override public ItemStack a(int slotIndex) { return null; }

    @Override public boolean b(EntityHuman human) { return true; }
}
