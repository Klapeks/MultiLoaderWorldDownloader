package com.klapeks.mlwd.api;

import java.io.File;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldCreator;

import com.klapeks.coserver.dFunctions;
import com.klapeks.mlwd.bukkit.BukkitWorldList;
import com.klapeks.mlwd.bukkit.MainBukkit;

public class lFunctions {
	
	public static String prefix = "[MLPD] ";
	public static void log(Object obj) {
		dFunctions.log_(prefix + obj);
	}
	

//	public static int getRandom(int from, int to) {
//		return getRandom(new Random(), from, to);
//	}
//	public static int getRandom(Random random, int from, int to) {
//        return random.nextInt((to - from) + 1) + from;
//	}
//	
	public static int toInt(Object obj) {
		if (obj==null) return 0;
		if (obj instanceof Integer) {
			return (Integer) obj;
		}
		String ss = obj+"";
		try {return Integer.parseInt(ss);} catch(Throwable e) {};
		ss = _filter_(ss, "-1234567890");
		if (ss.equals("")) return 0;
		try {return Integer.parseInt(ss);} catch(Throwable e) {return 0;}
	}
	
	private static String _filter_(String string, String filter) {
		String integer = "";
		for (String s : string.split("")) {
			if (filter.contains(s)) integer=integer+s;
		}
		return integer;
	}
	public static long toLong(Object obj) {
		if (obj==null) return 0;
		if (obj instanceof Long) {
			return (Long) obj;
		}
		String ss = obj+"";
		try {return Long.parseLong(ss);} catch(Throwable e) {};
		ss = _filter_(ss, "-1234567890");
		if (ss.equals("")) return 0;
		try {return Long.parseLong(ss);} catch(Throwable e) {return 0;}
	}
	
	public static void enableWorld(String path) {
		File folder = new File("worlds_MLWD" + File.separator + path.replace("/", File.separator));
		if (!new File(folder, "level.dat").exists()) {
			log("§clevel.dat file in §6{world}§c wasn't found".replace("{world}", folder+""));
			throw new RuntimeException("level.dat of " + folder + " is not exist");
		}
		try {
			WorldCreator wc = WorldCreator.name(folder+"");
			World world = wc.createWorld();
		} catch (Exception e) {
			Bukkit.getLogger().info("§cWorld §6" + folder + "§c didn't want to load");
			if (BukkitWorldList.DISABLE_BUKKIT_ON_WORLD_ERROR) {
				Bukkit.shutdown();
				return;
			}
			throw new RuntimeException(e);
		}
	}
}
