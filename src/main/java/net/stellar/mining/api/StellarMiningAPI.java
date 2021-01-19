package net.stellar.mining.api;

import net.stellar.mining.StellarMining;
import net.stellar.mining.storage.StellarOre;
import org.bukkit.block.Block;

import java.util.Iterator;
import java.util.Map;

public class StellarMiningAPI {

    public Map<String, StellarOre> getRegisteredOres() {
        return StellarMining.getCore().getOreStorage().getMap();
    }

    public Iterator<String> getOresByName() {
        return StellarMining.getCore().getOreStorage().getOresByName();
    }

    public Iterator<StellarOre> getOres() {
        return StellarMining.getCore().getOreStorage().getOres();
    }

    public boolean isOre(StellarOre ore) {
        return StellarMining.getCore().getOreStorage().containsOre(ore);
    }

    public boolean isOre(String name) {
        return StellarMining.getCore().getOreStorage().containsOre(name);
    }

    public boolean canBreakBlock(Block block) {
        if(StellarMining.getCore().getWorldGuardHook().canBreakBlock(block)) return true;
        return false;
    }

    public StellarOre getOreBreakable(Block block) {
        StellarOre ore = StellarMining.getCore().getWorldGuardHook().getOreBreakable(block);
        return ore;
    }

    private static StellarMiningAPI api;
    public static StellarMiningAPI getAPI() {
        if(api == null) api = new StellarMiningAPI();
        return api;
    }
}
