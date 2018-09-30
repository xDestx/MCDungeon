package me.xDest.mcdungeon.listener;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.plugin.java.JavaPlugin;

import me.xDest.mcdungeon.Messenger;

public class CreeperExplosionListener implements Listener {

	private JavaPlugin plugin;
	private HashMap<Location, Float> creeperdamage = new HashMap<Location, Float>();
	
	public CreeperExplosionListener(JavaPlugin pl) {
		plugin = pl;
	//	Messenger.info("Listener Created for Creeper Explosion");
	}
	
	@EventHandler
	public void onEntityExplosion(EntityExplodeEvent event) {
	//	Messenger.info("Something exploded");
		if (event.getEntity() instanceof Creeper) {
			//Messenger.broadcast("IT NOTICED A CREEPER EXPLODED");
			if (!event.getEntity().hasMetadata("explosionpower")) {
				event.setCancelled(true);
				return;
			}
			float p = event.getEntity().getMetadata("explosionpower").get(0).asFloat();
			//Messenger.broadcast("ITS WORKING SO FAR");
			final Location explloc = event.getLocation();
			//Fireball fb = (Fireball) w.spawnEntity(explloc, EntityType.FIREBALL);
			//fb.setIsIncendiary(false);
			//fb.setYield(p);
			//fb.setDirection(new Vector(0, -5, 0));
			event.setCancelled(true);
			creeperdamage.put(explloc, p);
			Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
				@Override
				public void run() {
					creeperdamage.remove(explloc);
				}
				
			}, 10l);
		}
		if (event.getEntity() instanceof Fireball) {
			Messenger.info("it was a fireball");
			event.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onEntityDamageEvent(EntityDamageEvent event) {
		final EntityDamageEvent e = event;
		Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
			@Override
			public void run() {
				if (e.getEntity() instanceof Player) {
					if (e.getCause() == DamageCause.ENTITY_EXPLOSION) {
						Player p = (Player) e.getEntity();
					//	Messenger.broadcast(p.getName() + " was hurt by an entity explosion");
						Location ploc = p.getLocation();
						try {
							for (Location l : creeperdamage.keySet()) {
								//Messenger.broadcast("entered loop");
								//Messenger.info("Player X " + ploc.getX() + " Xplo X " + l.getX());
								if ((ploc.getX() <= l.getX() - 5 && ploc.getX() >= l.getX() + 5 ) || (ploc.getX() >= l.getX() - 5 && ploc.getX() <= l.getX() + 5 )){
									//Messenger.info("Player Y " + ploc.getY() + " Xplo Y " + l.getY());
									if (ploc.getY() >= l.getY() - 5 && ploc.getY() <= l.getY() + 5) {
										//Messenger.info("Player Z " + ploc.getZ() + " Xplo Z " + l.getZ());
										if ((ploc.getZ() <= l.getZ() - 5 && ploc.getZ() >= l.getZ() + 5 ) || (ploc.getZ() >= l.getZ() - 5 && ploc.getZ() <= l.getZ() + 5 )) {
									//		Messenger.broadcast("Bonus damage cuz super creeper");
											Double d = e.getDamage();
											p.damage(d * creeperdamage.get(l));
										}
									}
								}
							}
						} catch (NullPointerException er) {
							Messenger.severe("No explosions nearby??");
						}
					}
				}
			}
			
			
		}, 5l);
	}
	
	
	/*
	 * 
	   if (event.getEntity() instanceof Creeper) {
			Messenger.broadcast("IT NOTICED A CREEPER EXPLODED");
			if (!event.getEntity().hasMetadata("explosionpower"))
				return;
			float p = event.getEntity().getMetadata("explosionpower").get(0).asFloat();
			Messenger.broadcast("ITS WORKING SO FAR");
			Location explloc = event.getLocation();
			World w = event.getLocation().getWorld();
			Fireball fb = (Fireball) w.spawnEntity(explloc, EntityType.FIREBALL);
			fb.setIsIncendiary(false);
			fb.setYield(p);
			fb.setDirection(new Vector(0, -5, 0));
			event.setCancelled(true);
		}
	 */
	
	
}
