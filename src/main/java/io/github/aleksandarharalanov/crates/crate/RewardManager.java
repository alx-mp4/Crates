package io.github.aleksandarharalanov.crates.crate;

import io.github.aleksandarharalanov.crates.Crates;
import io.github.aleksandarharalanov.crates.crate.CrateConfig.RewardEntry;
import io.github.aleksandarharalanov.crates.embed.DiscordConfig;
import io.github.aleksandarharalanov.crates.embed.DiscordEmbed;
import io.github.aleksandarharalanov.crates.embed.RewardEmbed;
import io.github.aleksandarharalanov.crates.util.log.DiscordUtil;
import io.github.aleksandarharalanov.crates.util.log.LogUtil;
import io.github.aleksandarharalanov.crates.util.misc.ColorUtil;
import io.github.aleksandarharalanov.crates.util.misc.EffectUtil;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.io.IOException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;

import static org.bukkit.Bukkit.getServer;

public final class RewardManager {

    private static final SecureRandom random = new SecureRandom();
    private static final List<Reward> rewards = new ArrayList<>();

    private RewardManager() { }

    public static void loadRewards() {
        random.nextInt();
        rewards.clear();
        for (RewardEntry e : CrateConfig.getRewards()) {
            rewards.add(new Reward(e.material, e.data, e.amount, e.weight, e.tier));
        }
        LogUtil.logConsoleInfo(String.format("[Crates] Loaded %d rewards from config.yml", rewards.size()));
    }

    public static void giveReward(Player player, Block block) {
        if (rewards.isEmpty()) loadRewards();
        if (rewards.isEmpty()) {
            player.sendMessage(ColorUtil.translateColorCodes("&7» No rewards are configured."));
            return;
        }

        Reward reward = getRandomReward();
        ItemStack drop = new ItemStack(reward.material, reward.amount, (short) 0, reward.data);
        block.setType(Material.AIR);
        EffectUtil.playBlockBreakEffect(player, block.getLocation(), 35, reward.tier.getParticleColor());
        block.getWorld().dropItem(block.getLocation(), drop);

        player.sendMessage(ColorUtil.translateColorCodes(String.format(
                "&7» The crate contained &f%d× %s &8[%s&8]&7!", reward.amount, reward.material.name(), reward.tier.getDisplayName())));

        if (!DiscordConfig.getEnabled()) return;

        final String webhookUrl = DiscordConfig.getWebhookUrl();
        DiscordUtil webhook = new DiscordUtil(webhookUrl);
        DiscordEmbed embed = new RewardEmbed(Crates.getInstance(), player, reward);
        webhook.addEmbed(embed.getEmbed());

        getServer().getScheduler().scheduleAsyncDelayedTask(
                Crates.getInstance(),
                () -> {
                    try {
                        webhook.execute();
                    } catch (IOException e) {
                        LogUtil.logConsoleWarning(e.getMessage());
                    }
                },
                20L
        );
    }

    private static Reward getRandomReward() {
        int total = 0;
        for (Reward r : rewards) total += r.weight;
        if (total <= 0) return rewards.get(0);

        int roll = random.nextInt(total);
        int cumulative = 0;
        for (Reward r : rewards) {
            cumulative += r.weight;
            if (roll < cumulative) return r;
        }
        return rewards.get(0);
    }
}
