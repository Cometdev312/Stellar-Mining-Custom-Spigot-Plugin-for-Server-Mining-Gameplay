package net.cloud.mining.storage;

import net.cloud.mining.CloudMining;
import net.cloud.mining.files.FileUtils;
import net.cloud.mining.files.enums.MiningFileType;
import net.cloud.mining.support.XMaterial;
import net.cloud.mining.utils.RewardStorage;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.List;

public class CloudOre {

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
    private RewardStorage<CloudReward> rewards;

    public CloudOre(String name) {
        setName(name);
        loadFromFile();

        if(CloudMining.getCore().isDebugging())
            CloudMining.getCore().getConsole().sendMessage(CloudMining.getCore().getUtils().getColor(" &6&l* &a[Loaded Ore] &f" + getName() + " &7with &f" + getRewardSize() + "x &7rewards"));
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

    public RewardStorage<CloudReward> getRewards() {
        return rewards;
    }

    public CloudReward getRandomReward() {
        return rewards.next();
    }

    public CloudOre setName(String name) {
        this.name = name;
        return this;
    }

    public CloudOre setEnabled(boolean enabled) {
        this.enabled = enabled;
        return this;
    }

    public CloudOre setType(String type) {
        this.type = type;
        return this;
    }

    public CloudOre setTypeAsMaterial(Material material) {
        typeAsMaterial = material;
        return this;
    }

    public CloudOre setDelay(Integer delay) {
        this.delay = delay;
        return this;
    }

    public CloudOre setPermissionEnabled(boolean enabled) {
        this.permissionsEnabled = enabled;
        return this;
    }

    public CloudOre setPermission(String permission) {
        this.permissionValue = permission;
        return this;
    }

    public CloudOre setRegionsEnabled(boolean enabled) {
        this.regionsEnabled = enabled;
        return this;
    }

    public CloudOre setRegions(List<String> regions) {
        this.whitelistedRegions = regions;
        return this;
    }

    public CloudOre setNothingEnabled(boolean enabled) {
        this.nothingEnabled = enabled;
        return this;
    }

    public CloudOre setNothingChance(double chance) {
        this.nothingChance = chance;
        return this;
    }

    public CloudOre setRewards(RewardStorage<CloudReward> rewards) {
        this.rewards = rewards;
        return this;
    }

    /*
    Load Data from File
     */

    public CloudOre loadFromFile() {
        FileConfiguration config = FileUtils.getInstance().getFileByType(MiningFileType.BLOCKS);

        setEnabled(config.getBoolean(getPath() + ".Settings.Enabled", false))
                .setType(config.getString(getPath() + "Settings.Ore-Type", "DIAMOND_ORE"))
                .setDelay(config.getInt(getPath() + "Settings.Delay", 100))
                .setPermissionEnabled(config.getBoolean(getPath() + "Permission-Settings.Permission-Required", false))
                .setPermission(config.getString(getPath() + "Permission-Settings.Permission-Value", "cloudmining.diamondore"))
                .setRegionsEnabled(config.getBoolean(getPath() + "Region-Settings.Regions-Required", false))
                .setRegions(config.getStringList(getPath() + "Region-Settings.Whitelisted-Regions"))
                .setNothingEnabled(config.getBoolean(getPath() + "Chance-Settings.Nothing-Chance-Enabled", false))
                .setNothingChance(config.getDouble(getPath() + "Chance-Settings.Nothing-Chance-Value", 0.0))
                .setRewards(loadRewards());

        loadMaterial();

        return this;
    }

    public RewardStorage<CloudReward> loadRewards() {
        FileConfiguration config = FileUtils.getInstance().getFileByType(MiningFileType.BLOCKS);
        RewardStorage<CloudReward> rewards = new RewardStorage<>();

        if(isNothingEnabled()) {
            rewards.add(getNothingChance(), null);
        }

        config.getConfigurationSection(getPath() + "Reward-Settings").getKeys(false).forEach(reward -> {
            CloudReward cloudReward = new CloudReward(getName(), reward).load();
            if(cloudReward.isRewardEnabled()) rewards.add(cloudReward.getChance(), cloudReward);
        });

        return rewards;
    }

    public CloudOre loadMaterial() {
        XMaterial xMaterial = XMaterial.matchXMaterial(getType()).get();
        if(xMaterial == null) {
            setTypeAsMaterial(Material.BEDROCK);
        } else {
            setTypeAsMaterial(xMaterial.parseMaterial());
        }
        return this;
    }

}
