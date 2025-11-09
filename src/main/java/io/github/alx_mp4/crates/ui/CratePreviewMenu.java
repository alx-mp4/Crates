package io.github.alx_mp4.crates.ui;

import io.github.alx_mp4.crates.crate.CrateConfig;
import io.github.alx_mp4.crates.crate.reward.Reward;
import io.github.alx_mp4.crates.ui.container.ContainerPreview;
import net.minecraft.server.EntityPlayer;
import net.minecraft.server.ICrafting;
import net.minecraft.server.IInventory;
import net.minecraft.server.InventoryLargeChest;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.util.List;

public final class CratePreviewMenu {

    private CratePreviewMenu() {}

    private static final String TITLE = "Preview [PREV < PAGE > NEXT]";
    private static final int SIZE = 27;

    public static void openPreview(Player player) {
        IInventory backing = new InventoryLargeChest(
                TITLE,
                new SimpleInventory(TITLE, SIZE),
                new SimpleInventory(TITLE, SIZE));
        List<Reward> rewards = CrateConfig.getRewards();
        openPreview(player, backing, rewards);
    }

    public static void openPreview(Player player, IInventory backing, List<Reward> rewards) {
        EntityPlayer playerHandle = ((CraftPlayer) player).getHandle();

        playerHandle.a(backing);
        int winId = playerHandle.activeContainer.windowId;

        ContainerPreview cp = new ContainerPreview(playerHandle, playerHandle.inventory, backing, rewards);
        cp.windowId = winId;
        cp.a((ICrafting) playerHandle);
        playerHandle.activeContainer = cp;

        cp.render();
        playerHandle.updateInventory(cp);
    }
}
