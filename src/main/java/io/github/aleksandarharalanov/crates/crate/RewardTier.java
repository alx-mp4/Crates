package io.github.aleksandarharalanov.crates.crate;

public enum RewardTier {

    COMMON("&7Common", (byte) 8, "#AAAAAA"),
    UNCOMMON("&aUncommon", (byte) 5, "#55FF55"),
    RARE("&9Rare", (byte) 11, "#5555FF"),
    EPIC("&5Epic", (byte) 10, "#AA00AA"),
    LEGENDARY("&6Legendary", (byte) 1, "#FFAA00");

    private final String displayName;
    private final byte particleColor;
    private final String rgbColor;

    RewardTier(String displayName, byte particleColor, String rgbColor) {
        this.displayName = displayName;
        this.particleColor = particleColor;
        this.rgbColor = rgbColor;
    }

    public String getDisplayName() { return displayName; }
    public byte getParticleColor() { return particleColor; }
    public String getRgbColor() { return rgbColor; }

    public static RewardTier fromString(String s) {
        if (s == null) return COMMON;

        String k = s.trim().toUpperCase();
        for (RewardTier t : values()) {
            if (t.name().equals(k)) return t;
        }

        return COMMON;
    }
}
