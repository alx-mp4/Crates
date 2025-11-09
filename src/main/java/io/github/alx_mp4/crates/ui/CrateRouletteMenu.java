package io.github.aleksandarharalanov.crates.ui;

import io.github.aleksandarharalanov.crates.Crates;
import io.github.aleksandarharalanov.crates.crate.CrateConfig;
import io.github.aleksandarharalanov.crates.crate.CrateHandler;
import io.github.aleksandarharalanov.crates.crate.reward.Reward;
import io.github.aleksandarharalanov.crates.crate.reward.RewardHandler;
import io.github.aleksandarharalanov.crates.ui.container.ContainerRoulette;
import net.minecraft.server.EntityPlayer;
import net.minecraft.server.ICrafting;
import net.minecraft.server.IInventory;
import net.minecraft.server.Packet101CloseWindow;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.security.SecureRandom;
import java.util.List;
import java.util.Random;

public final class CrateRouletteMenu {

    private CrateRouletteMenu() {}

    private static final Random random = new SecureRandom();
    private static final String TITLE = "              Spinning...";
    private static final int SIZE = 27;

    private static final int SPIN_CYCLES_MIN = 50;
    private static final int SPIN_CYCLES_MAX = 100;
    private static final int CENTER_OFFSET = 3;

    public static void playAnimation(Player player,
                                     Block crate,
                                     List<Reward> rewards,
                                     RewardHandler.ChosenReward chosenReward) {
        int cycles = random.nextInt(SPIN_CYCLES_MAX - SPIN_CYCLES_MIN) + SPIN_CYCLES_MIN;
        playAnimation(player, crate, rewards, chosenReward, cycles);
    }

    public static void playAnimation(Player player,
                                     Block crate,
                                     List<Reward> rewards,
                                     RewardHandler.ChosenReward chosenReward,
                                     int cycles) {
        if (!CrateConfig.getRouletteAnimation() || rewards == null || rewards.isEmpty()) {
            CrateHandler.crateEnd(crate, player, chosenReward.reward);
            return;
        }

        EntityPlayer handle = ((CraftPlayer) player).getHandle();
        IInventory backing = new SimpleInventory(TITLE, SIZE);
        handle.a(backing);
        int winId = handle.activeContainer.windowId;

        final ContainerRoulette cr = new ContainerRoulette(handle, handle.inventory, backing, rewards);
        cr.windowId = winId;
        cr.a((ICrafting) handle);
        handle.activeContainer = cr;

        final int rewardCount = rewards.size();
        final int targetIndex = clamp(chosenReward.index, rewardCount - 1);

        final int finalOffset = floorMod(targetIndex - CENTER_OFFSET, rewardCount);
        final int totalCycles = Math.max(0, cycles);
        final int startOffset = floorMod(finalOffset - (totalCycles % rewardCount), rewardCount);

        final int[] stepCount = { 0 };
        final int[] offset = { startOffset };
        final Runnable[] step = new Runnable[1];

        step[0] = () -> {
            if (stepCount[0] == totalCycles) {
                cr.render(finalOffset);
                player.playEffect(crate.getLocation(), Effect.CLICK1, 0);

                player.getServer().getScheduler().scheduleSyncDelayedTask(Crates.getInstance(), () -> {
                    handle.netServerHandler.sendPacket(new Packet101CloseWindow(handle.activeContainer.windowId));
                    handle.activeContainer = handle.defaultContainer;

                    Reward reward = rewards.get(targetIndex);
                    CrateHandler.crateEnd(crate, player, reward);

                    Location base = crate.getLocation().add(0.5, 0.5, 0.5);
                    for (int i = 0; i < 20; i++) base.getWorld().playEffect(base, Effect.SMOKE, 4);
                }, 30L);
                return;
            }

            cr.render(offset[0]);
            cr.playTickSound();

            offset[0] = (offset[0] + 1) % rewardCount;
            stepCount[0]++;

            long delay = intervalForStep(stepCount[0], totalCycles);
            player.getServer().getScheduler().scheduleSyncDelayedTask(Crates.getInstance(), step[0], delay);
        };

        player.getServer().getScheduler().scheduleSyncDelayedTask(Crates.getInstance(), step[0], 0L);
    }

    // --- Helpers
    private static long intervalForStep(int step, int total) {
        if (total <= 0) return 1L;
        int t33 = (int) Math.ceil(total * 0.33);
        int t66 = (int) Math.ceil(total * 0.66);
        int t80 = (int) Math.ceil(total * 0.80);
        int t95 = (int) Math.ceil(total * 0.95);

        if (step < t33) return 1L;
        if (step < t66) return 2L;
        if (step < t80) return 3L;
        if (step < t95) return 4L;
        return 8L;
    }

    private static int floorMod(int x, int m) {
        int r = x % m;
        return (r < 0) ? (r + m) : r;
    }

    private static int clamp(int v, int hi) {
        return (v < 0) ? 0 : Math.min(v, hi);
    }
}
