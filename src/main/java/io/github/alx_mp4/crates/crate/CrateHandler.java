package io.github.alx_mp4.crates.crate;

import io.github.alx_mp4.crates.Crates;
import io.github.alx_mp4.crates.api.event.CrateOpenEndEvent;
import io.github.alx_mp4.crates.api.event.CrateOpenStartEvent;
import io.github.alx_mp4.crates.crate.reward.Reward;
import io.github.alx_mp4.crates.crate.reward.RewardHandler;
import io.github.alx_mp4.crates.crate.reward.RewardTier;
import io.github.alx_mp4.crates.util.auth.AccessUtil;
import io.github.alx_mp4.crates.util.log.DiscordUtil;
import io.github.alx_mp4.crates.util.log.LogUtil;
import io.github.alx_mp4.crates.util.misc.ColorUtil;
import io.github.alx_mp4.crates.util.misc.EffectUtil;
import io.github.alx_mp4.crates.webhook.DiscordConfig;
import io.github.alx_mp4.crates.webhook.DiscordEmbed;
import io.github.alx_mp4.crates.webhook.RewardEmbed;
import net.minecraft.server.EntityPlayer;
import net.minecraft.server.Packet18ArmAnimation;
import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.io.IOException;
import java.sql.SQLException;

public final class CrateHandler {

    private CrateHandler() { }

    public static void crateStart(Player player, Block crate) {
        // --- Early guards
        if (CrateManager.isBusy(crate)) {
            player.sendMessage(ColorUtil.translateColorCodes("&7» This crate is already being opened!"));
            return;
        }

        if (CrateManager.isBusy(player)) {
            player.sendMessage(ColorUtil.translateColorCodes("&7» You are already opening another crate!"));
            return;
        }

        if (Crates.checkWorldGuard(player, crate)) {
            player.sendMessage(ColorUtil.translateColorCodes("&c» You can't open crates protected by a region."));
            return;
        }

        if (!AccessUtil.senderHasPermission(player, "crates.open")) {
            player.sendMessage(ColorUtil.translateColorCodes("&c» You don't have permission to open crates."));
            return;
        }

        // --- Dev API start
        CrateOpenStartEvent event = new CrateOpenStartEvent(crate, player);
        Bukkit.getServer().getPluginManager().callEvent(event);
        // --- Dev API end

        // --- Ensure player has keys and set values
        Crates.getIo().submit(() -> {
            final int keys;

            try {
                keys = Crates.getSqliteDb().getPlayerKeys(player);
                if (keys > 0) Crates.getSqliteDb().setPlayerKeys(player, keys - 1);
                else {
                    player.sendMessage(ColorUtil.translateColorCodes("&7» You don't have any keys."));
                    return;
                }
            } catch (SQLException e) {
                player.sendMessage(ColorUtil.translateColorCodes("&4» Database error. Contact an operator."));
                LogUtil.logConsoleSevere(String.format("[Crates] Database error while setting keys for %s: %s", player.getName(), e));
                return;
            }

            // --- Switch to main thread
            Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Crates.getInstance(), () -> {
                        // Mark busy player and specific crate as busy
                        CrateManager.add(player);
                        CrateManager.add(crate);

                        // Feedback to player
                        player.sendMessage(ColorUtil.translateColorCodes("&7» Opening crate..."));

                        // Visuals + sounds
                        EntityPlayer playerHandle = ((CraftPlayer) player).getHandle();
                        playerHandle.netServerHandler.sendPacket(new Packet18ArmAnimation(playerHandle, 1));

                        crate.getWorld().playEffect(crate.getLocation(), Effect.DOOR_TOGGLE, 0);
                        crate.getWorld().playEffect(crate.getLocation(), Effect.STEP_SOUND, 33);

                        // Schedule reward process
                        Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(
                                Crates.getInstance(),
                                () -> RewardHandler.giveReward(player, crate),
                                CrateConfig.getCrateOpenDelay()
                        );
                    },
                    0L
            );
        });
    }

    public static void crateEnd(Block crate, Player player, Reward reward) {
        // --- Visuals, sounds, and reward
        crate.setTypeId(0);
        EffectUtil.playBlockBreakEffect(player, crate.getLocation(), 35, reward.tier.getParticleColor());

        Location base = crate.getLocation().add(0.5, 0.5, 0.5);
        crate.getWorld().dropItem(base, reward.itemStack);

        // --- Player & server messages
        String rewardName = reward.material.name();
        String tierName = reward.tier.getDisplayName();
        int amount = reward.amount;

        player.sendMessage(ColorUtil.translateColorCodes(
                String.format("&7» The crate contained &f%d× %s &8[%s&8]&7!", amount, rewardName, tierName)));

        if (reward.tier == RewardTier.EPIC || reward.tier == RewardTier.LEGENDARY) {
            Bukkit.getServer().broadcastMessage(ColorUtil.translateColorCodes(
                    String.format("&7» &5%s&7 got &f%d× %s &8[%s&8]&7 from a crate!", player.getName(), amount, rewardName, tierName)));
        }

        // --- Increment crates opened counter for player
        Crates.getIo().submit(() -> {
            try {
                final int cratesOpened = Crates.getSqliteDb().getCratesOpened(player);
                Crates.getSqliteDb().setCratesOpened(player, cratesOpened + 1);
            } catch (SQLException e) {
                player.sendMessage(ColorUtil.translateColorCodes("&4» Database error. Contact an operator."));
                LogUtil.logConsoleWarning(String.format("[Crates] Database error incrementing crates opened for %s: %s", player.getName(), e));
            }
        });

        // --- Cleanup runtime state
        CrateManager.remove(player);
        CrateManager.remove(crate);

        // --- Discord webhook
        if (DiscordConfig.getEnabled()) {
            final DiscordUtil webhook = new DiscordUtil(DiscordConfig.getWebhookUrl());
            final DiscordEmbed embed = new RewardEmbed(Crates.getInstance(), player, reward);
            webhook.addEmbed(embed.getEmbed());

            Bukkit.getServer().getScheduler().scheduleAsyncDelayedTask(
                    Crates.getInstance(),
                    () -> {
                        try {
                            webhook.execute();
                        } catch (IOException e) {
                            LogUtil.logConsoleWarning(String.format("[Crates] An exception occurred when executing the Discord webhook: %s", e));
                        }
                    },
                    0L
            );
        }

        // --- Dev API start
        CrateOpenEndEvent event = new CrateOpenEndEvent(crate, player, reward);
        Bukkit.getServer().getPluginManager().callEvent(event);
        // --- Dev API end
    }
}
