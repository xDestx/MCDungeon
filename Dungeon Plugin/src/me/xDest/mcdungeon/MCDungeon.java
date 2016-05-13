package me.xDest.mcdungeon;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import me.xDest.mcdungeon.customcraft.CraftingManager;
import me.xDest.mcdungeon.customcraft.CustomCraftingListener;
import me.xDest.mcdungeon.custommob.CustomMob;
import me.xDest.mcdungeon.custommob.CustomSpider;
import me.xDest.mcdungeon.custommob.CustomZombie;
import me.xDest.mcdungeon.dungeon.DungeonCreator;
import me.xDest.mcdungeon.dungeon.DungeonManager;
import me.xDest.mcdungeon.listener.CreeperExplosionListener;
import me.xDest.mcdungeon.listener.DeathBlockListener;
import me.xDest.mcdungeon.listener.EntitySpawnListener;
import me.xDest.mcdungeon.listener.MCDungeonListener;
import me.xDest.mcdungeon.listener.OnDeathInDungeon;
import me.xDest.mcdungeon.listener.PlayerMoveListener;
import me.xDest.mcdungeon.listener.SignAreaListener;
import me.xDest.mcdungeon.listener.SignBuyListener;
import me.xDest.mcdungeon.listener.SkeletonShot;
import me.xDest.mcdungeon.listener.SpiderAttackListener;
import me.xDest.mcdungeon.party.Party;
import me.xDest.mcdungeon.party.PartyManager;
import me.xDest.mcdungeon.villager.CustomVillager;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.Effect;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.Potion;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;
import org.bukkit.util.Vector;

public class MCDungeon extends JavaPlugin {

	World w;
	private final Random random = new Random();
	@Override
	public void onEnable() {
		w = Bukkit.getServer().getWorlds().get(0);
		DungeonManager.enable(this);
		DungeonCreator.enable(w);
		createDungeons();
		startHungerWatch();
		CraftingManager.enable();
		Messenger.info("Crafting Finished");
		getServer().getPluginManager().registerEvents(new MCDungeonListener(this), this);
		getServer().getPluginManager().registerEvents(new OnDeathInDungeon(this), this);
		getServer().getPluginManager().registerEvents(new SignAreaListener(this), this);
		getServer().getPluginManager().registerEvents(new CreeperExplosionListener(this), this);
		getServer().getPluginManager().registerEvents(new SkeletonShot(this), this);
		getServer().getPluginManager().registerEvents(new SpiderAttackListener(this), this);
		getServer().getPluginManager().registerEvents(new SignBuyListener(this), this);
		getServer().getPluginManager().registerEvents(new CustomCraftingListener(this), this);
		getServer().getPluginManager().registerEvents(new EntitySpawnListener(this, DungeonManager.getFields()), this);
		getServer().getPluginManager().registerEvents(new PlayerMoveListener(this), this);
		getServer().getPluginManager().registerEvents(new DeathBlockListener(), this);
		beginBarrierWatch();
	}
	
	@Override
	public void onDisable() {
		DungeonManager.endAllDungeons();
		Messenger.info("All dungeons ended");
	}
	
