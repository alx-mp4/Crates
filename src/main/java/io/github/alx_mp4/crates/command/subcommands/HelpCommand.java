package io.github.alx_mp4.crates.command.subcommands;

import io.github.alx_mp4.crates.util.log.LogUtil;
import io.github.alx_mp4.crates.util.misc.ColorUtil;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public final class HelpCommand {

    public static void sendHelp(CommandSender sender) {
        String[] messages = {
                "&5» Crates commands:",
                "&7/crates &f- Displays this content.",
                "&7/crates about &f- About Crates.",
                "&7/crates preview &f- View crate rewards.",
                "&7/crates keys &f- Check how many keys you have.",
                "&7/crates keys <player> &f- Check another player's keys.",
                "&7/crates giveall [amount] &f- Give keys to all online players.",
                "&7/crates setkeys <player> <amount> &f- Set a player's keys.",
                "&7/crates delay <ticks> &f- Set the crate open delay in ticks.",
                "&7/crates roulette &f- Toggle crate roulette spin animation.",
                "&7/crates reload &f- Reload Crates config.",
        };

        for (String message : messages) {
            if (sender instanceof Player) {
                sender.sendMessage(ColorUtil.translateColorCodes(message));
            } else {
                LogUtil.logConsoleInfo(message.replaceAll("&.", ""));
            }
        }
    }
}
