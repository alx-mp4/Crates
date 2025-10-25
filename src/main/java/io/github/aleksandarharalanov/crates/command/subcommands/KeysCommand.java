package io.github.aleksandarharalanov.crates.command.subcommands;

import io.github.aleksandarharalanov.crates.Crates;
import io.github.aleksandarharalanov.crates.util.auth.AccessUtil;
import io.github.aleksandarharalanov.crates.util.log.LogUtil;
import io.github.aleksandarharalanov.crates.util.misc.ColorUtil;
import org.bukkit.Bukkit;
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

            Player player = (Player) sender;
            Crates.getIo().submit(() -> {
                int value;
                try {
                    value = Crates.getSqliteDb().getPlayerKeys(player);
                } catch (SQLException e) {
                    sender.sendMessage(ColorUtil.translateColorCodes("&4» Database error. Contact an operator."));
                    LogUtil.logConsoleSevere(String.format("[Crates] Database error while checking %s keys: %s", player.getName(), e));
                    return;
                }
                int getKeys = value;
                player.sendMessage(ColorUtil.translateColorCodes(String.format("&7» You have &5%d &7keys.", getKeys)));
            });
            return true;
        }

        if (!AccessUtil.senderHasPermission(sender, "crates.keys.view.other",
                "&c» You don't have permission to view other players' keys.")) {
            return true;
        }

        Player player = Bukkit.getServer().getPlayer(args[1]);
        Crates.getIo().submit(() -> {
            int value;
            try {
                value = Crates.getSqliteDb().getPlayerKeys(player);
            } catch (SQLException e) {
                sender.sendMessage(ColorUtil.translateColorCodes("&4» Database error. Contact an operator."));
                LogUtil.logConsoleSevere(String.format("[Crates] Database error while checking %s keys: %s", player.getName(), e));
                return;
            }
            int getKeys = value;
            sender.sendMessage(ColorUtil.translateColorCodes(String.format("&7» &5%s &7has &5%d &7keys.", player.getName(), getKeys)));
        });
        return true;
    }
}
