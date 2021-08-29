package com.klapeks.mlwd.bukkit;


import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import com.klapeks.mlwd.api.lFunctions;

public class MainBukkit extends JavaPlugin {
	
	static MainBukkit bukkit;
	
	public MainBukkit() {
		bukkit = this;
		if (!Bukkit.getVersion().contains("1.8")) {
			lFunctions.prefix = "§9[§6M§2L§2W§6D§9]§r ";
		}
	}
	
	@Override
	public void onLoad() {
		ConfigBukkit.__init__();
		BukkitWorldList.__init__();
	}
	
	
	@Override
	public void onEnable() {
		Bukkit.getPluginManager().registerEvents(new BukkitListener(), this);
		BukkitWorldList.__init2__();
	}

}
