package me.xDest.mcdungeon.dungeon;

import java.awt.geom.Area;

import java.util.ArrayList;
import java.util.List;

import me.xDest.mcdungeon.Messenger;
import me.xDest.mcdungeon.custommob.CustomMob;
import me.xDest.mcdungeon.geo.RectangularArea;
import me.xDest.mcdungeon.party.Party;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class Dungeon {

	//private Enum<DungeonState> state;
	
	//private DungeonState state;
	private Location corner1,corner2;
	private final World w;
	private List<Location> spawns;
	private List<CustomMob> mobs;
	private Location chestSpawn;
	private CustomMob boss;
	private int waves;
	private int spawnsperwave;
	private ItemStack[] reward;
	private Location playerSpawn;
	private Location bossSpawn;
	private Location exitSpawn;
	private Runnable wave;
	private final Runnable bossWave;
	private final Runnable endTimer;
	private final Runnable lastWave;
	private final Runnable bossWatch;
	private int wavec;
	private boolean canjoin = true;
	private Party dungeonparty;
	private String dId;
	private RectangularArea field;
	private List<Chunk> dchunks = new ArrayList<Chunk>();
	private int wavedelay = 600;
	private List<Integer> tasks = new ArrayList<Integer>();
	//Spawns per wave may change due to custom waves being designed
	
	/**
	 * The events in order <dungeon join> <dungeon start> <do waves> <boss + 3 more wave> <chest spawn> <dungeon end> <1 minute until eject>
	 **/
	
	
	public Dungeon(World w, String id, int x1, int y1, int z1, int x2, int y2, int z2) {
		this.w = w;
		this.dId = id;
		corner1 = new Location(w, x1, y1, z1);
		corner2 = new Location(w, x2, y2, z2);
		field = new RectangularArea(corner1.toVector(), corner2.toVector());
		//state.setState(DungeonState.JOINING);
		wave = new Runnable() {
			@Override
			public void run() {
				if (canjoin == false) {
					spawnMobs();
				}
			}
		};
		lastWave = new Runnable() {
			@Override
			public void run() {
				if (wavec <= 0 && (!customsInArea()) && canjoin == false) {
					//state.setState(DungeonState.BOSS);
					//Bukkit.getScheduler().cancelTasks(DungeonManager.getPlugin());
					Bukkit.getScheduler().scheduleSyncDelayedTask(DungeonManager.getPlugin(), bossWave,20l);
					Messenger.severe("The last wave has ended");
				} else  if (canjoin == false) {
					Bukkit.getScheduler().scheduleSyncDelayedTask(DungeonManager.getPlugin(), lastWave,20l);
					//Messenger.severe("I don't think the last wave has ended");
				}
			}
		};
		
		bossWave = new Runnable() {
			@Override
			public void run() {
				spawnMobs(true);
				spawnBoss();
				Bukkit.getScheduler().scheduleSyncDelayedTask(DungeonManager.getPlugin(), bossWatch,20l);
				try {
					List<String> members = dungeonparty.getMemberList();
					for (String p : members) {
						//Messenger.broadcast("Teleporting" + p + " to " + playerSpawn.toString());
						Bukkit.getPlayer(p).sendMessage(ChatColor.DARK_PURPLE + "The Boss has spawned!");
					}
				} catch (NullPointerException e) {
					e.printStackTrace();
				}
			}
		};
		
		bossWatch = new Runnable() {
			@Override
			public void run() {
				if (wavec <= 0 && (!customsInArea()) && canjoin == false) {
					//state.setState(DungeonState.END);
					//Bukkit.getScheduler().cancelTasks(DungeonManager.getPlugin());
					Bukkit.getScheduler().scheduleSyncDelayedTask(DungeonManager.getPlugin(), endTimer, 1200l);
					spawnChest();
					try {
						List<String> members = dungeonparty.getMemberList();
						for (String p : members) {
							//Messenger.broadcast("Teleporting" + p + " to " + playerSpawn.toString());
							Bukkit.getPlayer(p).sendMessage(ChatColor.GOLD + "Congrats on the win! You will leave in 1 minute");
						}
					} catch (NullPointerException e) {
						e.printStackTrace();
					}
				} else if (canjoin == false) {
					//Messenger.severe("I don't think the boss fight has ended");
					Bukkit.getScheduler().scheduleSyncDelayedTask(DungeonManager.getPlugin(), bossWatch, 10l);
				}
			}
			
		};
		endTimer = new Runnable() {
			@Override
			public void run() {
				ejectPlayers("See you next time!");
				wavec = waves;
				dungeonparty = null;
				canjoin = true;
				Messenger.broadcast(ChatColor.GREEN + "Dungeon " + dId + " has been reset.");
			}
			
		};
	}
	
	public RectangularArea getField() {
		if (field != null)
			return field;
		return null;
	}
	
	public void dungeonStart() {
		//state.setState(DungeonState.START);
		dchunks = field.getChunks(w);
		long delay = 20l;
		if (wavec == 1)
			wavec = 2;
		for (int i = 0; i < wavec; i++) {
			tasks.add(Bukkit.getScheduler().scheduleSyncDelayedTask(DungeonManager.getPlugin(), wave, delay));
			delay = delay + this.wavedelay;
		}
		Bukkit.getScheduler().scheduleSyncDelayedTask(DungeonManager.getPlugin(), lastWave,20l);	
		
	}
	
	public void setExitSpawn(Location exit) {
		exitSpawn = exit;
	}
	
	public boolean customsInArea() {
		for (Chunk c : dchunks) {
			//chunkcount++;
			if (c.equals(null))
				Messenger.severe("It actually is null");
			Entity[] ent = c.getEntities();
			//Messenger.severe("CHUNK COUNT: " + chunkcount + " - Entities for chunk: " + c.getEntities().length + " at " + (c.getX()) + " " + (c.getZ()) + " also remember that ent is " + ent.length);
			for (Entity e : ent) {
				//entitycount++;
				//Messenger.severe("ENTITY COUNT: " + entitycount);
				if (e instanceof Monster) {
					if (e.hasMetadata("dungeon")) {
						Location l = e.getLocation();
						if (field.isInArea(l)) {
							return true;
						}
					}
				}
			
			}
		}
		
		
		return false;
	}
	
	public void removeAllMonsters() {
		for (Chunk c : dchunks) {
			//chunkcount++;
			if (c.equals(null))
				Messenger.severe("It actually is null");
			Entity[] ent = c.getEntities();
			//Messenger.severe("CHUNK COUNT: " + chunkcount + " - Entities for chunk: " + c.getEntities().length + " at " + (c.getX()) + " " + (c.getZ()) + " also remember that ent is " + ent.length);
			for (Entity e : ent) {
				//entitycount++;
				//Messenger.severe("ENTITY COUNT: " + entitycount);
				if (e instanceof Monster) {
					Location l = e.getLocation();
					if (field.isInArea(l)) {
						//((Monster) e).setHealth(0);
						e.remove();
					}
				}
			
			}
		}
		
		
	}
	
	public void ejectPlayers(String msg) {
		try {
			List<String> members = dungeonparty.getMemberList();
			for (String p : members) {
				//Messenger.broadcast("Teleporting" + p + " to " + playerSpawn.toString());
				Bukkit.getPlayer(p).teleport(exitSpawn);
				Bukkit.getPlayer(p).sendMessage(msg);
			}
		} catch (NullPointerException e) {
			e.printStackTrace();
		}
	}
	
	public void setBossSpawn(Location l) {
		bossSpawn = l;
	}
	
	public void partyJoin(Party playerparty) {
		dungeonparty = playerparty;
		canjoin = false;
		summonPlayers();
	}
	
	
	private void summonPlayers() {
		try {
			List<String> members = dungeonparty.getMemberList();
			for (String p : members) {
				//Messenger.broadcast("Teleporting" + p + " to " + playerSpawn.toString());
				Bukkit.getPlayer(p).teleport(playerSpawn);
				Bukkit.getPlayer(p).sendMessage(ChatColor.RED + "You enter the dungeon");
			}
			dungeonStart();
		} catch (NullPointerException e) {
			e.printStackTrace();
		}
	}
	
	public void setPlayerSpawn(Location playerSpawn) {
		this.playerSpawn = playerSpawn;
	}
	
	public boolean canJoin() {
		return canjoin;
	}
	
	public void setCorners(int x1, int y1, int z1, int x2, int y2, int z2) {
		corner1 = new Location(w, x1, y1, z1);
		corner2 = new Location(w, x2, y2, z2);
		field = new RectangularArea(corner1.toVector(), corner2.toVector());
	}
	
	public void setSpawns(List<Location> l) {
		spawns = l;
	}
	
	public void setWaves(int waves) {
		this.waves = waves;
		wavec = this.waves;
	}
	
	public void setSpawnsPerWave(int spawnsper) {
		this.spawnsperwave = spawnsper;
	}
	
	public void setMobTypes(List<CustomMob> types) {
		mobs = types;
	}
	
	public void setChestSpawn(Location l) {
		chestSpawn = l;
	}
	
	public void setBoss(CustomMob cm) {
		boss = cm;
	}
	
	public void spawnMobs() {
		 if (!(canjoin == false))
			return;
			
		for (int i = 0; i < spawnsperwave; i++) {
			
			for (Location l : spawns) {
				for (CustomMob m : mobs) {
					if (m == null) {
						Messenger.info("Mob is null :LL");
					}
					if (l == null) {
						Messenger.info("location is null :LL");
					}
					m.setSpawnLocation(l);
					m.spawnInWorld();
				}
				w.playSound(l, Sound.ENDERDRAGON_GROWL, 7.0f, 0.5f);
			}
			
		}
		try {
			List<String> members = dungeonparty.getMemberList();
			for (String p : members) {
				//Messenger.broadcast("Teleporting" + p + " to " + playerSpawn.toString());
				Bukkit.getPlayer(p).sendMessage(ChatColor.RED + "MORE!!!");
			}
		} catch (NullPointerException e) {
			e.printStackTrace();
		}
		wavec--;
	}
	
	public void spawnMobs(boolean atboss) {//doesnt really matter...
		 if (!(canjoin == false))
			return;
			
		for (int i = 0; i < spawnsperwave; i++) {
				for (CustomMob m : mobs) {
					m.setSpawnLocation(bossSpawn);
					m.spawnInWorld();
				}
		}
		wavec--;
	}
	
	public void spawnBoss() {
		 if (!(canjoin == false))
				return;
		boss.setSpawnLocation(bossSpawn);
		boss.spawnInWorld();
		try {
			List<String> members = dungeonparty.getMemberList();
			for (String p : members) {
				Bukkit.getPlayer(p).sendMessage(ChatColor.RED + "THE BOSS HAS ARRIVED");
			}
			w.playSound(bossSpawn, Sound.WITHER_SPAWN, 7, 2);
		} catch (NullPointerException e) {
			e.printStackTrace();
		}
	}
	
	public void setItemReward(ItemStack[] reward) {
		this.reward = reward;
	}
	
	public void removeChest() {
		final Block b = chestSpawn.getBlock();
		Location l = new Location(w, chestSpawn.getX() -1 , chestSpawn.getY() - 1, chestSpawn.getZ() -1);
		for (int i = 0; i < 3; i++) {
			for (int x = 0; x < 3; x++) {
				Block block = l.getBlock();
				block.setType(Material.AIR);
				l.setX(l.getX() + 1);
			}
			l.setX(l.getX() - 3);
			l.setZ(l.getZ() + 1);
		}
		b.setType(Material.AIR);
	}
	
	public void spawnChest() {
		final Block b = chestSpawn.getBlock();
		Bukkit.getScheduler().scheduleSyncDelayedTask(DungeonManager.getPlugin(), new Runnable() {
			@Override
			public void run() {
				w.strikeLightningEffect(chestSpawn);
				Location l = new Location(w, chestSpawn.getX() -1, chestSpawn.getY() - 1, chestSpawn.getZ() -1);
				for (int i = 0; i < 3; i++) {
					for (int x = 0; x < 3; x++) {
						Block block = l.getBlock();
						block.setType(Material.GLOWSTONE);
						l.setX(l.getX() + 1);
					}
					l.setX(l.getX() - 3);
					l.setZ(l.getZ() + 1);
				}
				b.setType(Material.CHEST);
				Chest c = (Chest)b.getState();
				Inventory cc = c.getInventory(); //CC = Chest Contents
				cc.clear();
				for (int i = 0; i < dungeonparty.getSize(); i++) {
					try {
					cc.addItem(reward);
					} catch (Exception e) {
						
					}
				}
				Bukkit.getScheduler().scheduleSyncDelayedTask(DungeonManager.getPlugin(), new Runnable() {
					@Override
					public void run() {
						removeChest();
					}
					
				}, 1200l);
			}
			
		}, 10l);
	}

	public boolean containsPlayer(String name) {
		if (dungeonparty == null) {
			return false;
		}
		if (dungeonparty.getMemberList().contains(name))
			return true;
		
		return false;
	}

	public void ejectPlayer(Player player) {
		Messenger.broadcast(ChatColor.DARK_AQUA + player.getName() + " died in a dungeon!");
		
		dungeonparty.removeFromParty(player, dungeonparty.getOwner());
		player.sendMessage(ChatColor.DARK_RED + "You were ejected from the dungeon because you died. Better luck next time!");
		if (dungeonparty.isEmpty())
			forceEnd();
	}
	
	public void forceEnd() {
		canjoin = true;
		//ejectPlayers("You have all died, sorry!");
		wavec = waves;
		//dungeonpart = null;
		removeChest();
		removeAllMonsters();
		if (tasks.size() > 0) {
			for (int x : tasks) {
				Bukkit.getScheduler().cancelTask(x);
			}
			tasks.clear();
		}
		Messenger.broadcast(ChatColor.GOLD + "Dungeon " + dId + " has been reset due to all party members dying.");
	}

	public void setWaveDelay(int wavedelay) {
		this.wavedelay = wavedelay;
	}
	
}
