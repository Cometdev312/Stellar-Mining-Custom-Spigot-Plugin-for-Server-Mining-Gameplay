package net.stellar.mining.events;

import net.stellar.mining.StellarMining;
import net.stellar.mining.api.StellarMiningAPI;
import net.stellar.mining.api.events.MiningTriggerEvent;
import net.stellar.mining.files.FileUtils;
import net.stellar.mining.files.enums.MiningFileType;
import net.stellar.mining.storage.StellarOre;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

public class StellarMiningListener implements Listener {

    @EventHandler
    public void onBreak(BlockBreakEvent event) {
        Block block = event.getBlock();
        Player player = (Player) event.getPlayer();

        StellarOre ore = StellarMiningAPI.getAPI().getOreBreakable(block);
        if (ore == null) return;

        if (event.getPlayer().getGameMode().equals(GameMode.CREATIVE)) {
            if (event.getPlayer().isSneaking() && event.getPlayer().isOp()) {
                StellarMining.getCore().getUtils().sendMessage(player, FileUtils.getInstance().getFileByType(MiningFileType.MESSAGES), "BLOCK-BROKEN", null);
            } else {
                event.setCancelled(true);
                StellarMining.getCore().getUtils().sendMessage(player, FileUtils.getInstance().getFileByType(MiningFileType.MESSAGES), "BREAK-USAGE", null);
            }
            return;
        }

        event.setCancelled(true);
        if((ore.isPermissionsEnabled() && player.hasPermission(ore.getRequiredPermission())) || (!(ore.isPermissionsEnabled()))) {
            MiningTriggerEvent triggerEvent = new MiningTriggerEvent(player, block, ore);
            Bukkit.getPluginManager().callEvent(triggerEvent);
            if (triggerEvent.isCancelled()) return;

            if(FileUtils.getInstance().getFileByType(MiningFileType.BLOCKS).getBoolean(ore.getPath() + ".Settings.Give-Player-Ore", false)) {
                block.getDrops().forEach(blockOre -> player.getInventory().addItem(blockOre));
            }

            StellarMining.getCore().getUtils().breakBlock(player, block, ore);
            StellarMining.getCore().getUtils().playParticle(block.getLocation(), FileUtils.getInstance().getFileByType(MiningFileType.BLOCKS), ore.getPath() + "Particle-Settings.Block-Break");
        } else {
            StellarMining.getCore().getUtils().sendMessage(player, FileUtils.getInstance().getFileByType(MiningFileType.MESSAGES), "CANNOT-BREAK-PERMISSION", null);
        }
    }
}
