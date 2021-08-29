package com.klapeks.mlwd.bukkit;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import com.klapeks.mlwd.api.MLWD;
import com.klapeks.mlwd.api.MLWD.WorldFolder;
import com.klapeks.mlwd.api.lFunctions;

public class BukkitWorldList {
	
	public static boolean DISABLE_BUKKIT_ON_WORLD_ERROR = false;
	
	public static boolean isStartup = false;
	public static List<String> needsToBeEnabled = new ArrayList<>();
	
	static final String fs = File.separator;
	
	static void __init__() {
		try {
			isStartup = true;
			File file = new File(MainBukkit.bukkit.getDataFolder() + fs + "list.yml");
			if (!file.exists()) {
				file.getParentFile().mkdirs();
				file.createNewFile();
				FileWriter fw = new FileWriter(file);
				f(fw, "This is a list of worlds that will be");
				f(fw, "automatically updated or downloaded if not exist");
				f(fw);
				f(fw, "Example:");
				f(fw);
				f(fw, "folder1:");
				f(fw, "- world1");
				f(fw, "- world2");
				f(fw, "- world3");
				f(fw, "- world4");
				f(fw, "folder2:");
				f(fw, "- world5");
				f(fw, "- world6");
				f(fw, "folder3: [world7, world8, world9]");
				
				fw.flush();
				fw.close();
			}
			FileConfiguration fc = YamlConfiguration.loadConfiguration(file);
			for (String folder : fc.getKeys(true)) {
				lFunctions.log("---------------" + folder + " - " + fc.isList(folder) + " - " + MLWD.hasFolder(folder));
				if (fc.isList(folder) && MLWD.hasFolder(folder)) {
					List<?> list = fc.getList(folder);
					WorldFolder pf = MLWD.from(folder);
					list.forEach(plugin -> {
						pf.using(plugin+"");
					});
				}
			}
		} catch (Throwable t) {
			t.printStackTrace();
		}
	}
	public static void __init2__() {
		for (String folder$world : needsToBeEnabled) {
			try {
				lFunctions.enableWorld(folder$world.replace(",,,", File.separator));
				MLWD._addEnabled(folder$world.split(",,,")[0], folder$world.split(",,,")[1]);
			} catch (Throwable t) {
				if (DISABLE_BUKKIT_ON_WORLD_ERROR) {
					Bukkit.shutdown();
					return;
				}
				throw new RuntimeException(t);
			}
		}
		isStartup = false;
		needsToBeEnabled.clear();
		needsToBeEnabled = null;
	}
	static void f(FileWriter fw, String comment) {
		try {
			fw.write("# " + comment + "\n");
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}
	static void f(FileWriter fw) {
		f(fw, "");
	}
	
}
