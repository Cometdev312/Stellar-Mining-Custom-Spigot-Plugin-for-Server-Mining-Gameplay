package net.cloud.mining.api;

import net.cloud.mining.CloudMining;
import net.cloud.mining.storage.CloudOre;
import org.bukkit.block.Block;

import java.util.Iterator;
import java.util.Map;

public class CloudMiningAPI {

    public Map<String, CloudOre> getRegisteredOres() {
        return CloudMining.getCore().getOreStorage().getMap();
    }

    public Iterator<String> getOresByName() {
        return CloudMining.getCore().getOreStorage().getOresByName();
    }

    public Iterator<CloudOre> getOres() {
        return CloudMining.getCore().getOreStorage().getOres();
    }

    public boolean isOre(CloudOre ore) {
        return CloudMining.getCore().getOreStorage().containsOre(ore);
    }

    public boolean isOre(String name) {
        return CloudMining.getCore().getOreStorage().containsOre(name);
    }

    public boolean canBreakBlock(Block block) {
        if(CloudMining.getCore().getWorldGuardHook().canBreakBlock(block)) return true;
        return false;
    }

    public CloudOre getOreBreakable(Block block) {
        CloudOre ore = CloudMining.getCore().getWorldGuardHook().getOreBreakable(block);
        return ore;
    }

    private static CloudMiningAPI api;
    public static CloudMiningAPI getAPI() {
        if(api == null) api = new CloudMiningAPI();
        return api;
    }
}
