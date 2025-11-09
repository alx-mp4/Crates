package io.github.alx_mp4.crates.listener.entity;

import io.github.alx_mp4.crates.crate.CrateConfig;
import org.bukkit.block.Block;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.EntityListener;

import java.util.List;

public class EntityExplodeListener extends EntityListener {

    @Override
    public void onEntityExplode(EntityExplodeEvent event) {
        List<Block> blocks = event.blockList();
        for (Block block : blocks) {
            if (CrateConfig.isBlockCrate(block)) event.setCancelled(true);
        }
    }
}
