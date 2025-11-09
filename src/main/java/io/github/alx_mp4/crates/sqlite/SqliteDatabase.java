package io.github.alx_mp4.crates.sqlite;

import io.github.alx_mp4.crates.util.log.LogUtil;
import org.bukkit.entity.Player;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.*;
import java.util.Locale;

public final class SqliteDatabase implements AutoCloseable {

    private final Connection conn;

    public SqliteDatabase(Path dbPath) {
        try {
            Files.createDirectories(dbPath.getParent());
            try {
                Class.forName("org.sqlite.JDBC");
            } catch (ClassNotFoundException ignored) {}
            this.conn = DriverManager.getConnection("jdbc:sqlite:" + dbPath);

            migrateAndInit();
        } catch (IOException | SQLException e) {
            LogUtil.logConsoleSevere(String.format("[Crates] SQLite initialization failed: %s", e));
            throw new RuntimeException(e);
        }
    }

    private void migrateAndInit() throws SQLException {
        try (Statement st = conn.createStatement()) {
            st.execute("PRAGMA foreign_keys = ON");
            st.execute("PRAGMA journal_mode = WAL");
            st.execute("PRAGMA synchronous = NORMAL");
            st.execute("PRAGMA busy_timeout = 5000");

            // Base table: (Crates v2.0.3 and older)
            st.execute("CREATE TABLE IF NOT EXISTS crates (" +
                    "player_name  TEXT    NOT NULL PRIMARY KEY, " +
                    "player_keys  INTEGER NOT NULL DEFAULT 0" +
                    ")");

            // Schema migration: Implement new columns
            if (!columnExists("crates", "crates_opened")) {
                st.execute("ALTER TABLE crates ADD COLUMN crates_opened INTEGER NOT NULL DEFAULT 0");
            }
        }
    }

    private boolean columnExists(String table, String column) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement("PRAGMA table_info(" + table + ")")) {
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    String name = rs.getString("name");
                    if (column.equalsIgnoreCase(name)) return true;
                }
            }
        }
        return false;
    }

    private static String key(String name) { return name.toLowerCase(Locale.ROOT); }

    // -------- Player-based API --------
    public void ensurePlayer(Player player) throws SQLException { ensurePlayer(player.getName()); }

    public int getPlayerKeys(Player player) throws SQLException { return getPlayerKeys(player.getName()); }

    public void setPlayerKeys(Player player, int value) throws SQLException { setPlayerKeys(player.getName(), value); }

    public void addPlayerKeys(Player player, int value) throws SQLException { addPlayerKeys(player.getName(), value); }

    public void deletePlayer(Player player) throws SQLException { deletePlayer(player.getName()); }

    public int getCratesOpened(Player player) throws SQLException { return getCratesOpened(player.getName()); }

    public void setCratesOpened(Player player, int value) throws SQLException { setCratesOpened(player.getName(), value); }

    public void ensurePlayer(String playerName) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement(
                "INSERT OR IGNORE INTO crates(player_name) VALUES (?)")) {
            ps.setString(1, key(playerName));
            ps.executeUpdate();
        }
    }

    public int getPlayerKeys(String playerName) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement(
                "SELECT player_keys FROM crates WHERE player_name = ?")) {
            ps.setString(1, key(playerName));
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? rs.getInt(1) : 0;
            }
        }
    }

    public void setPlayerKeys(String playerName, int value) throws SQLException {
        ensurePlayer(playerName);
        try (PreparedStatement ps = conn.prepareStatement(
                "UPDATE crates SET player_keys = ? WHERE player_name = ?")) {
            ps.setInt(1, Math.max(0, value));
            ps.setString(2, key(playerName));
            ps.executeUpdate();
        }
    }

    public void addPlayerKeys(String playerName, int value) throws SQLException {
        ensurePlayer(playerName);
        try (PreparedStatement ps = conn.prepareStatement(
                "UPDATE crates SET player_keys = MAX(0, player_keys + ?) WHERE player_name = ?")) {
            ps.setInt(1, value);
            ps.setString(2, key(playerName));
            ps.executeUpdate();
        }
    }

    public void deletePlayer(String playerName) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement(
                "DELETE FROM crates WHERE player_name = ?")) {
            ps.setString(1, key(playerName));
            ps.executeUpdate();
        }
    }

    public int getCratesOpened(String playerName) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement(
                "SELECT crates_opened FROM crates WHERE player_name = ?")) {
            ps.setString(1, key(playerName));
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? rs.getInt(1) : 0;
            }
        }
    }

    public void setCratesOpened(String playerName, int value) throws SQLException {
        ensurePlayer(playerName);
        try (PreparedStatement ps = conn.prepareStatement(
                "UPDATE crates SET crates_opened = ? WHERE player_name = ?")) {
            ps.setInt(1, Math.max(0, value));
            ps.setString(2, key(playerName));
            ps.executeUpdate();
        }
    }

    @Override
    public void close() throws SQLException {
        if (conn != null && !conn.isClosed()) conn.close();
    }

    public Connection getConnection() {
        return conn;
    }
}
