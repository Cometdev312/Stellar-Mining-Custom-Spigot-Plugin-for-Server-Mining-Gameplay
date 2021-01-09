package net.stellar.mining.hooks;

import com.sk89q.worldguard.bukkit.RegionContainer;
import com.sk89q.worldguard.bukkit.RegionQuery;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import net.stellar.mining.StellarMining;
import net.stellar.mining.api.StellarMiningAPI;
import net.stellar.mining.storage.StellarOre;
import org.bukkit.block.Block;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class WorldGuardHook {

    public StellarMining plugin;
    private boolean enabled;

    public WorldGuardHook(StellarMining plugin) {
        this.plugin = plugin;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public WorldGuardHook setEnabled(boolean bool) {
        this.enabled = bool;
        return this;
    }

    public List<String> getRegionsInArea(Block block) {
        List<String> regions = new ArrayList<>();

        RegionContainer container = WorldGuardPlugin.inst().getRegionContainer();
        RegionQuery query = container.createQuery();
        ApplicableRegionSet set = query.getApplicableRegions(block.getLocation());

        for(ProtectedRegion region : set) {
            regions.add(region.getId());
        }

        return regions;
    }

    public boolean canBreakBlock(Block block) {
        List<String> regions = new ArrayList<>();

        if(isEnabled()) regions = getRegionsInArea(block);

        Iterator<StellarOre> ores = StellarMiningAPI.getAPI().getOres();

        while(ores.hasNext()) {
            StellarOre ore = ores.next();
            if(ore.getTypeAsMaterial().equals(block.getType())) {
                if(!(isEnabled())) return true;
                if(!(ore.isRegionsEnabled())) return true;

                for(String region : regions) {
                    if(ore.getWhitelistedRegions().contains(region)) {
                        return true;
                    }
                    continue;
                }
            }
        }

        return false;
    }

    public StellarOre getOreBreakable(Block block) {
        List<String> regions = new ArrayList<>();

        if(isEnabled()) regions = getRegionsInArea(block);

        Iterator<StellarOre> ores = StellarMiningAPI.getAPI().getOres();

        while(ores.hasNext()) {
            StellarOre ore = ores.next();
            if(ore.getTypeAsMaterial().equals(block.getType())) {
                if(!(isEnabled())) return ore;
                if(!(ore.isRegionsEnabled())) return ore;

                for(String region : regions) {
                    if(ore.getWhitelistedRegions().contains(region)) {
                        return ore;
                    }
                    continue;
                }
            }
        }

        return null;
    }
}
