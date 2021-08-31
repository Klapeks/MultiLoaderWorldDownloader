package com.klapeks.mlwd.bungee;

import java.io.File;
import java.io.FileWriter;

import com.klapeks.coserver.IMLPack;
import com.klapeks.coserver.dFunctions;
import com.klapeks.mlwd.api.lFunctions;

import net.md_5.bungee.api.plugin.Plugin;

public class MLPack implements IMLPack<Plugin> {

	static File folder = null;
	
	@Override
	public void init(Plugin plugin) {
		folder = new File("MLWD_worlds"+File.separator + "read me.txt");
		if (!folder.exists()) {
			try {
				folder.getParentFile().mkdirs();
				folder.createNewFile();
				FileWriter fw = new FileWriter(folder);
				fw.write("Create here some folder and put worlds here.\n");
				fw.write("In bukkit/spigot in configuration file specify the path to the world");
//				fw.write("Read more at https://www.spigotmc.org/resources/*addinfuture*");
				fw.flush();
				fw.close();
			} catch (Throwable t) {
				throw new RuntimeException(t);
			}
		}
		folder = new File("MLWD_worlds");
		
	}

	@Override
	public void load(Plugin plugin) {
		lFunctions.log("§3MultiLoaderWorldDownloader is loading");
		MLWDServer.__init__();
	}

	@Override
	public void enable(Plugin plugin) {
		lFunctions.log("§aMultiLoaderWorldDownloader is enabling");
	}

	@Override
	public void disable(Plugin plugin) {
		lFunctions.log("§cMultiLoaderWorldDownloader is disabling");
	}

}
