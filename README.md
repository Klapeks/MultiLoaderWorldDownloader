# MultiLoaderWorldDownloader
Plugin that downloads/updates worlds from bungeecord

SpigotMC: https://www.spigotmc.org/resources/multiloaderworlddownloader.95819/

Depends: https://www.spigotmc.org/resources/coserverapi.95244/

Please join our discord server https://discord.gg/TWEy37Frh2


### How does it work?
You have a lot of servers (~100). Some of them are hub, bedwars, skywars, etc.
You don't want to manually update the world on all your servers after every change. What to do?
MultiLoaderWorldDownloader is one of solutions (maybe only one).
You probably have a bungeecord. You can put your worlds on a special folder in your bungeecord server and download/update it from bukkit(spigot/paper, etc) while the server is enabling.


### How to use it?

Bungeecord side:
create folder 'MLWD_worlds' and put your worlds here

Bukkit/Spigot side:
in configuration file specify the path to the world

### Configuration be like:

```
# This is a list of worlds that will be
# automatically updated or downloaded if not exist

folder1:
- world1
- world2
- world3
- world4
folder2:
- world5
- world6
folder3: [world7, world8, world9]
```

### Easy API:

```java
//Automatically checks new version of the world's file and in case if old updates it
MLWD.from(folder).using(world);
```
