package io.github.aleksandarharalanov.crates.listener.block;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.block.BlockListener;
import org.bukkit.event.block.BlockPlaceEvent;

public class BlockPlaceListener extends BlockListener {

    @Override
    public void onBlockPlace(BlockPlaceEvent event) {
        Block block = event.getBlock();
        if (block.getType() == Material.LOCKED_CHEST || block.getTypeId() == 95) {
            block.setTypeIdAndData(33, (byte) 7, true);
        }
    }
}
