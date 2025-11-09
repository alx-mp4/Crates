package io.github.alx_mp4.crates.crate.reward;

public enum RewardTier {

    COMMON("&7Common", (byte) 8, "#AAAAAA"),
    UNCOMMON("&aUncommon", (byte) 5, "#55FF55"),
    RARE("&9Rare", (byte) 11, "#5555FF"),
    EPIC("&5Epic", (byte) 10, "#AA00AA"),
    LEGENDARY("&6Legendary", (byte) 1, "#FFAA00");

    private final String displayName;
    private final byte particleColor;
    private final String embedColor;

    RewardTier(String displayName, byte particleColor, String embedColor) {
        this.displayName = displayName;
        this.particleColor = particleColor;
        this.embedColor = embedColor;
    }

    public String getDisplayName() { return displayName; }
    public byte getParticleColor() { return particleColor; }
    public String getEmbedColor() { return embedColor; }

    public static RewardTier fromString(String string) {
        if (string == null) return COMMON;

        String key = string.trim().toUpperCase();
        for (RewardTier tier : values()) {
            if (tier.name().equals(key)) return tier;
        }

        return COMMON;
    }
}
