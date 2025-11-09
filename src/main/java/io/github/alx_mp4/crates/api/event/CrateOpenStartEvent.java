package io.github.alx_mp4.crates.api.event;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;

public class CrateOpenStartEvent extends Event implements Cancellable {

    private final Block crate;
    private final Player player;
    private boolean cancelled;

    /**
     * Event is called right before a key is consumed to open a crate.
     */
    public CrateOpenStartEvent(Block crate, Player player) {
        super("CRATE_OPEN_START");
        this.crate = crate;
        this.player = player;
    }

    public Block getCrate() { return this.crate; }

    public Player getPlayer() { return this.player; }

    @Override
    public boolean isCancelled() { return this.cancelled; }

    @Override
    public void setCancelled(boolean cancel) { this.cancelled = cancel; }
}
