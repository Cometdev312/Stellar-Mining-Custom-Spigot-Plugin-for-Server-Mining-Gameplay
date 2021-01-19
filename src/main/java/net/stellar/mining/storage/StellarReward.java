package net.stellar.mining.storage;

import net.stellar.mining.files.FileUtils;
import net.stellar.mining.files.enums.MiningFileType;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.List;

public class StellarReward {

    private String oreName;
    private String rewardName;

    private boolean rewardEnabled;
    private double rewardChance;
    private List<String> commands;

    public StellarReward(String oreName, String rewardName) {
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

    public StellarReward setOreName(String name) {
        this.oreName = name;
        return this;
    }

    public StellarReward setRewardName(String name) {
        this.rewardName = name;
        return this;
    }

    public StellarReward setEnabled(boolean enabled) {
        this.rewardEnabled = enabled;
        return this;
    }

    public StellarReward setChance(double chance) {
        this.rewardChance = chance;
        return this;
    }

    public StellarReward setCommands(List<String> commands) {
        this.commands = commands;
        return this;
    }

    public StellarReward load() {
        FileConfiguration config = FileUtils.getInstance().getFileByType(MiningFileType.BLOCKS);

        setEnabled(config.getBoolean(getPath() + "Settings.Enabled", false))
                .setChance(config.getDouble(getPath() + "Settings.Chance", 0))
                .setCommands(config.getStringList(getPath() + "Commands"));

        return this;
    }
}
