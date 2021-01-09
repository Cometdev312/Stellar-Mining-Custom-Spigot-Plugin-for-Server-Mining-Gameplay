package net.stellar.mining.storage;

import net.stellar.mining.StellarMining;
import org.bukkit.Location;
import org.bukkit.block.Block;

import java.util.HashMap;
import java.util.Map;

public class ReplacedStorage {

    private StellarMining plugin;

    public ReplacedStorage(StellarMining plugin) {
        this.plugin = plugin;
        storedBlocks = new HashMap<>();
    }

    private Map<Location, StellarOre> storedBlocks;

    public ReplacedStorage addBlock(Block block, StellarOre ore) {
        storedBlocks.put(block.getLocation(), ore);
        return this;
    }

    public ReplacedStorage removeBlock(Block block) {
        if(containsBlock(block)) storedBlocks.remove(block.getLocation());
        return this;
    }

    public ReplacedStorage clear() {
        storedBlocks.keySet().forEach(location -> {
            StellarOre ore = storedBlocks.get(location);
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
