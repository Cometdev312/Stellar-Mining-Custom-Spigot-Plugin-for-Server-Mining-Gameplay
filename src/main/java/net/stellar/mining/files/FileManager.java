package net.stellar.mining.files;

import java.io.File;
import java.io.IOException;

import net.stellar.mining.StellarMining;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class FileManager {
	
	private static FileManager config;

	public static FileManager getInstance() {
		if (config == null) {
			config = new FileManager();
		}
		return config;
	}

	public boolean createConfig(File configFile, String folder, String file) {
		try {
			if(!(configFile.exists())) {
				StellarMining.getCore().saveResource(folder + "/" + file + ".yml", false);

				return true;
			}

			return false;
		} catch(Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public boolean createConfig(File configFile, String file) {
		try {
			if(!(configFile.exists())) {
				StellarMining.getCore().saveResource(file + ".yml", false);

				return true;
			}

			return false;
		} catch(Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public File getFile(String folder, String file) {
		File configFile = new File(StellarMining.getCore().getDataFolder() + File.separator + folder, file + ".yml");

		if(!(configFile.exists())) {
			StellarMining.getCore().saveResource(folder + "/" + file + ".yml", false);
		}

		return configFile;
	}

	public File getFile(String file) {
		File configFile = new File(StellarMining.getCore().getDataFolder(), file + ".yml");

		if(!(configFile.exists())) {
			StellarMining.getCore().saveResource(file + ".yml", false);
		}

		return configFile;
	}

	public FileConfiguration getConfig(String folder, String file) {
		File configFile = new File(StellarMining.getCore().getDataFolder() + File.separator + folder, file + ".yml");
		FileConfiguration configs = YamlConfiguration.loadConfiguration(configFile);

		return configs;
	}

	public FileConfiguration getConfig(String file) {
		File configFile = new File(StellarMining.getCore().getDataFolder(), file + ".yml");
		FileConfiguration configs = YamlConfiguration.loadConfiguration(configFile);

		return configs;
	}

	public boolean saveConfig(FileConfiguration file, File configFile) {
		try {
			file.save(configFile);
		} catch (IOException e) {
			e.printStackTrace();
		}

		return true;
	}

	public boolean reloadConfig(FileConfiguration file, File configFile) {
		try {
			file.load(configFile);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}


}
