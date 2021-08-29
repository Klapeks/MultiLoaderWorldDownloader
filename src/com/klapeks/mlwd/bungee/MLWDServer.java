package com.klapeks.mlwd.bungee;

import java.io.File;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.function.Function;

import com.klapeks.coserver.aConfig;
import com.klapeks.coserver.dFunctions;
import com.klapeks.coserver.dRSA;
import com.klapeks.coserver.plugin.bungee.BungeeCoserv;
import com.klapeks.mlwd.api.lFunctions;

public class MLWDServer {
	
//	public static void main(String[] args) {
//		System.out.println(dRSA.generateSecretKey(3));
//	}
//	
//	public static void main3(String[] args) {
//		File file = new File("H://_KlapoMatia//DeadLight//DBD".replace("//", File.separator));
//		
//		String str = ""; int g = (file+"").length()+1;
//		List<File> files = getListOfWorldFiles(file);
//		for (File f : files) {
//			str += ",,,,,,,," + (f+"").substring(g).replace(fs, "/");
//		}
//		if (str.startsWith(",,,,,,,,")) str = str.replaceFirst(",,,,,,,,", "");
//		
//		System.out.println(str.replace(",,,,,,,,", "\n"));
//	}
	
	private static String[] doArgs(String req) {
		String[] ss = req.split(" ");
		for (int i = 0; i < ss.length; i++) {
			try { 
				ss[i] = dRSA.base64_decode(ss[i]);
			} 
			catch (Throwable r) {}
		}
		return ss;
	}
	
	static final String fs = File.separator;
	static final int iqii = 500;
	static HashMap<String, List<String>> world_cashdata = new HashMap<>();
	
	static void __init__() {
		Function<String, String> minihandler = request ->{
			String[] args = doArgs(request);
			switch (args[0]) {
			case "checkfolder": {
				try {
					File file = new File(MainBungee.folder + fs + args[1].replace("/", fs));
					dFunctions.debug("§eChecking folder: " + file);
					if (file.exists()) return "true";
				} catch (Throwable t) {
					t.printStackTrace();
				}
				return "false";
			}
			
			
			case "checkworld": {
				try {
					File file = new File(MainBungee.folder + fs + args[1].replace("/", fs) + fs + args[2]);
					dFunctions.debug("§eChecking world: " + file);
					if (file.exists()) return "true";
				} catch (Throwable t) {
					t.printStackTrace();
				}
				return "false";
			}
			
			case "getworldfilelastmodified": {
				try {
					File file = new File(MainBungee.folder + fs + args[1].replace("/", fs) + fs + args[2] + fs + args[3].replace("/", fs));
					if (file.exists()) return file.lastModified()+"";
				} catch (Throwable t) {
					t.printStackTrace();
				}
				return "-1";
			}
			
			case "getworldfiles":{
				try {
					File file = new File(MainBungee.folder + fs + args[1].replace("/", fs) + fs + args[2]);
					String str = ""; int g = (file+"").length()+1;
					List<File> files = getListOfWorldFiles(file);
					for (File f : files) {
						str += ",,,,,,,," + (f+"").substring(g).replace(fs, "/");
					}
					if (str.startsWith(",,,,,,,,")) str = str.replaceFirst(",,,,,,,,", "");
					return str;
				} catch (Throwable t) {
					t.printStackTrace();
				}
				return "null";
			}
			
			case "startworldfiledownloading": {
				File world = new File(MainBungee.folder + fs + args[1].replace("/", fs) + fs + args[2] + fs + args[3].replace("/", fs));
				dFunctions.debug("§eServer tries download world: " + world);
				if (!world.exists()) return "-1";
				try {
					byte[] bytes = Files.readAllBytes(world.toPath());
//					MainBungee.log("s: " + s);
					dFunctions.debug("§6Encoding world {world}... ".replace("{world}", world.toString()) + bytes.length);
					List<String> cash = new ArrayList<>();
					for (int a = 0; a <= bytes.length; a = a + iqii) {
						byte[] na = Arrays.copyOfRange(bytes, a, (a+iqii) > bytes.length ? bytes.length : (a + iqii));
						cash.add(dRSA.base64_encode_byte(na));
					}
					String randpsw = dRSA.generateSecretKey(3);
					world_cashdata.put(randpsw, cash);
					return cash.size() + " " + randpsw;
				} catch (Throwable t) {
					t.printStackTrace();
					return "-1";
				}
			}
			case "downloadworldfilestage": {
				String secretPsw = args[1];
				if (!world_cashdata.containsKey(secretPsw)) return "null";
				try {
					return world_cashdata.get(secretPsw).get(dFunctions.toInt(args[2]));
				} catch (Throwable t) {
					t.printStackTrace();
				}
				return "null";
			}
			case "mbneedclearcash": {
				String secretPsw = args[1];
				world_cashdata.remove(secretPsw);
				return "ok";
			}
			

			default: {
				lFunctions.log("Unknown request: " + Arrays.toString(args));
				break;
			}
			}
			return "404error";
		};
		if (aConfig.useSecurity) {
			BungeeCoserv.addSecurityHandler("multiloaderworlddownloader", minihandler);
		} else {
			BungeeCoserv.addHandler("multiloaderworlddownloader", minihandler);
		}
	}
	private static List<File> getListOfWorldFiles(File world) {
		List<File> f = new ArrayList<>();
		getListOfWorldFiles(f, world);
		return f;
	}
	private static void getListOfWorldFiles(List<File> f, File world) {
		for (File fl : world.listFiles()) {
			if (fl.isDirectory()) {
				getListOfWorldFiles(f, fl);
				continue;
			}
			f.add(fl);
		}
	}
}
