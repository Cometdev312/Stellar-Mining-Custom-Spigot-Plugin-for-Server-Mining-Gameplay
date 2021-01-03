package net.cloud.mining.support;

import net.cloud.mining.CloudMining;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandMap;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public abstract class CloudCommandBuilder extends BukkitCommand implements Cloneable {

    public CloudCommandBuilder(String name) {
        super(name);
    }

    public CloudCommandBuilder setCommand(String name) {
        setName(name);
        return this;
    }

    public CloudCommandBuilder setCommandDescription(String description) {
        setDescription(description);
        return this;
    }

    public CloudCommandBuilder setCommandAliases(List<String> aliases) {
        setAliases(aliases);
        return this;
    }

    public abstract boolean onCommand(CommandSender sender, String label, String[] args);
    public abstract List<String> onTabComplete(CommandSender sender, String label, String[] args);

    public boolean register() {
        try {
            final Field bukkitCommandMap = Bukkit.getServer().getClass().getDeclaredField("commandMap");
            bukkitCommandMap.setAccessible(true);
            CommandMap commandMap = (CommandMap) bukkitCommandMap.get(Bukkit.getServer());
            commandMap.register(getName(), CloudMining.getCore().getDescription().getName(), this);

            if(!(getAliases().isEmpty() || getAliases() == null)) {
                List<String> commands = new ArrayList<>(getAliases());

                for(String command : commands) {
                    commandMap.register(command, this.getClonedCommand().setCommand(command));
                    continue;
                }
            }
        } catch(Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    public boolean execute(CommandSender sender, String label, String[] args) {
        onCommand(sender, label, args);
        return false;
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String label, String[] args) throws IllegalArgumentException {
        List<String> tab = onTabComplete(sender, label, args);
        if(tab == null) return new ArrayList<>();
        return tab;
    }

    public CloudCommandBuilder getClonedCommand() {
        try {
            CloudCommandBuilder builder = (CloudCommandBuilder) this.clone();
            return (CloudCommandBuilder) this.clone();
        } catch(Exception e) {
            e.printStackTrace();
            CloudMining.getCore().getLogger().info("An error occured while cloning a command. (" + getName() + ")");
        }
        return null;
    }
}
