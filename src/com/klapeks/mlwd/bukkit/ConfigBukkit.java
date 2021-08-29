package com.klapeks.mlwd.bukkit;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import com.klapeks.coserver.dFunctions;
import com.klapeks.mlwd.api.lConfig;

public class ConfigBukkit {

	static final String fs = File.separator;

	private static FileWriter fw = null;
	private static FileConfiguration config = null;
	
	public static void __init__() {
		try {
			File file = new File(MainBukkit.bukkit.getDataFolder() + fs + "config.yml");
			
			if (!file.exists()) try { 
				file.getParentFile().mkdirs(); 
				file.createNewFile();
				dFunctions.debug("§6Config was not found; Creating new one");
				fw = new FileWriter(file);
				fw.write("# Config for Bukkit server side" + "\n");
			} catch (Throwable e) { throw new RuntimeException(e); }
			config = YamlConfiguration.loadConfiguration(file);
			
			if (fw==null) fw = open(file);
			

			lConfig.bukkit.limboWorld = g("limboWorld", "Limbo",  "Name of Limbo world", 
					"(It means that if player tries to teleport to the Limbo world,", 
					"he will be automatically teleported to the default world)");
			
			
			lConfig.bukkit.defaultWorld = g("defaultWorld", "folder/world",  "Name of default world", 
					"(It means that player will be spawned at this world when join)");

			fw.flush();
			fw.close();
			config = null; fw = null;
		} catch (Throwable t) {
			t.printStackTrace();
		}
	}


	private static FileWriter open(File file) {
		try {
			BufferedReader br = new BufferedReader(new FileReader(file));
			String e = "";
			String s; while((s=br.readLine())!=null) {
//				dFunctions.debug("§e---" + s);
				e = e + s + "\n";
			}
			br.close();
			FileWriter fw = new FileWriter(file);
//			dFunctions.debug("§a Adding to filewriter;   §b" + e);
			fw.write(e);
			return fw;
		} catch (Throwable t) {
			throw new RuntimeException(t);
		}
	}
	
	@SuppressWarnings("unchecked")
	private static <T> T g(String key, T defaultValue, String... comment) {
		try {
			if (!config.contains(key)) {
				fw.write("\n\n");
				if (comment!=null) 
					for (String s : comment) {
						fw.write("# " + s + "\n");
					}
				if (defaultValue instanceof String) {
					defaultValue = (T) ("\"" + defaultValue + "\"");
				}
				fw.write(key + ": " + defaultValue);
				dFunctions.debug("§3Adding a §6" + defaultValue + "§3 in key §6" + key);
				return defaultValue;
			} else {
				Object o = config.get(key);
				try {
					return (T) o;
				} catch (Throwable t) {
					return defaultValue;
				}
			}
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
	}
}