	private void beginBarrierWatch() {
		Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {
			@Override
			public void run() {
				List<Monster> watch = new ArrayList<Monster>();
				for (Entity e : w.getEntities()) {
					if (e instanceof Monster) {
						watch.add((Monster) e);
					}
				}
				for (Monster m : watch) {
					Location tmp = new Location(m.getWorld(), m.getLocation().getX(), m.getLocation().getY() - 1, m.getLocation().getZ());
					if (tmp.getBlock().getType().equals(Material.BARRIER)) {
						m.setHealth(0);
					}
				}
			}
			
			
		}, 600l, 600l);
	}
	
	public void startHungerWatch() {
		Runnable watch = new Runnable() {
			@Override
			public void run() {
				for (Player p : Bukkit.getOnlinePlayers()) {
					if (p.getFoodLevel() == 6) {
						p.addPotionEffect(PotionManager.getPotionEffect("SLOW", 40, 1));
					} else if (p.getFoodLevel() >= 3 && p.getFoodLevel() < 6) {
						p.addPotionEffect(PotionManager.getPotionEffect("SLOW", 40, 2));
						if (!p.hasPotionEffect(PotionEffectType.CONFUSION))
							p.addPotionEffect(PotionManager.getPotionEffect("CONFUSION", 999999, 3));
						playRandomSound(p);
					} else if (p.getFoodLevel() <= 2) {
						p.addPotionEffect(PotionManager.getPotionEffect("SLOW", 40, 3));
						p.addPotionEffect(PotionManager.getPotionEffect("WEAKNESS", 40, 20));
						p.addPotionEffect(PotionManager.getPotionEffect("WITHER", 40, 5));
						if (!p.hasPotionEffect(PotionEffectType.CONFUSION))
							p.addPotionEffect(PotionManager.getPotionEffect("CONFUSION", 999999, 3));
						playRandomSound(p);
						if(random.nextInt(100) <= 4)
							randomInventory(p);

						int x = (int) (p.getMaxHealth() * .075);
						
						p.setHealth(p.getHealth() - x);
						if (p.getHealth() < 0) {
							p.setHealth(0);
						}
						if (random.nextInt(100) + 1 >= 75) {
							p.addPotionEffect(PotionManager.getPotionEffect("BLINDNESS", 40, 1));
						}
					} else {
						p.removePotionEffect(PotionEffectType.CONFUSION);
					}
				}
			}
			
			
		};
		Bukkit.getScheduler().scheduleSyncRepeatingTask(this, watch, 40l, 20l);
	}
	
	public void randomInventory(Player p) {
		PlayerInventory pinv = p.getInventory();
		final ItemStack[] invc = pinv.getContents();
		//Messenger.severe("CONTENTS: " + invc.length);
		ItemStack[] newinv = new ItemStack[36];
		List<Integer> unused = new ArrayList<Integer>();
		for (int i = 0; i < 36; i++) {
			unused.add(i);
		}
		//pinv.clear();
		int num = 36;
		for (ItemStack item : invc) {
			if (item != null) {
				int x = -1;
				x = random.nextInt(num);//10 //10 //10 //5 = 34
				//Messenger.severe(x + " : " + unused.size() + " : " + num);
				x = unused.remove(x);
				num--;
				newinv[x] = item;
			}
			if (num == -1) {
				break;
			}
		}
		//pinv.setHeldItemSlot(random.nextInt(9));
		pinv.setContents(newinv);
		unused.clear();
	}
	
	public void playRandomSound(Player p) {
		final int[] maxes = {4, 3, 4};
		Random rnd = new Random();
		int[] ranges = new int[3];
		//0 = x, 1 = y, 2 = z
		for (int i = 0; i < 3; i++) {
			int s = rnd.nextInt(2);
			if (s == 0) {
				ranges[i] = (rnd.nextInt(maxes[i]) + 1);
			} else {
				ranges[i] = (rnd.nextInt(maxes[i]) + 1) * -1;
			}
		}
		Location loc = new Location(p.getWorld(), p.getLocation().getX() + ranges[0], p.getLocation().getY() + ranges[1], p.getLocation().getZ() + ranges[2]);
		Sound[] sound = Sound.values();
		double pitch = rnd.nextInt(200) +1;
		pitch = pitch / 100;
		double vol = rnd.nextInt(200) +1;
		vol = vol / 100;
		Sound playsound = sound[rnd.nextInt(sound.length)];//Seperating for debug
		//Messenger.info("Playing sound: " + playsound.toString() + " at " + (vol * 100) + "% volume, and pitch of " + (pitch * 100) + "%");
		p.playSound(loc, playsound, Float.parseFloat("" + vol), Float.parseFloat("" + pitch));
	}
	
	public void createDungeons() {
		//Spawns
		List<Location> spawnlf1 = new ArrayList<Location>();
		List<CustomMob> spawnmf1 = new ArrayList<CustomMob>();
		//Adding custom mobs
		spawnmf1.add(DungeonCreator.loadCustom("ZOMBIE", 1, w, null));
		spawnmf1.add(DungeonCreator.loadCustom("ZOMBIE", 1, w, null));
		spawnmf1.add(DungeonCreator.loadCustom("ZOMBIE", 1, w, null));
		spawnmf1.add(DungeonCreator.loadCustom("ZOMBIE", 1, w, null));
		spawnmf1.add(DungeonCreator.loadCustom("ZOMBIE", 1, w, null));
		//Adding spawn locations
		spawnlf1.add(new Location (w,-50, 65, 274));
		spawnlf1.add(new Location (w,-55, 65, 274));
		//Adding Chest spawn location
		Location chestspawnf1 = new Location(w, -50, 67, 261);
		//Adding player spawn, boss spawn, and exit spawn
		Location psf1 = new Location(w, -48, 64, 299);
		Location bsf1 = new Location(w, -50, 67, 261);
		Location exf1 = new Location(w,-48, 64, 306);
		//Setting rewards
		ItemStack[] f1r = new ItemStack[7];
		f1r[0] = new ItemStack(Material.IRON_SWORD, 1);
		f1r[1] = new ItemStack(Material.GOLDEN_APPLE, 8, (short)1);
		f1r[2] = new ItemStack(Material.IRON_HELMET, 1);
		f1r[3] = new ItemStack(Material.IRON_CHESTPLATE, 1);
		f1r[4] = new ItemStack(Material.IRON_LEGGINGS, 1);
		f1r[5] = new ItemStack(Material.IRON_BOOTS, 1);
		f1r[6] = new ItemStack(Material.INK_SACK, 1, (short)4);
		//Setting Boss
		CustomMob bof1 = DungeonCreator.loadCustom("ZOMBIE", -1, w, null);
		//Bosses have negative tags, normal have positive
		//Creating dungeon
		DungeonCreator.createNewDungeon("f1", w, -75, 60, 304, -22, 73, 252, spawnlf1 , chestspawnf1, spawnmf1, 2, 3, f1r, psf1, bsf1, bof1, exf1, 600);
		Messenger.info("Dungeon f1 has been created");
		/**********************************************************************************************************************************************/
		//Spawns
		List<Location> spawnlf2 = new ArrayList<Location>();
		List<CustomMob> spawnmf2 = new ArrayList<CustomMob>();
		//Adding custom mobs
		spawnmf2.add(DungeonCreator.loadCustom("ZOMBIE", 2, w, null));
		spawnmf2.add(DungeonCreator.loadCustom("CREEPER", 1, w, null));
		spawnmf2.add(DungeonCreator.loadCustom("SKELETON", 1, w, null));
		spawnmf2.add(DungeonCreator.loadCustom("SKELETON", 1, w, null));
		spawnmf2.add(DungeonCreator.loadCustom("SKELETON", 1, w, null));
		//Adding spawn locations
		spawnlf2.add(new Location (w,-48, 77, 276));
		spawnlf2.add(new Location (w,-30, 77, 294));
		spawnlf2.add(new Location (w,-58, 78, 287));
		spawnlf2.add(new Location (w,-33, 77, 260));
		//Adding player spawn, boss spawn, exit spawn, chest spawn
		Location psf2 = new Location(w, -49, 76, 301);
		Location bsf2 = new Location(w, -50, 79, 261);
		Location exf2 = new Location(w, -49, 76, 305);
		Location chestspawnf2 = new Location(w, -50, 79, 261);
		//Setting rewards
		ItemStack[] f2r = new ItemStack[7];
		f2r[0] = new ItemStack(Material.DIAMOND_SWORD, 1);
		f2r[1] = new ItemStack(Material.GOLDEN_APPLE, 64, (short)1);
		f2r[2] = new ItemStack(Material.DIAMOND_HELMET, 1);
		f2r[3] = new ItemStack(Material.DIAMOND_CHESTPLATE, 1);
		f2r[4] = new ItemStack(Material.DIAMOND_LEGGINGS, 1);
		f2r[5] = new ItemStack(Material.DIAMOND_BOOTS, 1);
		f2r[6] = new ItemStack(Material.INK_SACK, 3, (short)4);
		f2r[0].addUnsafeEnchantment(Enchantment.DAMAGE_ALL, 6);
		f2r[2].addUnsafeEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 5);
		f2r[3].addUnsafeEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 5);
		f2r[4].addUnsafeEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 5);
		f2r[5].addUnsafeEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 5);
		//Setting Boss
		CustomMob bof2 = DungeonCreator.loadCustom("SKELETON", -1, w, null);
		//Bosses have negative tags, normal have positive
		//Creating Dungeon
		DungeonCreator.createNewDungeon("f2", w, -75, 72, 304, -22, 85, 252, spawnlf2 , chestspawnf2, spawnmf2, 2, 4, f2r, psf2, bsf2, bof2, exf2, 1200);
		Messenger.info("Dungeon f2 has been created");
		/**********************************************************************************************************************************************/
		//Spawns
		List<Location> spawnlf3 = new ArrayList<Location>();
		List<CustomMob> spawnmf3 = new ArrayList<CustomMob>();
		//Adding custom mobs
		spawnmf3.add(DungeonCreator.loadCustom("SPIDER", 2, w, null));
		spawnmf3.add(DungeonCreator.loadCustom("SPIDER", 2, w, null));
		spawnmf3.add(DungeonCreator.loadCustom("SPIDER", 2, w, null));
		spawnmf3.add(DungeonCreator.loadCustom("SKELETON", 2, w, null));
		spawnmf3.add(DungeonCreator.loadCustom("CREEPER", 2, w, null));
		//Adding spawn locations
		spawnlf3.add(new Location (w,-39, 87, 290));
		spawnlf3.add(new Location (w,-40, 89, 283));
		spawnlf3.add(new Location (w,-52, 89, 274));
		spawnlf3.add(new Location (w,-38, 89, 276));
		//Adding player spawn, boss spawn, exit spawn, chest spawn
		Location psf3 = new Location(w, -48, 92, 300);
		Location bsf3 = new Location(w, -65, 90, 284);
		Location exf3 = new Location(w, -48, 87, 305);
		Location chestspawnf3 = new Location(w, -65, 90, 284);
		//Setting rewards
		ItemStack[] f3r = new ItemStack[8];
		f3r[0] = new ItemStack(Material.DIAMOND_SWORD, 1);
		f3r[1] = new ItemStack(Material.MILK_BUCKET, 6);
		f3r[2] = new ItemStack(Material.DIAMOND_HELMET, 1);
		f3r[3] = new ItemStack(Material.DIAMOND_CHESTPLATE, 1);
		f3r[4] = new ItemStack(Material.DIAMOND_LEGGINGS, 1);
		f3r[5] = new ItemStack(Material.DIAMOND_BOOTS, 1);
		f3r[6] = new ItemStack(Material.POTION, 16, (short) 16421);
		f3r[7] = new ItemStack(Material.LAPIS_BLOCK, 1);
		f3r[0].addUnsafeEnchantment(Enchantment.DAMAGE_ALL, 7);
		f3r[2].addUnsafeEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 5);
		f3r[3].addUnsafeEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 5);
		f3r[4].addUnsafeEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 5);
		f3r[5].addUnsafeEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 5);
		//Setting Boss
		CustomMob bof3 = DungeonCreator.loadCustom("SPIDER", -1, w, null);
		//Bosses have negative tags, normal have positive
		//Creating Dungeon
		DungeonCreator.createNewDungeon("f3", w, -75, 84, 304, -22, 97, 252, spawnlf3 , chestspawnf3, spawnmf3, 2, 7, f3r, psf3, bsf3, bof3, exf3, 2400);
		Messenger.info("Dungeon f3 has been created");
		/**********************************************************************************************************************************************/
		//Spawns
		List<Location> spawnlf4 = new ArrayList<Location>();
		List<CustomMob> spawnmf4 = new ArrayList<CustomMob>();
		//Adding custom mobs
		spawnmf4.add(DungeonCreator.loadCustom("SKELETON", 3, w, null));
		spawnmf4.add(DungeonCreator.loadCustom("SPIDER", 3, w, null));
		spawnmf4.add(DungeonCreator.loadCustom("JOCKEY", 3, w, null));
		spawnmf4.add(DungeonCreator.loadCustom("CREEPER", 3, w, null));
		spawnmf4.add(DungeonCreator.loadCustom("ZOMBIE", 3, w, null));
		//Adding spawn locations
		spawnlf4.add(new Location (w,-28, 112, 299));
		spawnlf4.add(new Location (w,-70, 113, 296));
		spawnlf4.add(new Location (w,-70, 112, 257));
		spawnlf4.add(new Location (w,-28, 112, 257));
		//Adding player spawn, boss spawn, exit spawn, chest spawn
		Location psf4 = new Location(w, -48, 98, 279);
		Location bsf4 = new Location(w, -48, 114, 278);
		Location exf4 = new Location(w, -48, 98, 305);
		Location chestspawnf4 = new Location(w, -70, 113, 299);
		//Setting rewards
		ItemStack[] f4r = new ItemStack[8];
		f4r[0] = new ItemStack(Material.DIAMOND_SWORD, 1);
		f4r[1] = new ItemStack(Material.GOLD_INGOT, 64);
		f4r[2] = new ItemStack(Material.DIAMOND_HELMET, 1);
		f4r[3] = new ItemStack(Material.DIAMOND_CHESTPLATE, 1);
		f4r[4] = new ItemStack(Material.DIAMOND_LEGGINGS, 1);
		f4r[5] = new ItemStack(Material.DIAMOND_BOOTS, 1);
		f4r[6] = new ItemStack(Material.POTION, 32, (short) 16421);
		f4r[7] = new ItemStack(Material.LAPIS_BLOCK, 6);
		f4r[0].addUnsafeEnchantment(Enchantment.DAMAGE_ALL, 14);
		f4r[0].addUnsafeEnchantment(Enchantment.DURABILITY, 15);
		f4r[2].addUnsafeEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 15);
		f4r[2].addUnsafeEnchantment(Enchantment.DURABILITY, 15);
		f4r[3].addUnsafeEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 15);
		f4r[3].addUnsafeEnchantment(Enchantment.DURABILITY, 15);
		f4r[4].addUnsafeEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 15);
		f4r[4].addUnsafeEnchantment(Enchantment.DURABILITY, 15);
		f4r[5].addUnsafeEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 15);
		f4r[5].addUnsafeEnchantment(Enchantment.DURABILITY, 15);
		//Setting Boss
		CustomMob bof4 = DungeonCreator.loadCustom("JOCKEY", -1, w, null);
		//Bosses have negative tags, normal have positive
		//Creating Dungeon
		DungeonCreator.createNewDungeon("f4", w, -75, 97, 304, -22, 133, 252, spawnlf4 , chestspawnf4, spawnmf4, 1, 5, f4r, psf4, bsf4, bof4, exf4, 1200);
		Messenger.info("Dungeon f4 has been created");//F5 Corner 1 16, 132, 443 Corner 2 -37, 180, 253
		/**********************************************************************************************************************************************/
		//Spawns
		List<Location> spawnlf5 = new ArrayList<Location>();
		List<CustomMob> spawnmf5 = new ArrayList<CustomMob>();
		//Adding custom mobs
		spawnmf5.add(DungeonCreator.loadCustom("SKELETON", 3, w, null));
		spawnmf5.add(DungeonCreator.loadCustom("SKELETON", 3, w, null));
		spawnmf5.add(DungeonCreator.loadCustom("SKELETON", 3, w, null));
		spawnmf5.add(DungeonCreator.loadCustom("CREEPER", 3, w, null));
		spawnmf5.add(DungeonCreator.loadCustom("SKELETON", 3, w, null));
		//Adding spawn locations
		spawnlf5.add(new Location (w,-10, 146, 280));
		spawnlf5.add(new Location (w, 7, 148, 313));
		spawnlf5.add(new Location (w, 6, 148, 338));
		spawnlf5.add(new Location (w,-8, 150, 422));
		spawnlf5.add(new Location(w, -33, 150, 422));
		//Adding player spawn, boss spawn, exit spawn, chest spawn
		Location psf5 = new Location(w, -71, 145, 279);
		Location bsf5 = new Location(w, -39, 154, 422);
		Location exf5 = new Location(w, -39, 150, 422);
		Location chestspawnf5 = new Location(w, -34, 151, 422);
		//Setting rewards
		ItemStack[] f5r = new ItemStack[8];
		f5r[0] = new ItemStack(Material.DIAMOND_SWORD, 1);
		f5r[1] = new ItemStack(Material.GOLD_BLOCK, 12);
		f5r[2] = new ItemStack(Material.DIAMOND_HELMET, 1);
		f5r[3] = new ItemStack(Material.DIAMOND_CHESTPLATE, 1);
		f5r[4] = new ItemStack(Material.DIAMOND_LEGGINGS, 1);
		f5r[5] = new ItemStack(Material.DIAMOND_BOOTS, 1);
		f5r[6] = new ItemStack(Material.POTION, 32, (short) 16421);
		f5r[7] = new ItemStack(Material.LAPIS_BLOCK, 9);
		f5r[0].addUnsafeEnchantment(Enchantment.DAMAGE_ALL, 20);
		f5r[0].addUnsafeEnchantment(Enchantment.DURABILITY, 20);
		f5r[2].addUnsafeEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 20);
		f5r[2].addUnsafeEnchantment(Enchantment.DURABILITY, 20);
		f5r[3].addUnsafeEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 20);
		f5r[3].addUnsafeEnchantment(Enchantment.DURABILITY, 20);
		f5r[4].addUnsafeEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 20);
		f5r[4].addUnsafeEnchantment(Enchantment.DURABILITY, 20);
		f5r[5].addUnsafeEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 20);
		f5r[5].addUnsafeEnchantment(Enchantment.DURABILITY, 20);
		f5r[5].addUnsafeEnchantment(Enchantment.DEPTH_STRIDER, 1);
		//Setting Boss
		CustomMob bof5 = DungeonCreator.loadCustom("CREEPER", -1, w, null);
		//Bosses have negative tags, normal have positive
		//Creating Dungeon
		DungeonCreator.createNewDungeon("f5", w, -79, 129, 435, 17, 180, 262, spawnlf5 , chestspawnf5, spawnmf5, 1, 10, f5r, psf5, bsf5, bof5, exf5, 300);
		Messenger.info("Dungeon f5 has been created");//F5 Corner 1 16, 132, 443 Corner 2 -37, 180, 253
				/**********************************************************************************************************************************************/
	}
	
	public void doCoolTeleportThing(Player p) {
		final Player player = p;
		int duration = 40;
		PlayerMoveManager.freeze(player);
		//Bukkit.getScheduler().scheduleSyncDelayedTask(this, new Runnable() {@Override public void run() {PlayerMoveManager.unFreeze(player);}}, duration);
		//player.addPotionEffect(PotionManager.getPotionEffect("SLOW", duration, 20));
		final Location ploc = player.getLocation();
		final int y = (int) ploc.getY();
		//10 strikes on each end
		double xmod = 5;
		double zmod = 5;
		double[] zzmod = {1, .9, .8, .7, .6, .4, .3, .1, .1, .1};
		double[] xxmod = {.1, .1, .1, .3, .4, .6, .7, .8, .9, 1};
		List<Location> strikes = new ArrayList<Location>();
		HashMap<Integer,Location[]> strikeshh = new HashMap<Integer,Location[]>();
		for (int i = 0; i < 10; i++) {
			Location[] ls = {new Location(w,ploc.getX() + xmod, y, ploc.getZ() + zmod), new Location(w,ploc.getX() - xmod, y, ploc.getZ() - zmod), new Location(w,ploc.getX() + zmod, y, ploc.getZ() - xmod),new Location(w,ploc.getX() - zmod, y, ploc.getZ() + xmod),  };
			//System.out.println(ls[0].toString() + "\n" + ls[1].toString() + "\n" + ls[2].toString() + "\n" + ls[3].toString());
			strikeshh.put(i, ls);
			//zmod = zmod - 0.5;
			//xmod = xmod - 0.5;
			
			zmod = zmod - zzmod[i];
			xmod = xmod - xxmod[i];
		}
		final HashMap<Integer,Location[]> strikesh = strikeshh;
		
		Runnable cool = new Runnable() {
			@Override
			public void run() {
				int delay = 0;
				for (Integer key : strikesh.keySet()) {
					final Location[] locations = strikesh.get(key);
					Bukkit.getScheduler().scheduleSyncDelayedTask(DungeonManager.getPlugin(), new Runnable() {
						@Override
						public void run() {
							for (Location l : locations) {
								w.strikeLightningEffect(l);
							}
						}
					}, delay);
					delay = delay + 5;
				}
				Bukkit.getScheduler().scheduleSyncDelayedTask(DungeonManager.getPlugin(),new Runnable() {
					@Override
					public void run() {
						Location l = w.getHighestBlockAt(player.getLocation()).getLocation();
						PlayerMoveManager.unFreeze(player);
						if (l.getY() <= player.getLocation().getY()) {
							player.setVelocity(new Vector(0, 456, 0));
						}
					}
				}, delay - 10);
				Bukkit.getScheduler().scheduleSyncDelayedTask(DungeonManager.getPlugin(), new Runnable() {
					@Override
					public void run() {
						w.strikeLightningEffect(w.getHighestBlockAt(player.getLocation()).getLocation());
						player.teleport(player.getWorld().getSpawnLocation());
						w.strikeLightningEffect(player.getLocation());
						
					}
					
					
				}, delay + 15);
			}
			
		};
		Bukkit.getScheduler().scheduleSyncDelayedTask(this, cool);
	}
	
	
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args){
		if (!(sender instanceof Player))
				return false;
		if(label.equals("spawn")) {
			Player p = (Player) sender;
			Party test = PartyManager.isInParty(p);
			if (test == null) {
				doCoolTeleportThing(p);
				//p.teleport(p.getWorld().getSpawnLocation());
				//p.setGameMode(GameMode.ADVENTURE);
			} else {
				p.sendMessage("You must leave your party to teleport.");
			}
			return true;
		}
		if(label.equals("hunger")) {
			Player p = (Player) sender;
			if (!p.isOp()) {
				return true;
			}
			if (args.length == 0) {
				return false;
			}
			if (args.length == 1) {
				return false;
			}
			Player t = Bukkit.getPlayer(args[0]);
			t.setFoodLevel(Integer.parseInt(args[1]));
			return true;
		}
		if (label.equals("spawnm")) {
			Player p = (Player) sender;
			if (!p.isOp())
				return false;
			if (args.length == 0)
				return false;
			World w = p.getWorld();	
			CustomMob cm;
			cm = DungeonCreator.loadCustom(args[0], Integer.parseInt(args[1]), w, p.getLocation());
			try {
				cm.setSpawnLocation(p.getLocation());
				cm.spawnInWorld();
			} catch (NullPointerException e) {
				p.sendMessage("[MCDungeon] Error, that Custom Mob does not exist or have that specified level. (Please use all caps)");
			}
			return true;
		}
		if(label.equals("party")) {
			Player p = (Player) sender;
			if (args.length == 1) {
				if (args[0].equalsIgnoreCase("disband")) {
					if (DungeonManager.playerIsInDungeon(p)) {
						p.sendMessage(ChatColor.RED + "Cannot disband while in dungeon.");
					} else {
						PartyManager.disband(p);
					}
				} else if (args[0].equalsIgnoreCase("leave")){
					if (DungeonManager.playerIsInDungeon(p)) {
						p.sendMessage(ChatColor.RED + "Cannot leave while in a dungeon");
					} else {
						PartyManager.leaveParty(p);
					}
				}else if (args[0].equals("members")) {
					p.sendMessage("Members: " + PartyManager.getMemAsString(p));
				} else if (args[0].equals("requested")) {
					p.sendMessage("Requested: " + PartyManager.getReqAsString(p, p));
				} else if (args[0].equalsIgnoreCase("create")){
					PartyManager.createNew(p);
				}
			} else {
				if (args[0].equalsIgnoreCase("join")) {
					PartyManager.requestToJoin(p, Bukkit.getPlayer(args[1]));
				} else if (args[0].equalsIgnoreCase("msg") || args[0].equalsIgnoreCase("message")) {
					String msg = "";
					for (int i = 1; i < (args.length); i++) {
						msg = msg + args[i] + " ";
					}
					PartyManager.sendPartyMessage(p, msg);
				} else if (args[0].equalsIgnoreCase("kick")) { 
					PartyManager.kickPlayerFromParty(p, p, Bukkit.getPlayer(args[1]));
				} else if (args[0].equalsIgnoreCase("accept")) {
					PartyManager.acceptPlayerJoin(p, Bukkit.getPlayer(args[1]));
				}
			}
			return true;
		}
		if (label.equals("ct")) {
			Player p = (Player) sender;
			Chunk c = w.getChunkAt(p.getLocation());
			Messenger.broadcast("There are " + c.getEntities().length + " entities in the chunk " + (c.getX() * 16) + " " + (c.getZ() * 16));
			return true;
		}
		if (label.equals("gt")) {
			Player p = (Player) sender;
			p.sendMessage(Bukkit.getScheduler().getPendingTasks().toString());
		}
		
		return false;
	}
	
	
}
