package net.stellar.mining;

import net.stellar.mining.commands.MiningCommand;
import net.stellar.mining.events.StellarMiningListener;
import net.stellar.mining.files.FileUtils;
import net.stellar.mining.files.enums.MiningFileType;
import net.stellar.mining.hooks.WorldGuardHook;
import net.stellar.mining.storage.StellarOre;
import net.stellar.mining.storage.OreStorage;
import net.stellar.mining.storage.ReplacedStorage;
import net.stellar.mining.utils.Utils;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.plugin.java.JavaPlugin;

public final class StellarMining extends JavaPlugin {

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
            StellarOre ore = new StellarOre(block);
            getOreStorage().addOre(ore);
        });

        /*
        Load Events
         */

        getServer().getPluginManager().registerEvents(new StellarMiningListener(), this);
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

    private static StellarMining core;
    public static StellarMining getCore() {
        return core;
    }
}
