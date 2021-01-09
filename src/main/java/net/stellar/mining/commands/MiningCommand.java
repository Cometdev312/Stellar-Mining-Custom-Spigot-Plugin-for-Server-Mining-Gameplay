package net.stellar.mining.commands;

import net.stellar.mining.StellarMining;
import net.stellar.mining.files.FileUtils;
import net.stellar.mining.files.enums.MiningFileType;
import net.stellar.mining.support.StellarCommandBuilder;
import org.bukkit.command.CommandSender;

import java.util.Arrays;
import java.util.List;

public class MiningCommand extends StellarCommandBuilder {

    public MiningCommand() {
        super(FileUtils.getInstance().getFileByType(MiningFileType.SETTINGS).getString("Command-Settings.Command", "Stellarmining"));
        setCommandDescription("The Stellar mining main command")
            .setCommandAliases(FileUtils.getInstance().getFileByType(MiningFileType.SETTINGS).getStringList("Command-Settings.Aliases"));
    }

    @Override
    public boolean onCommand(CommandSender sender, String label, String[] args) {
        if(!(sender.hasPermission(FileUtils.getInstance().getFileByType(MiningFileType.SETTINGS).getString("Command-Settings.Command-Permission")))) {
            StellarMining.getCore().getUtils().sendMessage(sender, FileUtils.getInstance().getFileByType(MiningFileType.MESSAGES), "COMMAND-NO-PERMISSION", null);
            return false;
        }
        if(args.length == 0) {
            StellarMining.getCore().getUtils().sendMessage(sender, FileUtils.getInstance().getFileByType(MiningFileType.MESSAGES), "COMMAND-HELP-MESSAGE", null);
        } else if(args.length > 0) {
            if(args[0].equalsIgnoreCase("reload")) {
                FileUtils.getInstance().init();
                StellarMining.getCore().getOreStorage().getOres().forEachRemaining(ore -> ore.loadFromFile());

                StellarMining.getCore().getUtils().sendMessage(sender, FileUtils.getInstance().getFileByType(MiningFileType.MESSAGES), "COMMAND-RELOADED", null);
            } else {
                StellarMining.getCore().getUtils().sendMessage(sender, FileUtils.getInstance().getFileByType(MiningFileType.MESSAGES), "COMMAND-HELP-MESSAGE", null);
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
