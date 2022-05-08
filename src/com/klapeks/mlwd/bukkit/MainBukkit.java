package com.klapeks.mlwd.bukkit;

import java.io.File;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import com.klapeks.mlwd.api.lFunctions;

public class MainBukkit extends JavaPlugin {

	static String dataFolder = "plugins" + File.separator + "MultiLoaderWorldDownloader";
	public MainBukkit() {
		if (!Bukkit.getVersion().contains("1.8")) {
			lFunctions.prefix = "§9[§6M§2L§2W§6D§9]§r ";
		}
	}
	public static String dataFolder() {
		return dataFolder;
	}
	
	@Override
	public void onLoad() {
		lFunctions.log("§3MultiLoaderWorldDownloader is loading");
		ConfigBukkit.__init__();
		BukkitWorldList.__init__();
	}
	
	@Override
	public void onEnable() {
		Bukkit.getScheduler().scheduleSyncDelayedTask(MainBukkit.this, new Runnable() {
			@Override
			public void run() {
				lFunctions.log("§aMultiLoaderWorldDownloader is enabling");
				Bukkit.getPluginManager().registerEvents(new BukkitListener(), MainBukkit.this);
				BukkitWorldList.__init2__();
			}
		});
	}
	
	@Override
	public void onDisable() {
		lFunctions.log("§cMultiLoaderWorldDownloader is disabling");
		lFunctions.onDisable();
	}

}
