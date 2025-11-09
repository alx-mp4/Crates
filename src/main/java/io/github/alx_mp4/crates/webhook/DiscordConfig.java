package io.github.alx_mp4.crates.webhook;

import io.github.alx_mp4.crates.Crates;

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
