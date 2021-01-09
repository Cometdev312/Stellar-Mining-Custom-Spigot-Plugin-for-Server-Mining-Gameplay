package net.stellar.mining.storage;

import net.stellar.mining.StellarMining;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class OreStorage {

    private StellarMining plugin;

    public OreStorage(StellarMining plugin) {
        this.plugin = plugin;
        ores = new HashMap<>();
    }

    private Map<String, StellarOre> ores;

    public OreStorage addOre(StellarOre ore) {
        ores.put(ore.getName(), ore);
        return this;
    }

    public OreStorage removeOre(StellarOre ore) {
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

    public boolean containsOre(StellarOre ore) {
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

    public Iterator<StellarOre> getOres() {
        return ores.values().iterator();
    }

    public Map<String, StellarOre> getMap() {
        return ores;
    }
}
