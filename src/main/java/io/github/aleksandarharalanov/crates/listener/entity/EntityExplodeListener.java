package io.github.aleksandarharalanov.crates.listener.entity;

import io.github.aleksandarharalanov.crates.crate.CrateManager;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.EntityListener;

public class EntityExplodeListener extends EntityListener {

    @Override
    public void onEntityExplode(EntityExplodeEvent event) {
        for (Block block : event.blockList()) {
            if (block.getType() == Material.LOCKED_CHEST) {
                if (CrateManager.isLocked(block) || CrateManager.isOpened(block)) {
                    event.setCancelled(true);
                }
            }
        }
    }
}
