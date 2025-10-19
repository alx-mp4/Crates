package io.github.aleksandarharalanov.crates.command;

import io.github.aleksandarharalanov.crates.Crates;
import io.github.aleksandarharalanov.crates.command.subcommands.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.util.HashMap;
import java.util.Map;

public final class CratesCommand implements CommandExecutor {

    private final Map<String, CommandExecutor> subcommands = new HashMap<>();

    public CratesCommand(Crates plugin) {
        subcommands.put("about", new AboutCommand(plugin));
        subcommands.put("keys", new KeysCommand());
        subcommands.put("giveall", new GiveAllKeysCommand());
        subcommands.put("setkeys", new SetKeysCommand());
        subcommands.put("delay", new DelayCommand());
        subcommands.put("reload", new ReloadCommand());
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            HelpCommand.sendHelp(sender);
            return true;
        }

        CommandExecutor sub = subcommands.get(args[0].toLowerCase());
        if (sub != null) return sub.onCommand(sender, command, label, args);

        HelpCommand.sendHelp(sender);
        return true;
    }
}
