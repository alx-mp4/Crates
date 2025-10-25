package io.github.aleksandarharalanov.crates.api.event;

import io.github.aleksandarharalanov.crates.crate.reward.Reward;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;

public class CrateOpenEndEvent extends Event {

    private final Block crate;
    private final Player player;
    private final Reward reward;

    /**
     * Event is called right after a crate has finished opening.
     */
    public CrateOpenEndEvent(Block crate, Player player, Reward reward) {
        super("CRATE_OPEN_END");
        this.crate = crate;
        this.player = player;
        this.reward = reward;
    }

    public Block getCrate() { return this.crate; }

    public Player getPlayer() { return this.player; }

    public Reward getReward() { return this.reward; }
}
