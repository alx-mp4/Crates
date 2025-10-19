package io.github.aleksandarharalanov.crates.listener.block;

import io.github.aleksandarharalanov.crates.crate.CrateManager;
import org.bukkit.block.Block;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockListener;

public class BlockBreakListener extends BlockListener {

    @Override
    public void onBlockBreak(BlockBreakEvent event) {
        Block block = event.getBlock();
        if (CrateManager.isLocked(block) || CrateManager.isOpened(block)) {
            event.setCancelled(true);
        }
    }
}
