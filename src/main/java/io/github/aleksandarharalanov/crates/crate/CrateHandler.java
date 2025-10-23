package io.github.aleksandarharalanov.crates.crate;

import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import io.github.aleksandarharalanov.crates.Crates;
import io.github.aleksandarharalanov.crates.api.event.CrateOpenEvent;
import io.github.aleksandarharalanov.crates.crate.reward.RewardManager;
import io.github.aleksandarharalanov.crates.util.auth.AccessUtil;
import io.github.aleksandarharalanov.crates.util.log.LogUtil;
import io.github.aleksandarharalanov.crates.util.misc.ColorUtil;
import net.minecraft.server.EntityPlayer;
import net.minecraft.server.Packet18ArmAnimation;
import org.bukkit.Effect;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.sql.SQLException;

import static org.bukkit.Bukkit.getServer;

public final class CrateHandler {

    private CrateHandler() { }

    public static void openCrate(Player player, Block crate) {
        if (CrateManager.isLocked(crate) || CrateManager.isOpened(crate)) {
            player.sendMessage(ColorUtil.translateColorCodes("&7» This crate is already being opened!"));
            return;
        }

        CrateManager.lock(crate);

        WorldGuardPlugin wg = (WorldGuardPlugin) getServer().getPluginManager().getPlugin("WorldGuard");
        if (wg != null && wg.isEnabled() && !wg.canBuild(player, crate.getLocation())) {
            player.sendMessage(ColorUtil.translateColorCodes("&c» You can't open crates protected by a region."));
            CrateManager.unlock(crate);
            return;
        }

        if (!AccessUtil.senderHasPermission(player, "crates.open")) {
            player.sendMessage(ColorUtil.translateColorCodes("&c» You don't have permission to open crates."));
            CrateManager.unlock(crate);
            return;
        }

        // --- Dev API start
        CrateOpenEvent event = new CrateOpenEvent(crate, player);
        getServer().getPluginManager().callEvent(event);
        if (event.isCancelled()) {
            CrateManager.unlock(crate);
            return;
        }
        // --- Dev API end

        Crates.getIo().submit(() -> {
            try {
                int keys = Crates.getSqliteDb().getPlayerKeys(player);
                if (keys <= 0) {
                    player.sendMessage(ColorUtil.translateColorCodes("&7» You don't have any keys."));
                    CrateManager.unlock(crate);
                    return;
                }

                try {
                    Crates.getSqliteDb().setPlayerKeys(player, keys - 1);
                } catch (SQLException e) {
                    player.sendMessage(ColorUtil.translateColorCodes(
                            "&4» Database error while setting keys. Contact an operator."));
                    LogUtil.logConsoleSevere(String.format("[Crates] Database error while setting keys: %s", e));
                    return;
                }

                CrateManager.markOpened(crate);

                player.sendMessage(ColorUtil.translateColorCodes("&5» Crate is being opened."));

                EntityPlayer nmsPlayer = ((CraftPlayer) player).getHandle();
                nmsPlayer.netServerHandler.sendPacket(new Packet18ArmAnimation(nmsPlayer, 1));

                player.playEffect(crate.getLocation(), Effect.DOOR_TOGGLE, 0);
                player.playEffect(crate.getLocation(), Effect.STEP_SOUND, 95);

                getServer().getScheduler().scheduleSyncDelayedTask(
                        Crates.getInstance(),
                        () -> {
                            RewardManager.giveReward(player, crate);
                            CrateManager.clear(crate);
                        },
                        CrateConfig.getCrateOpenDelay()
                );
            } catch (SQLException e) {
                player.sendMessage(ColorUtil.translateColorCodes("&4» Database error while opening crate. Contact an operator."));
                LogUtil.logConsoleWarning(String.format("[Crates] Database error while opening crate: %s", e));
                CrateManager.unlock(crate);
            }
        });
    }
}
