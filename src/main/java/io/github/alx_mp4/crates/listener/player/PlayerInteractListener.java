package io.github.alx_mp4.crates.listener.player;

import io.github.alx_mp4.crates.crate.CrateConfig;
import io.github.alx_mp4.crates.crate.CrateHandler;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerListener;

public class PlayerInteractListener extends PlayerListener {

    @Override
    public void onPlayerInteract(PlayerInteractEvent event) {
        Block block = event.getClickedBlock();
        if (block == null) return;

        Action action = event.getAction();
        Player player = event.getPlayer();
        if (CrateConfig.isBlockCrate(block) && action == Action.RIGHT_CLICK_BLOCK) {
            CrateHandler.crateStart(player, block);
        }
    }
}
