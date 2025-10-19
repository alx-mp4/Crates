package io.github.aleksandarharalanov.crates.crate;

import io.github.aleksandarharalanov.crates.Crates;
import io.github.aleksandarharalanov.crates.util.log.LogUtil;
import org.bukkit.Material;
import org.bukkit.util.config.Configuration;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public final class CrateConfig {

    private CrateConfig() {}

    public static final class RewardEntry {

        public final Material material;
        public final byte data;
        public final int amount;
        public final int weight;
        public final RewardTier tier;

        public RewardEntry(Material material, byte data, int amount, int weight, RewardTier tier) {
            this.material = material;
            this.data = data;
            this.amount = amount;
            this.weight = weight;
            this.tier = tier;
        }
    }

    public static int getCrateOpenDelay() {
        return Crates.getConfig().getInt("crate-open-delay", 20);
    }

    public static void setCrateOpenDelay(int value) {
        Crates.getConfig().setProperty("crate-open-delay", value);
        Crates.getConfig().save();
    }

    public static List<RewardEntry> getRewards() {
        Configuration cfg = Crates.getConfig();
        List<Object> raw = cfg.getList("rewards");

        List<RewardEntry> out = new ArrayList<>();
        if (raw == null) {
            LogUtil.logConsoleWarning("[Crates] No 'rewards' section found.");
            return out;
        }

        for (int i = 0; i < raw.size(); i++) {
            Object o = raw.get(i);
            if (!(o instanceof Map)) {
                LogUtil.logConsoleWarning("[Crates] Reward [" + i + "]: not a map; skipped.");
                continue;
            }

            @SuppressWarnings("unchecked")
            Map<String, Object> map = (Map<String, Object>) o;

            Object specObj = map.get("id");
            if (specObj == null) {
                LogUtil.logConsoleWarning("[Crates] Reward [" + i + "]: missing 'id' (e.g. \"35:5\"). Skipped.");
                continue;
            }
            String spec = specObj.toString().trim();
            String[] idData = spec.contains(":") ? spec.split(":", 2) : new String[] { spec, "0" };

            int id;
            try {
                id = Integer.parseInt(idData[0].trim());
            } catch (NumberFormatException ignored) {
                LogUtil.logConsoleWarning("[Crates] Reward [" + i + "]: invalid id '" + idData[0] + "'. Skipped.");
                continue;
            }

            int d;
            try {
                d = Integer.parseInt(idData[1].trim());
            } catch (NumberFormatException ignored) {
                d = 0;
            }

            if (d < 0) d = 0;
            else if (d > 127) d = 127;

            Material mat = Material.getMaterial(id);
            if (mat == null) {
                LogUtil.logConsoleWarning("[Crates] Reward [" + i + "]: unknown item ID " + id + ". Skipped.");
                continue;
            }

            int amount = asInt(map.get("amount"));
            if (amount < 1) amount = 1;

            int weight = asInt(map.get("weight"));
            if (weight < 1) weight = 1;

            String tierName = String.valueOf(map.get("tier"));
            RewardTier tier = RewardTier.fromString(tierName);

            byte data = (byte) d;
            out.add(new RewardEntry(mat, data, amount, weight, tier));
        }
        return out;
    }

    private static int asInt(Object o) {
        if (o instanceof Number) return ((Number) o).intValue();

        try {
            return o == null ? 1 : Integer.parseInt(o.toString().trim());
        } catch (NumberFormatException ignored) {
            return 1;
        }
    }
}
