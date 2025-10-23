package io.github.aleksandarharalanov.crates.api.event;

import io.github.aleksandarharalanov.crates.crate.reward.Reward;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;

public class CrateRewardEvent extends Event {

    private final Block crate;
    private final Player player;
    private Reward reward;

    /**
     * Event is called right before an award is dropped from the crate.
     */
    public CrateRewardEvent(Block crate, Player player, Reward reward) {
        super("CRATE_REWARD");
        this.crate = crate;
        this.player = player;
        this.reward = reward;
    }

    public Block getCrate() {
        return this.crate;
    }

    public Player getPlayer() {
        return this.player;
    }

    public Reward getReward() {
        return this.reward;
    }

    public void setReward(Reward reward) {
        this.reward = reward;
    }
}
