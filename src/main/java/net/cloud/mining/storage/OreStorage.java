package net.cloud.mining.storage;

import net.cloud.mining.CloudMining;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class OreStorage {

    private CloudMining plugin;

    public OreStorage(CloudMining plugin) {
        this.plugin = plugin;
        ores = new HashMap<>();
    }

    private Map<String, CloudOre> ores;

    public OreStorage addOre(CloudOre ore) {
        ores.put(ore.getName(), ore);
        return this;
    }

    public OreStorage removeOre(CloudOre ore) {
        if(containsOre(ore)) {
            ores.remove(ore.getName());
        }
        return this;
    }

    public OreStorage removeOre(String name) {
        if(containsOre(name)) {
            ores.remove(name);
        }
        return this;
    }

    public boolean containsOre(CloudOre ore) {
        if(ores.containsKey(ore.getName())) {
            return true;
        }
        return false;
    }

    public boolean containsOre(String name) {
        if(ores.containsKey(name)) {
            return true;
        }
        return false;
    }

    public Iterator<String> getOresByName() {
        return ores.keySet().iterator();
    }

    public Iterator<CloudOre> getOres() {
        return ores.values().iterator();
    }

    public Map<String, CloudOre> getMap() {
        return ores;
    }
}
