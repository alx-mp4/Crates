package io.github.aleksandarharalanov.crates.command.subcommands;

import io.github.aleksandarharalanov.crates.crate.CrateConfig;
import io.github.aleksandarharalanov.crates.util.auth.AccessUtil;
import io.github.aleksandarharalanov.crates.util.misc.ColorUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public final class RouletteCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!AccessUtil.senderHasPermission(sender, "crates.config",
                "&c» You don't have permission to toggle the crate roulette spin animation.")) {
            return true;
        }

        sender.sendMessage(ColorUtil.translateColorCodes(String.format("&7» Crate roulette spin animation toggled to &5%s&7.", !CrateConfig.getRouletteAnimation())));
        CrateConfig.setRouletteAnimation(!CrateConfig.getRouletteAnimation());
        return true;
    }
}
