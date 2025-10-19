package io.github.aleksandarharalanov.crates.listener.player;

import io.github.aleksandarharalanov.crates.Crates;
import io.github.aleksandarharalanov.crates.util.log.LogUtil;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerListener;

import java.sql.SQLException;

public class PlayerJoinListener extends PlayerListener {

    @Override
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        Crates.getIo().submit(() -> {
            try {
                Crates.getSqliteDb().ensurePlayer(player);
            } catch (SQLException ex) {
                LogUtil.logConsoleSevere("[Crates] Join seed failed for " + player.getName() + ": " + ex.getMessage());
            }
        });
    }
}
