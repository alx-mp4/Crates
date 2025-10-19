package io.github.aleksandarharalanov.crates.command.subcommands;

import io.github.aleksandarharalanov.crates.Crates;
import io.github.aleksandarharalanov.crates.util.auth.AccessUtil;
import io.github.aleksandarharalanov.crates.util.log.LogUtil;
import io.github.aleksandarharalanov.crates.util.misc.ColorUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public final class ReloadCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!AccessUtil.senderHasPermission(sender, "crates.config",
                "&c» You don't have permission to reload the config.")) {
            return true;
        }

        Crates.getConfig().load();

        if (sender instanceof Player) {
            sender.sendMessage(ColorUtil.translateColorCodes("&7» Configurations reloaded."));
        }
        LogUtil.logConsoleInfo("[Crates] Configurations reloaded.");

        return true;
    }
}
