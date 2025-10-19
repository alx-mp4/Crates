package io.github.aleksandarharalanov.crates.listener.player;

import io.github.aleksandarharalanov.crates.crate.CrateHandler;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerListener;

public class PlayerInteractListener extends PlayerListener {

    @Override
    public void onPlayerInteract(PlayerInteractEvent event) {
        Block block = event.getClickedBlock();
        if (block == null) {
            return;
        }

        Player player = event.getPlayer();
        if (block.getTypeId() == 95 && event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            CrateHandler.openCrate(player, block);
        }
    }
}
