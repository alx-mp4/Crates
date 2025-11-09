package io.github.alx_mp4.crates.command.subcommands;

import io.github.alx_mp4.crates.Crates;
import io.github.alx_mp4.crates.util.auth.AccessUtil;
import io.github.alx_mp4.crates.util.log.LogUtil;
import io.github.alx_mp4.crates.util.misc.ColorUtil;
import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.command.*;
import org.bukkit.entity.Player;

import java.sql.SQLException;

public final class SetKeysCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!AccessUtil.senderHasPermission(sender, "crates.keys.set",
                "&c» You don't have permission to set player keys.")) {
            return true;
        }

        if (args.length < 3) {
            sender.sendMessage(ColorUtil.translateColorCodes("&7» Usage: /crates setkeys <player> <value>"));
            return true;
        }

        int value;
        try {
            value = Integer.parseInt(args[2]);
            if (value < 0) value = 0;
        } catch (NumberFormatException ignored) {
            sender.sendMessage(ColorUtil.translateColorCodes("&7» Value must be 0 or higher."));
            return true;
        }
        int setKeys = value;

        Player player = Bukkit.getServer().getPlayer(args[1]);
        Crates.getIo().submit(() -> {
            try {
                int getKeys = Crates.getSqliteDb().getPlayerKeys(player);
                Crates.getSqliteDb().setPlayerKeys(player, setKeys);
                sender.sendMessage(ColorUtil.translateColorCodes(String.format(
                                    "&7» Set &5%s&7's keys to &5%d&7.", player.getName(), setKeys)));
                player.sendMessage(ColorUtil.translateColorCodes(String.format(
                        "&7» Your keys were set from &5%d &7to &5%d&7.", getKeys, setKeys
                )));
                player.playEffect(player.getLocation(), Effect.CLICK1, 0);
            } catch (SQLException e) {
                sender.sendMessage(ColorUtil.translateColorCodes("&4» Database error. Contact an operator."));
                LogUtil.logConsoleSevere(String.format("[Crates] Database error while setting keys for %s: %s", player.getName(), e));
            }
        });
        return true;
    }
}
