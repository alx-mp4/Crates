package io.github.aleksandarharalanov.crates.command.subcommands;

import io.github.aleksandarharalanov.crates.Crates;
import io.github.aleksandarharalanov.crates.util.auth.AccessUtil;
import io.github.aleksandarharalanov.crates.util.log.LogUtil;
import io.github.aleksandarharalanov.crates.util.misc.ColorUtil;
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

        final Player[] online = Bukkit.getServer().getOnlinePlayers();
        if (online.length == 0) {
            LogUtil.logConsoleInfo("[Crates] No players online.");
            return true;
        }

        int amount = 1;
        if (args.length >= 2) {
            try {
                amount = Integer.parseInt(args[1]);
                if (amount < 1) {
                    sender.sendMessage(ColorUtil.translateColorCodes("&7» Value must be 1 or higher."));
                    return true;
                }
            } catch (NumberFormatException ignored) {
                sender.sendMessage(ColorUtil.translateColorCodes("&7» Value must be a valid number."));
                return true;
            }
        }

        final int give = amount;
        Crates.getIo().submit(() -> {
            int count = 0;
            for (Player p : online) {
                try {
                    Crates.getSqliteDb().addPlayerKeys(p, give);
                    p.sendMessage(ColorUtil.translateColorCodes(String.format(
                            "&7» You received &5+%d &7keys&7!", give)));
                    p.playEffect(p.getLocation(), Effect.CLICK1, 0);
                    count++;
                } catch (SQLException e) {
                    sender.sendMessage(ColorUtil.translateColorCodes(
                            "&4» Database error while giving everyone keys. Contact an operator."));
                    LogUtil.logConsoleSevere(String.format("[Crates] Database error while giving everyone keys: %s", e));
                    return;
                }
            }
            final int givenTo = count;
            sender.sendMessage(ColorUtil.translateColorCodes(String.format(
                            "&7» Gave &5+%d &7keys to &5%s &7online player(s).", give, givenTo)));
        });
        return true;
    }
}
