/**
 * <b>Crates Dev API events for Bukkit 1060 (legacy CUSTOM_EVENT).</b>
 * <p>This goes in your {@code plugin.yml}:</p>
 * {@code softdepend: [Crates]}
 *
 * <p>This is how you register the listeners you've made for Crates:</p>
 * <pre>{@code
 * PluginManager pm = plugin.getServer().getPluginManager();
 * pm.registerEvent(Event.Type.CUSTOM_EVENT,
 *     new CratesEventBridge(new YOUR_CRATES_LISTENER()),
 *     Event.Priority.CHANGE_ME,
 *     YOUR_PLUGIN_INSTANCE);
 * }</pre>
 */
package io.github.aleksandarharalanov.crates.api;