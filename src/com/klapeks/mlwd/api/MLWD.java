package com.klapeks.mlwd.api;

import java.io.File;
import java.nio.file.Files;

import org.bukkit.Bukkit;
import org.bukkit.World;

import com.klapeks.coserver.Coserver;
import com.klapeks.coserver.aConfig;
import com.klapeks.coserver.dFunctions;
import com.klapeks.funcs.dRSA;
import com.klapeks.mlwd.bukkit.BukkitWorldList;

public class MLWD {
	private static World defaultWorld = null;
	private static World getDefWorld(String world) {
		try {
			if (world.contains("/")) world = world.replace("/", File.separator);
			World w = Bukkit.getWorld(world);
			if (w==null) throw new Exception("World " + world + " wasn't found");
			return w;
		} catch (Throwable t) {
			t.printStackTrace();
		}
		return null;
	}
	public static World getDefault() {
		if (defaultWorld!=null) return defaultWorld;
		defaultWorld = getDefWorld("worlds_MLWD/" + lConfig.bukkit.defaultWorld);
		if (defaultWorld==null) defaultWorld = getDefWorld(lConfig.bukkit.limboWorld);
		if (defaultWorld==null) {
			lFunctions.log("§cMLWD can't find default nor limbo worlds :(");
			if (aConfig.shutdownOnError) {
				lFunctions.log("§cServer will be disabled, to prevent further errors");
				dFunctions.shutdown();
				return null;
			}
			defaultWorld = Bukkit.getWorlds().get(0);
		}
		return defaultWorld;
	}
	
	static final String fs = File.separator;
//	private static WorldFolder nullFolder = new WorldFolder(null) {
//		public WorldFolder download(String world) {
//			lFunctions.log("§cworld §6'{world}'§c can't be downloaded because folder wasn't found!!".replace("{world}", world));
//			return this;
//		};
//		public boolean has(String world) {return false;};
//	};
	public static WorldFolder from(String folder) {
		if (hasFolder(folder)) return new WorldFolder(folder);
		lFunctions.log("§cFolder §6'{folder}'§c wasn't found!".replace("{folder}", folder));
//		return nullFolder;
		return null;
	}
	
	public static boolean hasFolder(String folder) {
		return (send("checkfolder", folder)+"").equals("true");
	}

	static uArrayMap<String, String> worldenabled = new uArrayMap<>();
	public static void _addEnabled(String folder, String world) {
		worldenabled.addIn(folder, world);
	}
	public static boolean _isEnabled(String folder, String world) {
		return worldenabled.containsKey(folder) && worldenabled.get(folder).contains(world);
	}
	public static class WorldFolder {
//		private world getworld(String world) {
//			File file = new File("worlds_MLPD" + fs + folder + fs + world + ".jar");
//			if (file.exists()) {
//				try {
//					world pl = Bukkit.getServer().getworldManager().loadworld(file);
//					pl.onLoad();
//				} catch (Throwable e) {
//					e.printStackTrace();
//				}
//			}
//		}
		
		String folder;
		public WorldFolder(String folder) {
			if (folder==null) return;
			this.folder = folder.replace(fs, "/");
		}

		public WorldFolder using(String world) {
			world = world.replace("/", fs);
			if (_isEnabled(folder, world)) {
				lFunctions.log("§6World '{world}' is already enabled and will not be enabled again".replace("{world}", world));
				return this;
			}
			try {
				if (has(world)) update(world);
			} catch (Throwable t) {
				t.printStackTrace();
			}
			try {//Trying load and enable plugin

				if (!BukkitWorldList.isStartup) {
					lFunctions.enableWorld(folder+fs+world);
					_addEnabled(folder, world);
				} else {
					BukkitWorldList.needsToBeEnabled.add(folder+",,,"+world);
				}
			} catch (Throwable t) {
				t.printStackTrace();
				if (aConfig.shutdownOnError) {
					lFunctions.log("§cServer will be disabled, to prevent further errors");
					dFunctions.shutdown();
				}
			}
			return this;
		}
		
		public boolean localContains(String world) {
			File file = new File("worlds_MLWD" + fs + folder + fs + world);
			return file.exists() && file.isDirectory();
		}
		
