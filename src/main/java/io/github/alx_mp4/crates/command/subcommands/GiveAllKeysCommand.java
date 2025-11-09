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

public final class GiveAllKeysCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!AccessUtil.senderHasPermission(sender, "crates.keys.giveall",
                "&c» You don't have permission to give player keys.")) {
            return true;
        }

        Player[] onlinePlayers = Bukkit.getServer().getOnlinePlayers();
        if (onlinePlayers.length == 0) {
            LogUtil.logConsoleInfo("[Crates] There are no players currently online.");
            return true;
        }

        int value = 1;
        if (args.length >= 2) {
            try {
                value = Integer.parseInt(args[1]);
                if (value < 1) {
                    sender.sendMessage(ColorUtil.translateColorCodes("&7» Value must be 1 or higher."));
                    return true;
                }
            } catch (NumberFormatException ignored) {
                sender.sendMessage(ColorUtil.translateColorCodes("&7» Value must be a valid number."));
                return true;
            }
        }

        int given = value;
        Crates.getIo().submit(() -> {
            int count = 0;
            for (Player player : onlinePlayers) {
                try {
                    Crates.getSqliteDb().addPlayerKeys(player, given);
                    player.sendMessage(ColorUtil.translateColorCodes(String.format("&7» You received &5+%d &7keys&7!", given)));
                    player.playEffect(player.getLocation(), Effect.CLICK1, 0);
                    count++;
                } catch (SQLException e) {
                    sender.sendMessage(ColorUtil.translateColorCodes("&4» Database error. Contact an operator."));
                    LogUtil.logConsoleSevere(String.format("[Crates] Database error while giving everyone keys: %s", e));
                    return;
                }
            }
            sender.sendMessage(ColorUtil.translateColorCodes(String.format("&7» Gave &5+%d &7keys to &5%d &7online player(s).", given, count)));
        });
        return true;
    }
}
