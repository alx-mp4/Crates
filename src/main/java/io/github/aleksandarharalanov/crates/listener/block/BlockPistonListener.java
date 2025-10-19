package io.github.aleksandarharalanov.crates.listener.block;

import io.github.aleksandarharalanov.crates.crate.CrateManager;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.block.BlockListener;
import org.bukkit.event.block.BlockPistonExtendEvent;
import org.bukkit.event.block.BlockPistonRetractEvent;

public class BlockPistonListener extends BlockListener {

    @Override
    public void onBlockPistonExtend(BlockPistonExtendEvent event) {
        for (Block block : event.getBlocks()) {
            if (block.getType() == Material.LOCKED_CHEST) {
                if (CrateManager.isLocked(block) || CrateManager.isOpened(block)) {
                    event.setCancelled(true);
                }
            }
        }
    }

    @Override
    public void onBlockPistonRetract(BlockPistonRetractEvent event) {
        Block block = event.getRetractLocation().getBlock();
        if (block.getType() == Material.LOCKED_CHEST) {
            if (CrateManager.isLocked(block) || CrateManager.isOpened(block)) {
                event.setCancelled(true);
            }
        }
    }
}
