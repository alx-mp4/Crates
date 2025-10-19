package io.github.aleksandarharalanov.crates.embed;

import io.github.aleksandarharalanov.crates.util.log.DiscordUtil;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public abstract class DiscordEmbed {

    protected final String pluginVersion;
    protected final Player player;
    protected final long timestamp;
    protected final DiscordUtil.EmbedObject embed;

    public DiscordEmbed(JavaPlugin plugin, Player player) {
        this.pluginVersion = plugin.getDescription().getVersion();
        this.player = player;
        this.timestamp = System.currentTimeMillis() / 1000;
        this.embed = new DiscordUtil.EmbedObject();
    }

    protected void setupBaseEmbed() {
        embed.setAuthor(player.getName(), null, DiscordConfig.getAvatarApi().replace("%player%", player.getName()));

        setupEmbedDetails();

        embed.addField("Timestamp:", String.format("<t:%d:f>", timestamp), true);
        embed.setFooter(
                String.format("Crates v%s ・ Logger", pluginVersion),
                "https://github.githubassets.com/images/modules/logos_page/GitHub-Mark.png"
        );
    }

    protected abstract void setupEmbedDetails();

    public DiscordUtil.EmbedObject getEmbed() {
        return embed;
    }
}