package com.klapeks.mlwd.bukkit;

import java.io.File;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;

import com.klapeks.mlwd.api.lConfig;

public class BukkitListener implements Listener {

	@EventHandler
	public void cmd(PlayerCommandPreprocessEvent e) {
		String msg = e.getMessage();
		String cmd = msg.split(" ")[0];
		msg = msg.replaceFirst(cmd+" ", "");
		cmd = cmd.replaceFirst("/", "");
		switch (cmd) {
		case "worldtp": {
			e.getPlayer().teleport(Bukkit.getWorld(msg).getSpawnLocation());
			return;
		}
		case "worlds": {
			for (World w : Bukkit.getWorlds()) {
				e.getPlayer().sendMessage(w.getName());
			}
			return;
		}
		default: {
			return;
		}
		}
	}
	
	
	@EventHandler
	public void onJoin(PlayerJoinEvent e) {
		
	}

	@EventHandler
	public void respawn(PlayerRespawnEvent e) {
		Location loc = redo(e.getPlayer(), e.getRespawnLocation().getWorld());
		if (loc!=null) {
			e.setRespawnLocation(loc);
			return;
		}
	}
	
	
	@EventHandler
	public void onTeleport(PlayerTeleportEvent e) {
		Location loc = redo(e.getPlayer(), e.getTo().getWorld());
		if (loc!=null) {
			e.setCancelled(true);
			e.getPlayer().teleport(loc, TeleportCause.PLUGIN);
			return;
		}
	}

	public static Location redo(Player p, World to) {
		if (to.getName().equals(lConfig.bukkit.limboWorld.replace("/", File.separator))) {
			World w = Bukkit.getWorld("worlds_MLWD" + File.separator + lConfig.bukkit.defaultWorld.replace("/", File.separator));
			if (w!=null) {
				return w.getSpawnLocation();
			}
		}
		return null;
	}
}