		public WorldFolder update(String world) {
			if (!has(world)) {
				lFunctions.log("§cWorld §6'{world}'§c wasn't found!".replace("{world}", world));
				return this;
			}
			lFunctions.log("---World " + world + " was found, starting updating");
			String[] files = get_files(world);
			for (String file : files) {
				update_part(world, file);
			}
//			closeLarge();
			return this;
		}
		protected WorldFolder update_part(String world, String filepath) {
			if (check_part_newversion(world, filepath)) download_part(world, filepath);
			return this;
		}
		public boolean check_part_newversion(String world, String filepath) {
			File file = new File("worlds_MLWD" + fs + folder + fs + world + fs + filepath.replace("/", fs));
			if (!file.exists()) return true;
			return lFunctions.toLong(send("getworldfilelastmodified", folder, world, filepath)) - file.lastModified() >= 1000;
//			return (send("checkpluginnewversion", folder, plugin, (file.exists() ? file.lastModified() : -1)+"")+"").equals("true");
		}
		
		public WorldFolder download(String world) {
			if (!has(world)) {
				lFunctions.log("§cWorld §6'{world}'§c wasn't found!".replace("{world}", world));
				return this;
			}
			String[] files = get_files(world);
			for (String file : files) {
				download_part(world, file);
			}
//			closeLarge();
			return this;
		}
		
		protected WorldFolder download_part(String world, String filepath) {
			lFunctions.log("-- Prepare to download part");
			//Prepare to file downloading
			String secretPsw = send("startworldfiledownloading", folder, world, filepath);
			int size = lFunctions.toInt(secretPsw.split(" ")[0]);
			if (secretPsw.equals("-1") || size==-1) {
//				closeLarge();
				lFunctions.log("§cWorld file §6'{wf}'§c was found but no?".replace("{wf}", folder+"/"+world+"/"+filepath));
				return this;
			}
			secretPsw = secretPsw.replaceFirst(secretPsw.split(" ")[0]+" ", "");
			
			File file = new File("worlds_MLWD" + fs + folder + fs + world + fs + filepath.replace("/", fs));
			try {//Downloading file
				if (file.exists()) file.delete();
				file.getParentFile().mkdirs();
				String g = "";
				int old_proc = 0, new_one = 0;
				
				for (int i = 0; i < size; i++) {
					g = send("downloadworldfilestage", secretPsw, i+"");
					if (g==null || g.equals("null")) {
						lFunctions.log("§cSomething went wrong. On iterator: " + i);
						return this;
					}
					Files.write(file.toPath(), dRSA.base64_decode_byte(g), 
							java.nio.file.StandardOpenOption.CREATE,
							java.nio.file.StandardOpenOption.WRITE,
							java.nio.file.StandardOpenOption.APPEND);
					new_one = i*100 / size;
					if (new_one - old_proc >= 10 || i==0) {
						lFunctions.log("§6Downloading world file '{wf}', iterator {i}... ".replace("{wf}", world+"/"+filepath).replace("{i}", i+"") + new_one);
						old_proc = new_one;
					}
				}
				file.setLastModified(lFunctions.toLong(send("getworldfilelastmodified", folder, world, filepath)));
				lFunctions.log("§6World was downloaded");
			} catch (Throwable t) {
				lFunctions.log("§cError with world downloading");
				file.delete();
				t.printStackTrace();
			}
			
			send("mbneedclearcash", secretPsw);
			return this;
		}

		public boolean has(String world) {
			return (send("checkworld", folder, world)+"").equals("true");
		}
		
		public String[] get_files(String world) {
			String s = send("getworldfiles", folder, world);
			if ((s+"").equals("null")) return null;
			return s.split(",,,,,,,,");
		}
	}
	static Coserver coserver;
	private static String send(String cmd, String... args) {
		if (coserver==null) {
			coserver = Coserver.newCordServer(false);
			coserver.open();
		}
		cmd = dRSA.base64_encode(cmd);
		for (String arg : args) {
			cmd += " " + dRSA.base64_encode(arg);
		}
		return coserver.send(aConfig.useSecurity, "multiloaderworlddownloader " + cmd);
	}
	
}
