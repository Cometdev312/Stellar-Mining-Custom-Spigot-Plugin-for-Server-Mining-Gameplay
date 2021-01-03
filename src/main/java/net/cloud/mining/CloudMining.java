package net.cloud.mining;

import net.cloud.mining.commands.MiningCommand;
import net.cloud.mining.events.CloudMiningListener;
import net.cloud.mining.files.FileUtils;
import net.cloud.mining.files.enums.MiningFileType;
import net.cloud.mining.hooks.WorldGuardHook;
import net.cloud.mining.storage.CloudOre;
import net.cloud.mining.storage.OreStorage;
import net.cloud.mining.storage.ReplacedStorage;
import net.cloud.mining.utils.Utils;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.plugin.java.JavaPlugin;

public final class CloudMining extends JavaPlugin {

    @Override
    public void onEnable() {
        /*
        Initiate Core
         */

        core = this;

        /*
        Load Files
         */

        FileUtils.getInstance().init();
        new MiningCommand().register();

        /*
        Load Classes
         */

        utils = new Utils();
        oreStorage = new OreStorage(this);
        replacedStorage = new ReplacedStorage(this);
        worldGuardHook = new WorldGuardHook(this).setEnabled(getServer().getPluginManager().isPluginEnabled("WorldGuard") ? true : false);

        /*
        Load Ores
         */

        FileUtils.getInstance().getFileByType(MiningFileType.BLOCKS).getConfigurationSection("Blocks").getKeys(false).forEach(block -> {
            CloudOre ore = new CloudOre(block);
            getOreStorage().addOre(ore);
        });

        /*
        Load Events
         */

        getServer().getPluginManager().registerEvents(new CloudMiningListener(), this);
    }

    @Override
    public void onDisable() {
        /*
        Set Blocks Back
         */

        getReplacedStorage().clear();

        /*
        Nullify Core
         */

        core = null;
    }

    public boolean isDebugging() {
        try {
            boolean bool = FileUtils.getInstance().getFileByType(MiningFileType.SETTINGS).getBoolean("Loading-Settings.Debug-Mode", true);
            return bool;
        } catch(Exception e) {
            e.printStackTrace();
            return true;
        }
    }

    public ConsoleCommandSender getConsole() {
        return getServer().getConsoleSender();
    }

    private Utils utils;
    private OreStorage oreStorage;
    private ReplacedStorage replacedStorage;
    private WorldGuardHook worldGuardHook;

    public Utils getUtils() {
        return utils;
    }
    public OreStorage getOreStorage() {
        return oreStorage;
    }
    public ReplacedStorage getReplacedStorage() {
        return replacedStorage;
    }
    public WorldGuardHook getWorldGuardHook() {
        return worldGuardHook;
    }

    private static CloudMining core;
    public static CloudMining getCore() {
        return core;
    }
}
