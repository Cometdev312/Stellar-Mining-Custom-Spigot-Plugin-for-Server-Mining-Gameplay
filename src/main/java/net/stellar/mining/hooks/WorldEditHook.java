package net.stellar.mining.hooks;

import com.sk89q.worldedit.*;
import com.sk89q.worldedit.blocks.BaseBlock;
import com.sk89q.worldedit.bukkit.BukkitUtil;
import com.sk89q.worldedit.bukkit.BukkitWorld;
import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import net.stellar.mining.StellarMining;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;

public class WorldEditHook {

    public StellarMining plugin;
    private boolean enabled;

    public WorldEditHook(StellarMining plugin) {
        this.plugin = plugin;
        enabled = false;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public WorldEditHook setEnabled(boolean bool) {
        this.enabled = bool;
        return this;
    }

    public WorldEditPlugin getWorldEditPlugin() {
        return (WorldEditPlugin) plugin.getServer().getPluginManager().getPlugin("WorldEdit");
    }

    public boolean setBlockFast(Block block, Material material) {
        if(!enabled) {
            block.setType(material);
        } else {

            World world = block.getWorld();
            LocalWorld worldEditWorld = BukkitUtil.toWorldVector(block).getWorld();
            EditSession editSession = WorldEdit.getInstance().getEditSessionFactory().getEditSession(worldEditWorld, 1);

            boolean set = false;
            try {
                set = editSession.setBlock(new Vector(block.getX(), block.getY(), block.getZ()), new BaseBlock(material.getId()));
            } catch (Exception e) {
                e.printStackTrace();
            }
            editSession.flushQueue();
            return set;
        }
        return true;
    }

}
