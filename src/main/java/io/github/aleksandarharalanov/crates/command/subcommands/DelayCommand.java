package io.github.aleksandarharalanov.crates.command.subcommands;

import io.github.aleksandarharalanov.crates.crate.CrateConfig;
import io.github.aleksandarharalanov.crates.util.auth.AccessUtil;
import io.github.aleksandarharalanov.crates.util.misc.ColorUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public final class DelayCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!AccessUtil.senderHasPermission(sender, "crates.config",
                "&c» You don't have permission to change crate open delay.")) {
            return true;
        }

        if (args.length < 2) {
            sender.sendMessage(ColorUtil.translateColorCodes("&7» Usage: /crates delay <ticks>"));
            return true;
        }

        int value;
        try {
            value = Integer.parseInt(args[1]);
            if (value < 0) value = 0;
        } catch (NumberFormatException ignored) {
            sender.sendMessage(ColorUtil.translateColorCodes("&7» Value must be a non-negative number."));
            return true;
        }

        sender.sendMessage(ColorUtil.translateColorCodes(String.format(
                "&7» Set crate open delay to &5%d ticks&7.", value)));
        CrateConfig.setCrateOpenDelay(value);
        return true;
    }
}
