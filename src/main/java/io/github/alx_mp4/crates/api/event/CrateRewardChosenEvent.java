package io.github.alx_mp4.crates.api.event;

import io.github.alx_mp4.crates.crate.reward.Reward;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;

public class CrateRewardChosenEvent extends Event {

    private final Block crate;
    private final Player player;
    private Reward reward;

    /**
     * Event is called right after an award is chosen and the crate hasn't begun opening yet.
     */
    public CrateRewardChosenEvent(Block crate, Player player, Reward reward) {
        super("CRATE_REWARD_CHOSEN");
        this.crate = crate;
        this.player = player;
        this.reward = reward;
    }

    public Block getCrate() { return this.crate; }

    public Player getPlayer() { return this.player; }

    public Reward getReward() { return this.reward; }

    public void setReward(Reward reward) { this.reward = reward; }
}
