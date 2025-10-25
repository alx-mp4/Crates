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

public final class ContainerRoulette extends ContainerChest {

    private final EntityPlayer viewer;
    private final IInventory backing;
    private final List<Reward> rewards;

    private static final int TOP_GOLD_MARKER = 4;
    private static final int BOT_GOLD_MARKER = 22;
    private static final int MID_ROW_START = 10;
    private static final int MID_ROW_END = 16;

    public ContainerRoulette(EntityPlayer viewer,
                             IInventory playerInv,
                             IInventory backing,
                             List<Reward> rewards) {
        super(playerInv, backing);
        this.viewer = viewer;
        this.backing = backing;
        this.rewards = rewards;
    }

    private void setSlot(int slot, int id, int amount, int data) {
        backing.setItem(slot, new ItemStack(id, Math.max(1, amount), data));
    }

    public void render(int offset) {
        int rewardCount = rewards.size();
        for (int i = 0; i < backing.getSize(); i++) setSlot(i, 34, 1, 0);
        for (int i = MID_ROW_START; i <= MID_ROW_END; i++) {
            int rewardIndex = (offset + i - MID_ROW_START) % rewardCount;
            Reward reward = rewards.get(rewardIndex);
            setSlot(i, reward.material.getId(), reward.amount, reward.data);
        }
        setSlot(TOP_GOLD_MARKER, 41, 1, 0);
        setSlot(BOT_GOLD_MARKER, 41, 1, 0);

        viewer.updateInventory(this);
    }

    public void playTickSound() {
        Player player = (Player) viewer.getBukkitEntity();
        player.playEffect(player.getLocation(), Effect.CLICK2, 0);
    }

    @Override
    public ItemStack a(int slot, int button, boolean shift, EntityHuman who) { return null; }

    @Override
    public ItemStack a(int slotIndex) { return null; }

    @Override
    public boolean b(EntityHuman human) { return true; }
}
