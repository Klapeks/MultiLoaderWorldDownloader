package com.klapeks.mlwd.bungee;

import java.io.File;
import java.io.FileWriter;

import org.slf4j.Logger;

import com.google.inject.Inject;
import com.klapeks.mlwd.api.lFunctions;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.event.proxy.ProxyShutdownEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.proxy.ProxyServer;

@Plugin(id="mlwd")
public class MainVelocity {

	@Inject
    public MainVelocity(ProxyServer server, Logger logger) {
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

		lFunctions.log("§3MultiLoaderWorldDownloader is loading");
		MLWDServer.__init__();
	}
	@Subscribe
	public void onProxyInitialization(ProxyInitializeEvent event) {
		lFunctions.log("§aMultiLoaderWorldDownloader is enabling");
	}
	@Subscribe
	public void onProxyShutdown(ProxyShutdownEvent event) {
		lFunctions.log("§cMultiLoaderWorldDownloader is disabling");
	}
}
