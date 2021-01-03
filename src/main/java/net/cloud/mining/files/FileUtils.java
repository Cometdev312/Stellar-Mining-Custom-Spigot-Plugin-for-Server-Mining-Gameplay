package net.cloud.mining.files;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import net.cloud.mining.CloudMining;
import net.cloud.mining.files.enums.MiningFileType;
import org.bukkit.configuration.file.FileConfiguration;

public class FileUtils {
	
	Map<MiningFileType, FileConfiguration> configMaps = new HashMap<MiningFileType, FileConfiguration>();
	Map<MiningFileType, File> fileMaps = new HashMap<MiningFileType, File>();

	public void init() {
		for(MiningFileType type : MiningFileType.values()) {
			configMaps.put(type, FileManager.getInstance().getConfig(type.getName()));
			fileMaps.put(type, FileManager.getInstance().getFile(type.getName()));
		}
		reload();
	}

	public void reload() {
		for(MiningFileType type : MiningFileType.values()) {
			if(!((configMaps.containsKey(type)) || (fileMaps.containsKey(type)))) {
				createFileByType(type);
			} else {
				reloadFile(configMaps.get(type), fileMaps.get(type));
			}
		}
	}

	public void destroy() {
		for(MiningFileType type : MiningFileType.values()) {
			if(!((configMaps.containsKey(type)) || (fileMaps.containsKey(type)))) continue;

			configMaps.remove(type);
			fileMaps.remove(type);
		}
	}

	public void reloadFile(FileConfiguration config, File file) { 
		FileManager.getInstance().reloadConfig(config, file);
	}

	public void saveFile(FileConfiguration config, File file) { 
		FileManager.getInstance().saveConfig(config, file);
	}

	public void createFileByType(MiningFileType type) {
		configMaps.put(type, FileManager.getInstance().getConfig(type.getName()));
		fileMaps.put(type, FileManager.getInstance().getFile(type.getName()));
	}

	public FileConfiguration getFileByType(MiningFileType type) {
		if(!((configMaps.containsKey(type)) || (fileMaps.containsKey(type)))) {
			createFileByType(type);
		}

		return configMaps.get(type);
	}
	
	public File getAFileByType(MiningFileType type) {
		if(!((configMaps.containsKey(type)) || (fileMaps.containsKey(type)))) {
			createFileByType(type);
		}

		return fileMaps.get(type);
	}

	public void writeToLogsFile(String name, String line) {
		String fileName = CloudMining.getCore().getDataFolder() + File.separator + "logs" + File.separator + name + ".txt";
		PrintWriter printWriter = null;
		File file = new File(fileName);
		try {
			if(!file.exists()) {
				file.createNewFile();
			}

			printWriter = new PrintWriter(new FileOutputStream(fileName, true));
			printWriter.write(line + System.lineSeparator());
		} catch (IOException ioex) {
			ioex.printStackTrace();
		} finally {
			if (printWriter != null) {
				printWriter.flush();
				printWriter.close();
			}	
		}
	}

	public void saveFileByType(MiningFileType type) {
		if(!(configMaps.containsKey(type) || fileMaps.containsKey(type))) {
			saveFile(FileManager.getInstance().getConfig(type.getName()), FileManager.getInstance().getFile(type.getName()));
		} else {
			saveFile(configMaps.get(type), fileMaps.get(type));
		}
	}

	public static FileUtils instance;
	public static FileUtils getInstance() {
		if(instance == null) instance = new FileUtils();
		return instance;
	}

}
