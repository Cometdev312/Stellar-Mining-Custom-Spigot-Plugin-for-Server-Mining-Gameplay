package net.stellar.mining.utils;

import net.stellar.mining.StellarMining;
import net.stellar.mining.StellarMining;
import net.stellar.mining.files.FileUtils;
import net.stellar.mining.files.enums.MiningFileType;
import net.stellar.mining.storage.StellarOre;
import net.stellar.mining.storage.StellarReward;
import net.stellar.mining.support.XSound;
import net.stellar.mining.support.particles.ParticleEffect;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class Utils {

    public String getColor(String args) {
        return ChatColor.translateAlternateColorCodes('&', args);
    }
    public static boolean playSound(Player player, String sound) {
        try {
            XSound.playSoundFromString(player, sound);
            return true;
        } catch(Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    public void sendMessage(CommandSender sender, FileConfiguration config, String path, StellarPlaceholder placeholders) {
        if(config.getBoolean(path + ".Sound.Enabled", false) && (sender instanceof Player)) {
            playSound((Player) sender, config.getString(path + ".Sound.Value", "ENTITY_BAT_TAKEOFF"));
        }

        if(config.getBoolean(path + ".Message.Enabled", false)) {
            List<String> messages = config.getStringList(path + ".Message.Value");
            for(String message : messages) {
                if(placeholders == null) {
                    sender.sendMessage(StellarMining.getCore().getUtils().getColor(message));
                } else {
                    sender.sendMessage(StellarMining.getCore().getUtils().getColor(placeholders.parse(message)));
                }
            }
        }
    }
    public void broadcastMessage(FileConfiguration config, String path, StellarPlaceholder placeholders) {
        if(config.getBoolean(path + ".Sound.Enabled", false)) {
            Bukkit.getOnlinePlayers().forEach(player -> playSound(player, config.getString(path + ".Sound.Value", "ENTITY_BAT_TAKEOFF")));
        }

        if(config.getBoolean(path + ".Message.Enabled", false)) {
            List<String> messages = config.getStringList(path + ".Message.Value");
            for(String message : messages) {
                if(placeholders == null) {
                    Bukkit.broadcastMessage(StellarMining.getCore().getUtils().getColor(message));
                } else {
                    Bukkit.broadcastMessage(StellarMining.getCore().getUtils().getColor(placeholders.parse(message)));
                }
            }
        }
    }

    public void breakBlock(Player player, Block block, StellarOre ore) {
        StellarMining.getCore().getReplacedStorage().addBlock(block, ore); //Add Block To Storage
        StellarMining.getCore().getServer().getScheduler().scheduleSyncDelayedTask(StellarMining.getCore(), new Runnable() { //Change Block Back
            @Override
            public void run() {
                block.setType(ore.getTypeAsMaterial());
                StellarMining.getCore().getReplacedStorage().removeBlock(block);
            }
        }, ore.getDelay());
        block.setType(Material.BEDROCK); //Set Block to Bedrock

        StellarReward reward = ore.getRandomReward();
        if(reward == null) return;

        List<String> commands = FileUtils.getInstance().getFileByType(MiningFileType.BLOCKS).getStringList(reward.getPath() + "Commands");
        for(String command : commands) {
            Bukkit.dispatchCommand(StellarMining.getCore().getConsole(), command.replace("%player%", player.getName()));
        }

        sendMessage(player, FileUtils.getInstance().getFileByType(MiningFileType.BLOCKS), reward.getPath() + "Message-Settings.Player-Message", new StellarPlaceholder().addPlaceholder("%player%", player.getName()));
        broadcastMessage(FileUtils.getInstance().getFileByType(MiningFileType.BLOCKS), reward.getPath() + "Message-Settings.Broadcast-Message", new StellarPlaceholder().addPlaceholder("%player%", player.getName()));
    }

    public void playParticle(Location location, FileConfiguration config, String path) {
        if(config.getBoolean(path + ".Enabled", false)) {
            String[] particles = config.getString(path + ".Value").split(";");
            if(particles.length > 5) {
                if(isParticle(particles[0]) && isInteger(particles[1]) && isInteger(particles[2]) && isInteger(particles[3]) && isInteger(particles[4]) && isInteger(particles[5])) {
                    ParticleEffect.fromName(particles[0]).display(Integer.valueOf(particles[1]), Integer.valueOf(particles[2]), Integer.valueOf(particles[3]), Integer.valueOf(particles[4]), Integer.valueOf(particles[5]), location, new ArrayList<Player>(Bukkit.getOnlinePlayers()));
                }
            }
        }
    }

    public boolean isParticle(String particle) {
        try {
            if(ParticleEffect.fromName(particle) == null) return false;
            return true;
        } catch(Exception e) {
            return false;
        }
    }

    public boolean isInteger(String arguments) {
        try {
            Integer.parseInt(arguments);
            return true;
        } catch(Exception e) {
            return false;
        }
    }

    public boolean isFloat(String arguments) {
        try {
            Float.parseFloat(arguments);
            return true;
        } catch(Exception e) {
            return false;
        }
    }
}
