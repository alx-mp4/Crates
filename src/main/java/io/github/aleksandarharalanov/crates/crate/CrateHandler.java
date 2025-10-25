package io.github.aleksandarharalanov.crates.crate;

import io.github.aleksandarharalanov.crates.Crates;
import io.github.aleksandarharalanov.crates.api.event.CrateOpenEndEvent;
import io.github.aleksandarharalanov.crates.api.event.CrateOpenStartEvent;
import io.github.aleksandarharalanov.crates.crate.reward.Reward;
import io.github.aleksandarharalanov.crates.crate.reward.RewardHandler;
import io.github.aleksandarharalanov.crates.crate.reward.RewardTier;
import io.github.aleksandarharalanov.crates.util.auth.AccessUtil;
import io.github.aleksandarharalanov.crates.util.log.LogUtil;
import io.github.aleksandarharalanov.crates.util.misc.ColorUtil;
import io.github.aleksandarharalanov.crates.util.misc.EffectUtil;
import net.minecraft.server.EntityPlayer;
import net.minecraft.server.Packet18ArmAnimation;
import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.sql.SQLException;

import static org.bukkit.Bukkit.getServer;

public final class CrateHandler {

    private CrateHandler() { }

    public static void crateStart(Player player, Block crate) {
        if (CrateManager.isBusy(crate)) {
            player.sendMessage(ColorUtil.translateColorCodes("&7» This crate is already being opened!"));
            return;
        }

        if (Crates.checkWorldGuard(player, crate)) {
            player.sendMessage(ColorUtil.translateColorCodes("&c» You can't open crates protected by a region."));
            return;
        }

        if (!AccessUtil.senderHasPermission(player, "crates.open")) {
            player.sendMessage(ColorUtil.translateColorCodes("&c» You don't have permission to open crates."));
            CrateManager.remove(crate);
            return;
        }

        // --- Dev API start
        CrateOpenStartEvent event = new CrateOpenStartEvent(crate, player);
        getServer().getPluginManager().callEvent(event);
        // --- Dev API end

        Crates.getIo().submit(() -> {
            try {
                int keys = Crates.getSqliteDb().getPlayerKeys(player);
                if (keys <= 0) {
                    player.sendMessage(ColorUtil.translateColorCodes("&7» You don't have any keys."));
                    CrateManager.remove(crate);
                    return;
                }

                try {
                    Crates.getSqliteDb().setPlayerKeys(player, keys - 1);
                } catch (SQLException e) {
                    player.sendMessage(ColorUtil.translateColorCodes("&4» Database error. Contact an operator."));
                    LogUtil.logConsoleSevere(String.format("[Crates] Database error while setting keys for %s: %s", player.getName(), e));
                    return;
                }

                try {
                    if (keys - 1 == 0) Crates.getSqliteDb().deletePlayer(player);
                } catch (SQLException e) {
                    player.sendMessage(ColorUtil.translateColorCodes("&4» Database error. Contact an operator."));
                    LogUtil.logConsoleSevere(String.format("[Crates] Database error while deleting player %s: %s", player.getName(), e));
                    return;
                }

                CrateManager.add(crate);

                player.sendMessage(ColorUtil.translateColorCodes("&5» Opening crate..."));

                EntityPlayer playerHandle = ((CraftPlayer) player).getHandle();
                playerHandle.netServerHandler.sendPacket(new Packet18ArmAnimation(playerHandle, 1));

                player.playEffect(crate.getLocation(), Effect.DOOR_TOGGLE, 0);
                player.playEffect(crate.getLocation(), Effect.STEP_SOUND, 33);

                getServer().getScheduler().scheduleSyncDelayedTask(
                        Crates.getInstance(),
                        () -> RewardHandler.giveReward(player, crate), CrateConfig.getCrateOpenDelay());
            } catch (SQLException e) {
                player.sendMessage(ColorUtil.translateColorCodes("&4» Database error. Contact an operator."));
                LogUtil.logConsoleWarning(String.format("[Crates] Database error while opening crate for %s: %s", player.getName(), e));
                CrateManager.remove(crate);
            }
        });
    }

    public static void crateEnd(Block crate, Player player, Reward reward) {
        crate.setTypeId(0);
        EffectUtil.playBlockBreakEffect(player, crate.getLocation(), 35, reward.tier.getParticleColor());
        crate.getWorld().dropItem(crate.getLocation(), reward.itemStack);
        player.sendMessage(ColorUtil.translateColorCodes(String.format(
                "&7» The crate contained &f%d× %s &8[%s&8]&7!", reward.amount, reward.material.name(), reward.tier.getDisplayName())));
        if (reward.tier == RewardTier.EPIC || reward.tier == RewardTier.LEGENDARY) {
            Bukkit.getServer().broadcastMessage(ColorUtil.translateColorCodes(String.format(
                    "&7» &5%s&7 got &f%d× %s &8[%s&8]&7 from a crate!",
                    player.getName(), reward.amount, reward.material.name(), reward.tier.getDisplayName())));
        }
        CrateManager.remove(crate);

        // --- Dev API start
        CrateOpenEndEvent event = new CrateOpenEndEvent(crate, player, reward);
        getServer().getPluginManager().callEvent(event);
        // --- Dev API end
    }
}
