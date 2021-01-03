package net.cloud.mining.events;

import net.cloud.mining.CloudMining;
import net.cloud.mining.api.CloudMiningAPI;
import net.cloud.mining.api.events.MiningTriggerEvent;
import net.cloud.mining.files.FileUtils;
import net.cloud.mining.files.enums.MiningFileType;
import net.cloud.mining.storage.CloudOre;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

public class CloudMiningListener implements Listener {

    @EventHandler
    public void onBreak(BlockBreakEvent event) {
        Block block = event.getBlock();
        Player player = (Player) event.getPlayer();

        if(CloudMining.getCore().getReplacedStorage().containsBlock(block)) {
            if(event.getPlayer().getGameMode().equals(GameMode.CREATIVE)) {
                if(event.getPlayer().isSneaking() && event.getPlayer().isOp()) {
                    CloudMining.getCore().getUtils().sendMessage(player, FileUtils.getInstance().getFileByType(MiningFileType.MESSAGES), "BLOCK-BROKEN", null);
                } else {
                    event.setCancelled(true);
                    CloudMining.getCore().getUtils().sendMessage(player, FileUtils.getInstance().getFileByType(MiningFileType.MESSAGES), "BREAK-USAGE", null);
                }
            } else {
                event.setCancelled(true);
                CloudMining.getCore().getUtils().sendMessage(player, FileUtils.getInstance().getFileByType(MiningFileType.MESSAGES), "CANNOT-BREAK", null);
            }
            return;
        }

        CloudOre ore = CloudMiningAPI.getAPI().getOreBreakable(block);
        if(ore == null) return;

        if(event.getPlayer().getGameMode().equals(GameMode.CREATIVE)) {
            if(event.getPlayer().isSneaking() && event.getPlayer().isOp()) {
                CloudMining.getCore().getUtils().sendMessage(player, FileUtils.getInstance().getFileByType(MiningFileType.MESSAGES), "BLOCK-BROKEN", null);
            } else {
                event.setCancelled(true);
                CloudMining.getCore().getUtils().sendMessage(player, FileUtils.getInstance().getFileByType(MiningFileType.MESSAGES), "BREAK-USAGE", null);
            }
            return;
        }

        event.setCancelled(true);
        if(ore.isPermissionsEnabled() && player.hasPermission(ore.getRequiredPermission())) {
            MiningTriggerEvent triggerEvent = new MiningTriggerEvent(player, block, ore);
            Bukkit.getPluginManager().callEvent(triggerEvent);

            if(triggerEvent.isCancelled()) return;
            CloudMining.getCore().getUtils().breakBlock(player, block, ore);
        } else {
            CloudMining.getCore().getUtils().sendMessage(player, FileUtils.getInstance().getFileByType(MiningFileType.MESSAGES), "CANNOT-BREAK-PERMISSION", null);
        }
    }
}
