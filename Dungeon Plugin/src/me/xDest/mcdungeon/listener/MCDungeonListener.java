package me.xDest.mcdungeon.listener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Sign;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import me.xDest.mcdungeon.dungeon.Dungeon;
import me.xDest.mcdungeon.dungeon.DungeonManager;
import me.xDest.mcdungeon.party.Party;
import me.xDest.mcdungeon.party.PartyManager;

public class MCDungeonListener implements Listener {

	JavaPlugin plugin;
	
	public final HashMap<Location, Dungeon> signs = new HashMap<Location, Dungeon>();
	
	public MCDungeonListener(JavaPlugin pl) {
		plugin = pl;
	}
	
	
	@EventHandler
	public void onPlayerLeave(PlayerQuitEvent event) {
		Player player = event.getPlayer();
		Party p = PartyManager.isInParty(player);
		if (!DungeonManager.playerHasDied(player)) {
			if (p != null) {
				p.removeFromParty(player, p.getOwner());
				p.sendPartyCMessage(player.getName() + " Was removed due to logging off.");
			}
		}
		
		
	}
	
	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent event) {
		Player p = event.getPlayer();
		if (p.getInventory().getItemInMainHand().getType().equals(Material.IRON_AXE) && (event.getAction().toString().equals("RIGHT_CLICK_AIR") || event.getAction().toString().equals("RIGHT_CLICK_BLOCK"))) {
			for (Entity e : p.getNearbyEntities(20, 20, 20)) {
				if (!(e instanceof LivingEntity)) {
					
				} else {
					e.getWorld().strikeLightning(e.getLocation());
				}
			}
		}
		try {
			if (event.getClickedBlock().getState() instanceof Sign) {
				//signs.containsKey(event.getClickedBlock().getLocation())
				Sign s = (Sign) event.getClickedBlock().getState();
				Dungeon d = DungeonManager.getDungeon(s.getLine(2));
				//Messenger.info("MADE IT TO INSIDE");
				if (d.canJoin()) {
					if (PartyManager.partyExists(p)) {
						//Messenger.info("MADE IT TO PARTY DOES EXIST AND IS OWNED BY YOU");
						d.partyJoin(PartyManager.getParty(p));
					} else {
						p.sendMessage(ChatColor.DARK_RED + "You have to own a party to join a dungeon.");
					}
				} else {
					p.sendMessage(ChatColor.DARK_RED + "Dungeon in progress");
				}	
			}
		} catch (NullPointerException e) {
				//e.printStackTrace();
		}
	}
	
	
	@EventHandler
	public void onSignChange(SignChangeEvent event) {
		
		if(event.getLine(0).equals("")) {
			if(event.getLine(1).equals("[Dungeon]")) {
				if(event.getLine(3).equals("")) {
					String f = event.getLine(2);
					signs.put(event.getBlock().getLocation(),  DungeonManager.getDungeon(f));
					if (signs.containsKey(event.getBlock().getLocation())) {
						try {
						if (!signs.get(event.getBlock().getLocation()).equals((null))) {
							event.getPlayer().sendMessage("Dungeon Link successful");
						}
						} catch (NullPointerException e) {
							event.getPlayer().sendMessage("Error in Dungeon Link...Dungeon doesn't exist");
						}
					}
				}
			}
		}
		
	}
	
	@EventHandler
	public void onEntityDeath(EntityDeathEvent event) {
		if (!event.getEntity().hasMetadata("isboss"))
			return;
		Location l = event.getEntity().getLocation();
		List<ItemStack> items = new ArrayList<ItemStack>();
		items.add(new ItemStack(Material.DIAMOND_HELMET, 1));
		items.add( new ItemStack(Material.DIAMOND_CHESTPLATE, 1));
		items.add(new ItemStack(Material.DIAMOND_LEGGINGS, 1));
		items.add(new ItemStack(Material.DIAMOND_BOOTS, 1));
	
		for (ItemStack item : items) {
			l.getWorld().dropItemNaturally(l, item);
		}
		
	}
	
	
	
	/*
	    Player p = event.getPlayer();
		if (p.getItemInHand().getType().equals(Material.IRON_AXE) && (event.getAction().toString().equals("RIGHT_CLICK_AIR") || event.getAction().toString().equals("RIGHT_CLICK_BLOCK"))) {
			for (Entity e : p.getNearbyEntities(20, 20, 20)) {
				if (!(e instanceof LivingEntity)) {
					
				} else {
					e.getWorld().strikeLightning(e.getLocation());
				}
			}
		}
	 */
	
}
