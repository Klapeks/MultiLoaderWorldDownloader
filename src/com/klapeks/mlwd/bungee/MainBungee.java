package com.klapeks.mlwd.bungee;

import java.io.File;
import java.io.FileWriter;

import com.klapeks.mlwd.api.lFunctions;

import net.md_5.bungee.api.plugin.Plugin;

public class MainBungee extends Plugin {

	public MainBungee() {
		MLWDServer.mlwd_folder = new File("MLWD_worlds"+File.separator + "read me.txt");
		if (!MLWDServer.mlwd_folder.exists()) {
			try {
				MLWDServer.mlwd_folder.getParentFile().mkdirs();
				MLWDServer.mlwd_folder.createNewFile();
				FileWriter fw = new FileWriter(MLWDServer.mlwd_folder);
				fw.write("Create here some folder and put worlds here.\n");
				fw.write("In bukkit/spigot in configuration file specify the path to the world");
//				fw.write("Read more at https://www.spigotmc.org/resources/*addinfuture*");
				fw.flush();
				fw.close();
			} catch (Throwable t) {
				throw new RuntimeException(t);
			}
		}
		MLWDServer.mlwd_folder = new File("MLWD_worlds");
	
	}
	
	@Override
	public void onLoad() {
		lFunctions.log("§3MultiLoaderWorldDownloader is loading");
		MLWDServer.__init__();
	}
	
	
	@Override
	public void onEnable() {
		lFunctions.log("§aMultiLoaderWorldDownloader is enabling");
	}
	
	@Override
	public void onDisable() {
		lFunctions.log("§cMultiLoaderWorldDownloader is disabling");
	}
}