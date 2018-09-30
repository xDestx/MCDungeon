package me.xDest.mcdungeon.dungeon;

import java.util.HashMap;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.inventory.ItemStack;

import me.xDest.mcdungeon.Messenger;
import me.xDest.mcdungeon.custommob.CustomCreeper;
import me.xDest.mcdungeon.custommob.CustomJockey;
import me.xDest.mcdungeon.custommob.CustomMob;
import me.xDest.mcdungeon.custommob.CustomSkeleton;
import me.xDest.mcdungeon.custommob.CustomSpider;
import me.xDest.mcdungeon.custommob.CustomZombie;

public class DungeonCreator {
	

	private static HashMap<Integer, CustomZombie> zombies = new HashMap<Integer, CustomZombie>();// {LEVEL, CUSTOMMOB (INSANCE??)}
	private static HashMap<Integer, CustomCreeper> creepers = new HashMap<Integer, CustomCreeper>();
	private static HashMap<Integer, CustomSkeleton> skeletons = new HashMap<Integer, CustomSkeleton>();
	private static HashMap<Integer, CustomSpider> spiders = new HashMap<Integer, CustomSpider>();
	private static HashMap<Integer, CustomJockey> jockeys = new HashMap<Integer, CustomJockey>();
	
	public static void createNewDungeon(String name, World w, int x1, int y1, int z1, int x2, int y2, int z2, List<Location> spawns, Location chestspawn, List<CustomMob> mobtypes, int spawnsperwave, int waves, ItemStack[] reward, Location playerSpawn, Location bossSpawn, CustomMob boss, Location exit, int wavedelay) {
		Dungeon d = new Dungeon(w, name, x1, y1, z1, x2, y2, z2);
		d.setSpawns(spawns);
		d.setChestSpawn(chestspawn);
		d.setMobTypes(mobtypes);
		d.setSpawnsPerWave(spawnsperwave);
		d.setWaves(waves);
		d.setItemReward(reward);
		d.setPlayerSpawn(playerSpawn);
		d.setBossSpawn(bossSpawn);
		d.setBoss(boss);
		d.setExitSpawn(exit);
		d.setWaveDelay(wavedelay);
		DungeonManager.addDungeon(name, d);
	}
	
	public static void enable(World w) {
		//Zombies
		CustomZombie z1 = new CustomZombie(w, false);
		z1.setProperties(w, null, 30, 0, 2);
		zombies.put(1, z1);
		
		CustomZombie z2 = new CustomZombie(w, false);
		z2.setProperties(w, null, 95, 3, 3);
		zombies.put(2, z2);
		
		CustomZombie z3 = new CustomZombie(w, false);
		z3.setProperties(w, null, 115, 7, 9);
		zombies.put(3, z3);
		
		//Creepers
		CustomCreeper c1 = new CustomCreeper(w, false);
		c1.setProperties(w, null, 40, 5, 0);
		creepers.put(1, c1);
		
		CustomCreeper c2 = new CustomCreeper(w, false);
		c2.setProperties(w, null, 125, 10, 1);
		creepers.put(2, c2);
		
		CustomCreeper c3 = new CustomCreeper(w, false);
		c3.setProperties(w, null, 155, 20, 3);
		creepers.put(3, c3);
		
		//Skeletons
		CustomSkeleton sk1 = new CustomSkeleton(w, false);
		sk1.setProperties(w, null, 50, 13, 0);
		skeletons.put(1, sk1);
		
		CustomSkeleton sk2 = new CustomSkeleton(w, false);
		sk2.setProperties(w, null, 120, 17, 1);
		skeletons.put(2, sk2);
		
		CustomSkeleton sk3 = new CustomSkeleton(w, false);
		sk3.setProperties(w, null, 180, 20, 2);
		skeletons.put(3, sk3);
		
		//Spiders
		CustomSpider sp1 = new CustomSpider(w, false, 40);
		sp1.setProperties(w,  null, 50, 3, 1);
		spiders.put(1, sp1);
		
		CustomSpider sp2 = new CustomSpider(w, false, 60);
		sp2.setProperties(w,  null, 110, 11, 2);
		spiders.put(2, sp2);
		
		CustomSpider sp3 = new CustomSpider(w, false, 80);
		sp3.setProperties(w,  null, 160, 15, 3);
		spiders.put(3, sp3);
		
		//Spider Jockeys
		CustomJockey sj1 = new CustomJockey(w, false, 40);
		sj1.setProperties(w, null, 30, 4, 14, 3);
		jockeys.put(1, sj1);
		
		CustomJockey sj2 = new CustomJockey(w, false, 60);
		sj2.setProperties(w, null, 60, 11, 17, 4);
		jockeys.put(2, sj2);
		
		CustomJockey sj3 = new CustomJockey(w, false, 100);
		sj3.setProperties(w, null, 90, 4, 14, 3);
		jockeys.put(3, sj3);
		
		/*
		 * BOSSES
		 */
		//Zombies
		CustomZombie zb1 = new CustomZombie(w, true);
		zb1.setProperties(w, null, 60, 2, 4);
		zombies.put(-1, zb1);
		
		//Creepers
		CustomCreeper cb1 = new CustomCreeper(w, true);
		cb1.setProperties(w, null, 90, 11, 1);
		creepers.put(-1, cb1);
		
		//Skeletons
		CustomSkeleton skb1 = new CustomSkeleton(w, true);
		skb1.setProperties(w, null, 120, 17, 1);
		skeletons.put(-1, skb1);
		
		//Spiders
		CustomSpider spb1 = new CustomSpider(w, true, 120);
		spb1.setProperties(w, null, 100, 14, 3);
		spiders.put(-1, spb1);
		
		//Jockeys
		CustomJockey sjb1 = new CustomJockey(w, true, 120);
		sjb1.setProperties(w, null, 30, 20, 22, 4);
		jockeys.put(-1, sjb1);
		
	}
	
	public static CustomMob loadCustom(String type, int lvl, World w, Location l) {
		if (type.equals("ZOMBIE")) {
			return zombies.get(lvl);
		} else if (type.equals("CREEPER")) {
			return creepers.get(lvl);
		} else if (type.equals("SKELETON")) {
			return skeletons.get(lvl);
		} else if (type.equals("SPIDER")) {
			return spiders.get(lvl);
		} else if (type.equals("JOCKEY")) {
			return jockeys.get(lvl);	
		} else {
			Messenger.severe("Non-existing CustomMob requested...");
			return null;
		}
	}
	
}
