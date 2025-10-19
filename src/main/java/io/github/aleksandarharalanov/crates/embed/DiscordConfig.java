package io.github.aleksandarharalanov.crates.embed;

import io.github.aleksandarharalanov.crates.Crates;

public final class DiscordConfig {

    private DiscordConfig() {}

    public static boolean getEnabled() {
        return Crates.getConfig().getBoolean("webhook.enabled", false);
    }

    public static String getWebhookUrl() {
        return Crates.getConfig().getString("webhook.url");
    }

    public static String getAvatarApi() {
        return Crates.getConfig().getString("webhook.avatar-api");
    }
}
