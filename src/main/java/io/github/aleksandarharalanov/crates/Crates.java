package io.github.aleksandarharalanov.crates;

import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import io.github.aleksandarharalanov.crates.block.CustomLockedChest;
import io.github.aleksandarharalanov.crates.command.CratesCommand;
import io.github.aleksandarharalanov.crates.listener.block.BlockBreakListener;
import io.github.aleksandarharalanov.crates.listener.block.BlockPistonListener;
import io.github.aleksandarharalanov.crates.listener.block.BlockPlaceListener;
import io.github.aleksandarharalanov.crates.listener.entity.EntityExplodeListener;
import io.github.aleksandarharalanov.crates.listener.player.PlayerInteractListener;
import io.github.aleksandarharalanov.crates.sqlite.SqliteDatabase;
import io.github.aleksandarharalanov.crates.util.config.ConfigUtil;
import io.github.aleksandarharalanov.crates.util.log.UpdateUtil;
import net.minecraft.server.Block;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Event.Type;
import org.bukkit.event.Event.Priority;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import io.github.aleksandarharalanov.crates.util.log.LogUtil;

import java.nio.file.Path;
import java.sql.SQLException;
import java.util.concurrent.ExecutorService;

public class Crates extends JavaPlugin {

    private static Crates plugin;
    private static ConfigUtil config;
    private static ExecutorService io;
    private static SqliteDatabase sqliteDb;

    @Override
    public void onEnable() {
        UpdateUtil.checkAvailablePluginUpdates(this,
                "https://api.github.com/repos/AleksandarHaralanov/Crates/releases/latest");

        plugin = this;

        try {
            Block.byId[Block.LOCKED_CHEST.id] = null;
            final CustomLockedChest customLockedChest = new CustomLockedChest(Block.LOCKED_CHEST.id);
            Block.byId[Block.LOCKED_CHEST.id] = customLockedChest;
        } catch (Exception e) {
            Block.byId[Block.LOCKED_CHEST.id] = Block.LOCKED_CHEST;
            LogUtil.logConsoleSevere(String.format("[%s] Could not register custom block class: %s",
                    getDescription().getName(), e));
        }

        io = java.util.concurrent.Executors.newSingleThreadExecutor();
        try {
            Path dbPath = getDataFolder().toPath().resolve("crates.db");
            sqliteDb = new SqliteDatabase(dbPath);
            LogUtil.logConsoleInfo("[Crates] SQLite ready at: " + dbPath);
        } catch (RuntimeException e) {
            LogUtil.logConsoleSevere("[Crates] Failed to init SQLite: " + e.getMessage());
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        config = new ConfigUtil(this, "config.yml");
        config.load();

        PluginManager pm = getServer().getPluginManager();
        final BlockBreakListener bbl = new BlockBreakListener();
        final BlockPistonListener bpl = new BlockPistonListener();
        final BlockPlaceListener bpl2 = new BlockPlaceListener();
        final EntityExplodeListener eel = new EntityExplodeListener();
        final PlayerInteractListener pil = new PlayerInteractListener();
        pm.registerEvent(Type.BLOCK_BREAK, bbl, Priority.Lowest, this);
        pm.registerEvent(Type.BLOCK_PISTON_EXTEND, bpl, Priority.Lowest, this);
        pm.registerEvent(Type.BLOCK_PISTON_RETRACT, bpl, Priority.Lowest, this);
        pm.registerEvent(Type.BLOCK_PLACE, bpl2, Priority.Lowest, this);
        pm.registerEvent(Type.ENTITY_EXPLODE, eel, Priority.Lowest, this);
        pm.registerEvent(Type.PLAYER_INTERACT, pil, Priority.Lowest, this);

        final CratesCommand command = new CratesCommand(this);
        getCommand("crates").setExecutor(command);

        LogUtil.logConsoleInfo(String.format("[%s] v%s Enabled.",
                getDescription().getName(), getDescription().getVersion()));
    }

    @Override
    public void onDisable() {
        Block.byId[Block.LOCKED_CHEST.id] = Block.LOCKED_CHEST;

        try {
            if (sqliteDb != null) sqliteDb.close();
        } catch (SQLException e) {
            LogUtil.logConsoleSevere(String.format(
                    "[%s] An exception occurred whe closing SQLite database connection: %s", getDescription().getName(), e));
        }

        if (io != null) io.shutdown();

        LogUtil.logConsoleInfo(String.format("[%s] v%s Disabled.",
                getDescription().getName(), getDescription().getVersion()));
    }

    public static boolean checkWorldGuard(Player player, org.bukkit.block.Block block) {
        WorldGuardPlugin wg = (WorldGuardPlugin) Bukkit.getServer().getPluginManager().getPlugin("WorldGuard");
        return wg != null && wg.isEnabled() && !wg.canBuild(player, block.getLocation());
    }

    public static Crates getInstance() { return plugin; }

    public static ConfigUtil getConfig() { return config; }

    public static ExecutorService getIo() { return io; }

    public static SqliteDatabase getSqliteDb() { return sqliteDb; }
}