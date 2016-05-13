package me.xDest.mcdungeon.villager;

import java.util.ArrayList;
import java.util.List;

import me.xDest.mcdungeon.PotionManager;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Villager;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffectType;

import com.gmail.fedmanddev.*;

public class CustomVillager {

	World w;
	Location l;
	List<VillagerTrade> trades = new ArrayList<VillagerTrade>();
	Villager v;
	String customname = ChatColor.DARK_PURPLE + "Custom";
	boolean clear = true;
	
	public CustomVillager(World w, String name) {
		this.w = w;
		l = new Location(w, 0,0,0);
		if (!(name == null))
			this.customname = ChatColor.DARK_PURPLE + name;
	}
	
	public void setProperties(World w, Location l) {
		this.w = w;
		this.l = l;
	}
	
	public void setCustomTrades(List<VillagerTrade> trades) {
		 this.trades = trades;
	}
	
	public void setClearTrades(boolean clear) {
		this.clear = clear;
	}
	
	public void addTrade(ItemStack use1, ItemStack use2, ItemStack reward) {
		trades.add(new VillagerTrade(use1, use2, reward));
	}
	
	public void addTrade(ItemStack use1, ItemStack reward) {
		trades.add(new VillagerTrade(use1, reward));
	}
	
	public void spawnInWorld() {
		applyCustomTrades();
		setCustoms();
		setInvincible();
	}
	
	private void setCustoms() {
		v.setAdult();
		v.setAgeLock(true);
		v.setCanPickupItems(false);
		v.setCustomName(customname);
		v.setCustomNameVisible(true);
		v.addPotionEffect(PotionManager.getPotionEffect("SLOW", PotionManager.FOREVER, 100));
	}
	
	private void setInvincible() {
		v.setRemoveWhenFarAway(false);
		v.addPotionEffect(PotionManager.getPotionEffect("DAMAGE_RESISTANCE", PotionManager.FOREVER, 10));
		v.addPotionEffect(PotionManager.getPotionEffect("FIRE_RESISTANCE", PotionManager.FOREVER, 10));
		v.addPotionEffect(PotionManager.getPotionEffect("WATER_BREATHING", PotionManager.FOREVER, 10));
	}
	
	private void applyCustomTrades() {
		if (clear)
			VillagerTradeApi.clearTrades(v);
		if (trades.isEmpty())
			return;
		for (VillagerTrade vt : trades) {
			VillagerTradeApi.addTrade(v, vt);
		}
	}
	
}
