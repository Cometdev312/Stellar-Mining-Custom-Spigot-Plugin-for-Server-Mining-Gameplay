package net.stellar.mining.storage;

import net.stellar.mining.StellarMining;
import net.stellar.mining.files.FileUtils;
import net.stellar.mining.files.enums.MiningFileType;
import net.stellar.mining.support.XMaterial;
import net.stellar.mining.utils.RewardStorage;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.List;

public class StellarOre {

    private String name;

    //Settings
    private boolean enabled;
    private String type;
    private Material typeAsMaterial;
    private Integer delay;

    //Permission Settings
    private boolean permissionsEnabled;
    private String permissionValue;

    //Region Settings
    private boolean regionsEnabled;
    private List<String> whitelistedRegions;

    //Chance Settings
    private boolean nothingEnabled;
    private double nothingChance;

    //Rewards - With name of reward in blocks.yml
    private RewardStorage<StellarReward> rewards;

    public StellarOre(String name) {
        setName(name);
        loadFromFile();

        if(StellarMining.getCore().isDebugging())
            StellarMining.getCore().getConsole().sendMessage(StellarMining.getCore().getUtils().getColor(" &6&l* &a[Loaded Ore] &f" + getName() + " &7with &f" + getRewardSize() + "x &7rewards"));
    }

    public Integer getRewardSize() {
        if(isNothingEnabled()) return getRewards().size() - 1;
        return getRewards().size();
    }

    public String getName() {
        return name;
    }
    public String getPath() {
        return "Blocks." + getName() + ".";
    }

    public boolean isEnabled() {
        return enabled;
    }

    public String getType() {
        return type;
    }

    public Material getTypeAsMaterial() {
        return typeAsMaterial;
    }

    public Integer getDelay() {
        return delay;
    }

    public boolean isPermissionsEnabled() {
        return permissionsEnabled;
    }

    public String getRequiredPermission() {
        return permissionValue;
    }

    public boolean isRegionsEnabled() {
        return regionsEnabled;
    }

    public List<String> getWhitelistedRegions() {
        return whitelistedRegions;
    }

    public boolean isNothingEnabled() {
        return nothingEnabled;
    }

    public double getNothingChance() {
        return nothingChance;
    }

    public RewardStorage<StellarReward> getRewards() {
        return rewards;
    }

    public StellarReward getRandomReward() {
        return rewards.next();
    }

    public StellarOre setName(String name) {
        this.name = name;
        return this;
    }

    public StellarOre setEnabled(boolean enabled) {
        this.enabled = enabled;
        return this;
    }

    public StellarOre setType(String type) {
        this.type = type;
        return this;
    }

    public StellarOre setTypeAsMaterial(Material material) {
        typeAsMaterial = material;
        return this;
    }

    public StellarOre setDelay(Integer delay) {
        this.delay = delay;
        return this;
    }

    public StellarOre setPermissionEnabled(boolean enabled) {
        this.permissionsEnabled = enabled;
        return this;
    }

    public StellarOre setPermission(String permission) {
        this.permissionValue = permission;
        return this;
    }

    public StellarOre setRegionsEnabled(boolean enabled) {
        this.regionsEnabled = enabled;
        return this;
    }

    public StellarOre setRegions(List<String> regions) {
        this.whitelistedRegions = regions;
        return this;
    }

    public StellarOre setNothingEnabled(boolean enabled) {
        this.nothingEnabled = enabled;
        return this;
    }

    public StellarOre setNothingChance(double chance) {
        this.nothingChance = chance;
        return this;
    }

    public StellarOre setRewards(RewardStorage<StellarReward> rewards) {
        this.rewards = rewards;
        return this;
    }

    /*
    Load Data from File
     */

    public StellarOre loadFromFile() {
        FileConfiguration config = FileUtils.getInstance().getFileByType(MiningFileType.BLOCKS);

        setEnabled(config.getBoolean(getPath() + ".Settings.Enabled", false))
                .setType(config.getString(getPath() + "Settings.Ore-Type", "DIAMOND_ORE"))
                .setDelay(config.getInt(getPath() + "Settings.Delay", 100))
                .setPermissionEnabled(config.getBoolean(getPath() + "Permission-Settings.Permission-Required", false))
                .setPermission(config.getString(getPath() + "Permission-Settings.Permission-Value", "Stellarmining.diamondore"))
                .setRegionsEnabled(config.getBoolean(getPath() + "Region-Settings.Regions-Required", false))
                .setRegions(config.getStringList(getPath() + "Region-Settings.Whitelisted-Regions"))
                .setNothingEnabled(config.getBoolean(getPath() + "Chance-Settings.Nothing-Chance-Enabled", false))
                .setNothingChance(config.getDouble(getPath() + "Chance-Settings.Nothing-Chance-Value", 0.0))
                .setRewards(loadRewards());

        loadMaterial();

        return this;
    }

    public RewardStorage<StellarReward> loadRewards() {
        FileConfiguration config = FileUtils.getInstance().getFileByType(MiningFileType.BLOCKS);
        RewardStorage<StellarReward> rewards = new RewardStorage<>();

        if(isNothingEnabled()) {
            rewards.add(getNothingChance(), null);
        }

        if(config.isConfigurationSection(getPath() + "Reward-Settings")) {
            config.getConfigurationSection(getPath() + "Reward-Settings").getKeys(false).forEach(reward -> {
                StellarReward StellarReward = new StellarReward(getName(), reward).load();
                if (StellarReward.isRewardEnabled()) rewards.add(StellarReward.getChance(), StellarReward);
            });
        }

        return rewards;
    }

    public StellarOre loadMaterial() {
        XMaterial xMaterial = XMaterial.matchXMaterial(getType()).get();
        if(xMaterial == null) {
            setTypeAsMaterial(Material.BEDROCK);
        } else {
            setTypeAsMaterial(xMaterial.parseMaterial());
        }
        return this;
    }

}
