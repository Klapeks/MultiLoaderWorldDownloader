package com.klapeks.mlwd.bukkit;

import java.io.File;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import com.klapeks.coserver.IMLPack;
import com.klapeks.coserver.dFunctions;
import com.klapeks.mlwd.api.lFunctions;

public class MLPack implements IMLPack<JavaPlugin> {

	@Override
	public void init(JavaPlugin plugin) {
		if (!Bukkit.getVersion().contains("1.8")) {
			lFunctions.prefix = "§9[§6M§2L§2W§6D§9]§r ";
		}
	}

	@Override
	public void load(JavaPlugin plugin) {
		lFunctions.log("§3MultiLoaderWorldDownloader is loading");
		ConfigBukkit.__init__();
		BukkitWorldList.__init__();
	}

	@Override
	public void enable(JavaPlugin plugin) {
		Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
			@Override
			public void run() {
				lFunctions.log("§aMultiLoaderWorldDownloader is enabling");
				Bukkit.getPluginManager().registerEvents(new BukkitListener(), plugin);
				BukkitWorldList.__init2__();
			}
		});
	}

	@Override
	public void disable(JavaPlugin plugin) {
		lFunctions.log("§cMultiLoaderWorldDownloader is disabling");

	}
	static String dataFolder = "plugins" + File.separator + "MultiLoaderWorldDownloader";
	public static String getDataFolder() {
		return dataFolder;
	}

}
