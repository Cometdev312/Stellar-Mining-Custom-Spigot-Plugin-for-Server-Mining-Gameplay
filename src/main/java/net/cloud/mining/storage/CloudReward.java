package net.cloud.mining.storage;

import net.cloud.mining.files.FileUtils;
import net.cloud.mining.files.enums.MiningFileType;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.List;

public class CloudReward {

    private String oreName;
    private String rewardName;

    private boolean rewardEnabled;
    private double rewardChance;
    private List<String> commands;

    public CloudReward(String oreName, String rewardName) {
        this.oreName = oreName;
        this.rewardName = rewardName;
    }

    public String getOreName() {
        return oreName;
    }

    public String getRewardName() {
        return rewardName;
    }

    public boolean isRewardEnabled() {
        return rewardEnabled;
    }

    public double getChance() {
        return rewardChance;
    }

    public List<String> getCommands() {
        return commands;
    }

    public String getPath() {
        return "Blocks." + getOreName() + ".Reward-Settings." + getRewardName() + ".";
    }

    public CloudReward setOreName(String name) {
        this.oreName = name;
        return this;
    }

    public CloudReward setRewardName(String name) {
        this.rewardName = name;
        return this;
    }

    public CloudReward setEnabled(boolean enabled) {
        this.rewardEnabled = enabled;
        return this;
    }

    public CloudReward setChance(double chance) {
        this.rewardChance = chance;
        return this;
    }

    public CloudReward setCommands(List<String> commands) {
        this.commands = commands;
        return this;
    }

    public CloudReward load() {
        FileConfiguration config = FileUtils.getInstance().getFileByType(MiningFileType.BLOCKS);

        setEnabled(config.getBoolean(getPath() + "Settings.Enabled", false))
                .setChance(config.getDouble(getPath() + "Settings.Chance", 0))
                .setCommands(config.getStringList(getPath() + "Commands"));

        return this;
    }
}
