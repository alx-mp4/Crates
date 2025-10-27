package io.github.aleksandarharalanov.crates.listener.block;

import io.github.aleksandarharalanov.crates.Crates;
import io.github.aleksandarharalanov.crates.crate.CrateConfig;
import io.github.aleksandarharalanov.crates.crate.CrateManager;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockListener;
import org.bukkit.inventory.ItemStack;

public class BlockBreakListener extends BlockListener {

    @Override
    public void onBlockBreak(BlockBreakEvent event) {
        Block block = event.getBlock();
        if (CrateManager.isBusy(block)) {
            event.setCancelled(true);
            return;
        }

        if (CrateConfig.isBlockCrate(block)) {
            Player player = event.getPlayer();
            if (Crates.checkWorldGuard(player, block)) return;

            event.setCancelled(true);
            block.setTypeId(0);
            Location base = block.getLocation().add(0.5, 0.5, 0.5);
            block.getWorld().dropItem(base, new ItemStack(95, 1));
        }
    }
}
