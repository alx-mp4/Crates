package io.github.alx_mp4.crates.command.subcommands;

import io.github.alx_mp4.crates.Crates;
import io.github.alx_mp4.crates.ui.CratePreviewMenu;
import io.github.alx_mp4.crates.util.auth.AccessUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public final class PreviewCommand implements CommandExecutor {

    private final Crates plugin;

    public PreviewCommand(Crates plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!AccessUtil.senderHasPermission(sender, "crates.use",
                "&c» You don't have permission to view the crate rewards.")) {
            return true;
        }

        if (AccessUtil.denyIfNotPlayer(sender, plugin)) return true;

        CratePreviewMenu.openPreview((Player) sender);
        return true;
    }
}
