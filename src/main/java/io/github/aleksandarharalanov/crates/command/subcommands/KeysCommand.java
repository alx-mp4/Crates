package io.github.aleksandarharalanov.crates.command.subcommands;

import io.github.aleksandarharalanov.crates.Crates;
import io.github.aleksandarharalanov.crates.util.auth.AccessUtil;
import io.github.aleksandarharalanov.crates.util.log.LogUtil;
import io.github.aleksandarharalanov.crates.util.misc.ColorUtil;
import org.bukkit.command.*;
import org.bukkit.entity.Player;

import java.sql.SQLException;

public final class KeysCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (args.length == 1) {
            if (!(sender instanceof Player)) {
                sender.sendMessage(ColorUtil.translateColorCodes("&7» Usage: /crates keys <player>"));
                return true;
            }
            if (!AccessUtil.senderHasPermission(sender, "crates.keys.view",
                    "&c» You don't have permission to view your keys.")) {
                return true;
            }

            final Player p = (Player) sender;
            Crates.getIo().submit(() -> {
                int k;
                try {
                    k = Crates.getSqliteDb().getPlayerKeys(p);
                } catch (SQLException e) {
                    sender.sendMessage(ColorUtil.translateColorCodes(
                            "&4» Database error while checking personal keys. Contact an operator."));
                    LogUtil.logConsoleSevere(String.format("[Crates] Database error while checking personal keys: %s", e));
                    return;
                }
                final int keys = k;
                p.sendMessage(ColorUtil.translateColorCodes(String.format("&7» You have &5%d &7keys.", keys)));
            });
            return true;
        }

        if (!AccessUtil.senderHasPermission(sender, "crates.keys.view.other",
                "&c» You don't have permission to view other players' keys.")) {
            return true;
        }

        final String targetName = args[1];
        Crates.getIo().submit(() -> {
            int k;
            try {
                k = Crates.getSqliteDb().getPlayerKeys(targetName);
            } catch (SQLException e) {
                sender.sendMessage(ColorUtil.translateColorCodes(
                        "&4» Database error while checking someone's keys. Contact an operator."));
                LogUtil.logConsoleSevere(String.format("[Crates] Database error while checking someone's keys: %s", e));
                return;
            }
            final int keys = k;
            sender.sendMessage(ColorUtil.translateColorCodes(String.format(
                    "&7» &5%s &7has &5%d &7keys.", targetName, keys)));
        });
        return true;
    }
}
