package net.cloud.mining.storage;

import net.cloud.mining.CloudMining;
import org.bukkit.Location;
import org.bukkit.block.Block;

import java.util.HashMap;
import java.util.Map;

public class ReplacedStorage {

    private CloudMining plugin;

    public ReplacedStorage(CloudMining plugin) {
        this.plugin = plugin;
        storedBlocks = new HashMap<>();
    }

    private Map<Location, CloudOre> storedBlocks;

    public ReplacedStorage addBlock(Block block, CloudOre ore) {
        storedBlocks.put(block.getLocation(), ore);
        return this;
    }

    public ReplacedStorage removeBlock(Block block) {
        if(containsBlock(block)) storedBlocks.remove(block.getLocation());
        return this;
    }

    public ReplacedStorage clear() {
        storedBlocks.keySet().forEach(location -> {
            CloudOre ore = storedBlocks.get(location);
            location.getBlock().setType(ore.getTypeAsMaterial());
        });
        return this;
    }

    public boolean containsBlock(Block block) {
        if(storedBlocks.containsKey(block.getLocation())) {
            return true;
        }
        return false;
    }

}
