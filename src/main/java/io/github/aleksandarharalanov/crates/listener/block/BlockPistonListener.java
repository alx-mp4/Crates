package io.github.aleksandarharalanov.crates.listener.block;

import io.github.aleksandarharalanov.crates.crate.CrateConfig;
import org.bukkit.block.Block;
import org.bukkit.event.block.BlockListener;
import org.bukkit.event.block.BlockPistonExtendEvent;
import org.bukkit.event.block.BlockPistonRetractEvent;

import java.util.List;

public class BlockPistonListener extends BlockListener {

    @Override
    public void onBlockPistonExtend(BlockPistonExtendEvent event) {
        List<Block> blocks = event.getBlocks();
        for (Block block : blocks) {
            if (CrateConfig.isBlockCrate(block)) event.setCancelled(true);
        }
    }

    @Override
    public void onBlockPistonRetract(BlockPistonRetractEvent event) {
        Block block = event.getRetractLocation().getBlock();
        if (CrateConfig.isBlockCrate(block)) event.setCancelled(true);
    }
}