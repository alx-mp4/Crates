package io.github.alx_mp4.crates.crate;

import io.github.alx_mp4.crates.Crates;
import io.github.alx_mp4.crates.crate.reward.Reward;
import io.github.alx_mp4.crates.crate.reward.RewardTier;
import io.github.alx_mp4.crates.util.log.LogUtil;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.util.config.Configuration;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public final class CrateConfig {

    private CrateConfig() {}

    public static int getCrateOpenDelay() { return Crates.getConfig().getInt("crate-open-delay", 20); }

    public static void setCrateOpenDelay(int value) {
        Crates.getConfig().setProperty("crate-open-delay", value);
        Crates.getConfig().save();
    }

    public static boolean getRouletteAnimation() { return Crates.getConfig().getBoolean("roulette-animation", true); }

    public static void setRouletteAnimation(boolean bool) {
        Crates.getConfig().setProperty("roulette-animation", bool);
        Crates.getConfig().save();
    }

    public static List<Reward> getRewards() {
        Configuration config = Crates.getConfig();
        List<Object> raw = config.getList("rewards");

        List<Reward> out = new ArrayList<>();
        if (raw == null) {
            LogUtil.logConsoleWarning("[Crates] No 'rewards' section found.");
            return out;
        }

        for (int i = 0; i < raw.size(); i++) {
            Object object = raw.get(i);
            if (!(object instanceof Map)) {
                LogUtil.logConsoleWarning(String.format("[Crates] Reward [%d]: not a map; skipped.", i));
                continue;
            }

            @SuppressWarnings("unchecked")
            Map<String, Object> map = (Map<String, Object>) object;

            Object specObj = map.get("id");
            if (specObj == null) {
                LogUtil.logConsoleWarning(String.format("[Crates] Reward [%d]: missing 'id'. Skipped.", i));
                continue;
            }
            String spec = specObj.toString().trim();
            String[] idData = spec.contains(":") ? spec.split(":", 2) : new String[] { spec, "0" };

            int id;
            try {
                id = Integer.parseInt(idData[0].trim());
            } catch (NumberFormatException ignored) {
                LogUtil.logConsoleWarning(String.format("[Crates] Reward [%d]: invalid id '%s'. Skipped.", i, idData[0]));
                continue;
            }

            int data;
            try {
                data = Integer.parseInt(idData[1].trim());
            } catch (NumberFormatException ignored) {
                data = 0;
            }

            if (data < 0) data = 0;
            else if (data > 127) data = 127;

            Material mat = Material.getMaterial(id);
            if (mat == null) {
                LogUtil.logConsoleWarning(String.format("[Crates] Reward [%d]: unknown item ID %d. Skipped.", i, id));
                continue;
            }

            int amount = asInt(map.get("amount"));
            if (amount < 1) amount = 1;

            int weight = asInt(map.get("weight"));
            if (weight < 1) weight = 1;

            String tierName = String.valueOf(map.get("tier"));
            RewardTier tier = RewardTier.fromString(tierName);

            out.add(new Reward(mat, (byte) data, amount, weight, tier));
        }
        return out;
    }

    private static int asInt(Object object) {
        if (object instanceof Number) return ((Number) object).intValue();

        try {
            return object == null ? 1 : Integer.parseInt(object.toString().trim());
        } catch (NumberFormatException ignored) {
            return 1;
        }
    }

    public static boolean isBlockCrate(Block block) { return block.getTypeId() == 33 && block.getData() == (byte) 7; }
}
