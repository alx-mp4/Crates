package io.github.alx_mp4.crates.crate.reward;

import io.github.alx_mp4.crates.Crates;
import io.github.alx_mp4.crates.api.event.CrateRewardChosenEvent;
import io.github.alx_mp4.crates.crate.CrateConfig;
import io.github.alx_mp4.crates.ui.CrateRouletteMenu;
import io.github.alx_mp4.crates.util.log.LogUtil;
import io.github.alx_mp4.crates.util.misc.ColorUtil;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import java.security.SecureRandom;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.bukkit.Bukkit.getServer;

public final class RewardHandler {

    private static final SecureRandom random = new SecureRandom();
    private static final List<Reward> rewards = new ArrayList<>();

    private RewardHandler() { }

    public static final class ChosenReward {
        public final int index;
        public final Reward reward;

        public ChosenReward(List<Reward> rewards, int index) {
            this.index = index;
            this.reward = rewards.get(index);
        }
    }

    public static void loadRewards() {
        random.nextInt();
        rewards.clear();
        for (Reward reward : CrateConfig.getRewards()) {
            rewards.add(new Reward(reward.material, reward.data, reward.amount, reward.weight, reward.tier));
        }
        LogUtil.logConsoleInfo(String.format("[Crates] Loaded %d rewards from config.yml", rewards.size()));
    }

    public static void giveReward(Player player, Block crate) {
        if (rewards.isEmpty()) loadRewards();
        if (rewards.isEmpty()) {
            player.sendMessage(ColorUtil.translateColorCodes("&7» No rewards are configured."));
            return;
        }

        ChosenReward chosenReward = getRandomReward(crate, player);
        if (chosenReward == null || chosenReward.reward == null || chosenReward.reward.itemStack.getTypeId() == 0) {
            Crates.getIo().submit(() -> {
                try {
                    int keys = Crates.getSqliteDb().getPlayerKeys(player);
                    Crates.getSqliteDb().setPlayerKeys(player, keys + 1);
                } catch (SQLException e) {
                    player.sendMessage(ColorUtil.translateColorCodes("&4» Database error. Contact an operator."));
                    LogUtil.logConsoleSevere(String.format("[Crates] Database error while setting keys for %s: %s", player.getName(), e));
                }
            });
            return;
        }

        CrateRouletteMenu.playAnimation(player, crate, CrateConfig.getRewards(), chosenReward);
    }

    private static ChosenReward getRandomReward(Block crate, Player player) {
        if (rewards.isEmpty()) return null;

        int total = 0;
        for (Reward r : rewards) total += Math.max(0, r.weight);
        if (total <= 0) return new ChosenReward(rewards, 0);

        int roll = random.nextInt(total);
        int cumulative = 0;

        for (int i = 0; i < rewards.size(); i++) {
            Reward reward = rewards.get(i);
            cumulative += Math.max(0, reward.weight);
            if (roll < cumulative) {
                // --- Dev API start
                CrateRewardChosenEvent event = new CrateRewardChosenEvent(crate, player, reward);
                getServer().getPluginManager().callEvent(event);
                // --- Dev API end
                return new ChosenReward(rewards, i);
            }
        }

        return new ChosenReward(rewards, 0);
    }

}
