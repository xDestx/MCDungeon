package me.xDest.mcdungeon.dungeon;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import me.xDest.mcdungeon.Messenger;
import me.xDest.mcdungeon.custommob.CustomZombie;
import me.xDest.mcdungeon.geo.RectangularArea;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class DungeonManager {

	private static HashMap<String, Dungeon> dungeons = new HashMap<String, Dungeon>();//{NAME, DUNGEON (INSTANCE??)}
	private static JavaPlugin plugin;
	
	
	public static void load() {
		
	}
	
	public static void enable(JavaPlugin pl) {
		plugin = pl;
	}
	
	public static JavaPlugin getPlugin() {
		return plugin;
	}
	
	public static void addDungeon(String name, Dungeon d) {
		dungeons.put(name, d);
	}
	
	public static void spawnCustom(World w, Location l, String type, double hp) {
		if (type.equals("ZOMBIE")) {
			CustomZombie cz = new CustomZombie(w, false);
			cz.setProperties(w, l, hp, 30, 100);
			cz.spawnInWorld();
		}
	}
	
	public static void spawnInDungeon(String name) {
		if (!dungeons.containsKey(name))
			return;
		dungeons.get(name).spawnMobs();
		dungeons.get(name).spawnChest();
	}
	
	public static Dungeon getDungeon(String key) {
		if (dungeons.containsKey(key))
			return dungeons.get(key);
		return null;
	}
	
	public static boolean playerHasDied(Player player) {
		for (String s : dungeons.keySet()) {
			if (dungeons.get(s).canJoin() == false) {
				Messenger.info("Found that a dungeon in progress");
				if (dungeons.get(s).containsPlayer(player.getName())) {
					Messenger.info("Found that a player has died and is in a dungeon, " + player.getName());
					dungeons.get(s).ejectPlayer(player);
					Messenger.info("Found that a player has died and is in a dungeon, ejected him " + player.getName());
					return true;
				}
			}
		}
		return false;
	}
	
	public static List<RectangularArea> getFields() {
		List<RectangularArea> fields = new ArrayList<RectangularArea>();
		for (String d : dungeons.keySet()) {
			fields.add(dungeons.get(d).getField());
		}
		return fields;
	}
	
	public static void endAllDungeons() {
		for (String s : dungeons.keySet()) {
			dungeons.get(s).forceEnd();
		}
	}
	
	public static boolean playerIsInDungeon(Player p) {
		for (String s : dungeons.keySet()) {
			if (dungeons.get(s).canJoin() == false) {
				Messenger.info("Found that a dungeon in progress");
				if (dungeons.get(s).containsPlayer(p.getName())) {
					Messenger.info("Found that a player is in a dungeon, " + p.getName());
					return true;
				}
			}
		}
		return false;
	}
	
	
	
}
