package net.cloud.mining.commands;

import net.cloud.mining.CloudMining;
import net.cloud.mining.files.FileUtils;
import net.cloud.mining.files.enums.MiningFileType;
import net.cloud.mining.storage.OreStorage;
import net.cloud.mining.support.CloudCommandBuilder;
import org.bukkit.command.CommandSender;

import java.util.Arrays;
import java.util.List;

public class MiningCommand extends CloudCommandBuilder {

    public MiningCommand() {
        super(FileUtils.getInstance().getFileByType(MiningFileType.SETTINGS).getString("Command-Settings.Command", "cloudmining"));
        setCommandDescription("The cloud mining main command")
            .setCommandAliases(FileUtils.getInstance().getFileByType(MiningFileType.SETTINGS).getStringList("Command-Settings.Aliases"));
    }

    @Override
    public boolean onCommand(CommandSender sender, String label, String[] args) {
        if(!(sender.hasPermission(FileUtils.getInstance().getFileByType(MiningFileType.SETTINGS).getString("Command-Settings.Command-Permission")))) {
            CloudMining.getCore().getUtils().sendMessage(sender, FileUtils.getInstance().getFileByType(MiningFileType.MESSAGES), "COMMAND-NO-PERMISSION", null);
            return false;
        }
        if(args.length == 0) {
            CloudMining.getCore().getUtils().sendMessage(sender, FileUtils.getInstance().getFileByType(MiningFileType.MESSAGES), "COMMAND-HELP-MESSAGE", null);
        } else if(args.length > 0) {
            if(args[0].equalsIgnoreCase("reload")) {
                FileUtils.getInstance().init();
                CloudMining.getCore().getOreStorage().getOres().forEachRemaining(ore -> ore.loadFromFile());

                CloudMining.getCore().getUtils().sendMessage(sender, FileUtils.getInstance().getFileByType(MiningFileType.MESSAGES), "COMMAND-RELOADED", null);
            } else {
                CloudMining.getCore().getUtils().sendMessage(sender, FileUtils.getInstance().getFileByType(MiningFileType.MESSAGES), "COMMAND-HELP-MESSAGE", null);
            }
        }
        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, String label, String[] args) {
        if(args.length == 1) {
            return Arrays.asList("reload");
        }
        return null;
    }
}
