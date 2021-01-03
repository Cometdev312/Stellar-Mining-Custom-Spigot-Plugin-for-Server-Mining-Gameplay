package net.cloud.mining.api.events;

import net.cloud.mining.storage.CloudOre;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class MiningTriggerEvent extends Event implements Cancellable {

    private HandlerList handlers = new HandlerList();

    private Player player;
    private Block block;
    private CloudOre ore;

    private boolean cancelled;

    public MiningTriggerEvent(Player player, Block block, CloudOre ore) {
        this.player = player;
        this.block = block;
        this.ore = ore;
    }

    public Player getPlayer() {
        return player;
    }

    public Block getBlock() {
        return block;
    }

    public CloudOre getOre() {
        return ore;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        cancelled = cancel;
    }
}
