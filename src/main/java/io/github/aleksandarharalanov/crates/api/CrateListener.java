package io.github.aleksandarharalanov.crates.api;

import io.github.aleksandarharalanov.crates.api.event.CrateOpenEvent;
import io.github.aleksandarharalanov.crates.api.event.CrateRewardEvent;
import org.bukkit.event.Listener;

/**
 * <b>Base listener for Crates custom events.</b>
 *
 * <pre>{@code
 * PluginManager pm = plugin.getServer().getPluginManager();
 * pm.registerEvent(Event.Type.CUSTOM_EVENT,
 *     new CratesEventBridge(new YOUR_CRATES_LISTENER()),
 *     Event.Priority.CHANGE_ME,
 *     YOUR_PLUGIN_INSTANCE);
 * }</pre>
 */
public class CrateListener implements Listener {

    public CrateListener() {}

    public void onCrateOpen(CrateOpenEvent event) {}

    public void onCrateReward(CrateRewardEvent event) {}
}
