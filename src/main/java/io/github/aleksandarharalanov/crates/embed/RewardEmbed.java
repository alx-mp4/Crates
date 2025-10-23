package io.github.aleksandarharalanov.crates.embed;

import io.github.aleksandarharalanov.crates.crate.reward.Reward;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.awt.Color;

public final class RewardEmbed extends DiscordEmbed {

    private final Reward reward;

    public RewardEmbed(JavaPlugin plugin, Player player, Reward reward) {
        super(plugin, player);
        this.reward = reward;
        setupBaseEmbed();
    }

    @Override
    protected void setupEmbedDetails() {
        final int id = reward.material.getId();
        final byte data = reward.data;
        final String icon = String.format("https://raw.githubusercontent.com/AleksandarHaralanov/Crates/refs/heads/master/src/main/resources/items/%d_%d.webp", id, data);

        embed.setTitle("Unboxed Crate")
                .setThumbnail(icon)
                .addField("Reward:", String.format("%s× %s", reward.amount, reward.material), true)
                .addField("Rarity:", reward.tier.toString(), true)
                .setColor(Color.decode(reward.tier.getRgbColor()));
    }
}
