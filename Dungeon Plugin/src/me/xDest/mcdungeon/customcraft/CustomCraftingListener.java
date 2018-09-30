package me.xDest.mcdungeon.customcraft;

import java.util.Map;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import me.xDest.mcdungeon.Messenger;

public class CustomCraftingListener implements Listener {

	JavaPlugin plugin;

	final Material[] helmets = {Material.LEATHER_HELMET, Material.GOLDEN_HELMET, Material.IRON_HELMET, Material.CHAINMAIL_HELMET, Material.DIAMOND_HELMET};
	final Material[] chests = {Material.LEATHER_CHESTPLATE, Material.GOLDEN_CHESTPLATE, Material.IRON_CHESTPLATE, Material.CHAINMAIL_CHESTPLATE, Material.DIAMOND_CHESTPLATE};
	final Material[] legs = {Material.LEATHER_LEGGINGS, Material.GOLDEN_LEGGINGS, Material.IRON_LEGGINGS, Material.CHAINMAIL_LEGGINGS, Material.DIAMOND_LEGGINGS};
	final Material[] boots = {Material.LEATHER_BOOTS, Material.GOLDEN_BOOTS, Material.IRON_BOOTS, Material.CHAINMAIL_BOOTS, Material.DIAMOND_BOOTS};
	final Material[] swords = {Material.STONE_SWORD, Material.GOLDEN_SWORD, Material.IRON_SWORD, Material.DIAMOND_SWORD, Material.WOODEN_SWORD};
	
	public CustomCraftingListener(JavaPlugin pl) {
		this.plugin = pl;
	}
	
	@EventHandler
	public void prepareCraftEvent(PrepareItemCraftEvent e) {
		if (e.isRepair()) {
			Messenger.severe("@CustomCraft: Triggered by repair");
			return;
		}
		checkHelm(e);
		checkChest(e);
		checkLegs(e);
		checkBoots(e);
		checkSwords(e);
	}
	
	private void checkHelm(PrepareItemCraftEvent e) {
		ItemStack item = e.getInventory().getItem(5);
		for (Material helmet : helmets) {
			ItemStack result = new ItemStack(helmet, 1);
			if (item == null)
				return;
			if (item.getType() != helmet) {
				
			} else {
				Map<Enchantment, Integer> iench = item.getEnchantments();
				if (iench.isEmpty()) {
					return;
				}
				for (Enchantment en : iench.keySet()) {
					result.addUnsafeEnchantment(en, iench.get(en) + 1);
					//Messenger.broadcast("Added enchantment " + en + " level: " + (iench.get(en) + 1));
				}
				e.getInventory().setResult(result);
			}
		}
		
	}
	
	private void checkChest(PrepareItemCraftEvent e) {
		ItemStack item = e.getInventory().getItem(5);
		for (Material chest : chests) {
			ItemStack result = new ItemStack(chest, 1);
			if (item == null)
				return;
			if (item.getType() != chest) {
				
			} else {
				Map<Enchantment, Integer> iench = item.getEnchantments();
				if (iench.isEmpty()) {
					return;
				}
				for (Enchantment en : iench.keySet()) {
					if (iench.get(en) >= 127) {
						
					} else {
						result.addUnsafeEnchantment(en, iench.get(en) + 1);
					}
					//Messenger.broadcast("Added enchantment " + en + " level: " + (iench.get(en) + 1));
				}
				e.getInventory().setResult(result);
			}
		}
		
	}
	
	private void checkLegs(PrepareItemCraftEvent e) {
		ItemStack item = e.getInventory().getItem(5);
		for (Material leg : legs) {
			ItemStack result = new ItemStack(leg, 1);
			if (item == null)
				return;
			if (item.getType() != leg) {
				
			} else {
				Map<Enchantment, Integer> iench = item.getEnchantments();
				if (iench.isEmpty()) {
					return;
				}
				for (Enchantment en : iench.keySet()) {
					if (iench.get(en) >= 127) {

					} else {
						result.addUnsafeEnchantment(en, iench.get(en) + 1);
					}
					//Messenger.broadcast("Added enchantment " + en + " level: " + (iench.get(en) + 1));
				}
				e.getInventory().setResult(result);
			}
		}
		
	}
	
	private void checkBoots(PrepareItemCraftEvent e) {
		ItemStack item = e.getInventory().getItem(5);
		for (Material boot : boots) {
			ItemStack result = new ItemStack(boot, 1);
			if (item == null)
				return;
			if (item.getType() != boot) {
				
			} else {
				Map<Enchantment, Integer> iench = item.getEnchantments();
				if (iench.isEmpty()) {
					return;
				}
				for (Enchantment en : iench.keySet()) {
					if (iench.get(en) >= 127) {

					} else {
						result.addUnsafeEnchantment(en, iench.get(en) + 1);
					}
					//Messenger.broadcast("Added enchantment " + en + " level: " + (iench.get(en) + 1));
				}
				e.getInventory().setResult(result);
			}
		}
		
	}
	
	private void checkSwords(PrepareItemCraftEvent e) {
		ItemStack item = e.getInventory().getItem(5);
		for (Material sword : swords) {
			ItemStack result = new ItemStack(sword, 1);
			if (item == null)
				return;
			if (item.getType() != sword) {
				
			} else {
				Map<Enchantment, Integer> iench = item.getEnchantments();
				if (iench.isEmpty()) {
					return;
				}
				for (Enchantment en : iench.keySet()) {
					result.addUnsafeEnchantment(en, iench.get(en) + 1);
					//Messenger.broadcast("Added enchantment " + en + " level: " + (iench.get(en) + 1));
				}
				e.getInventory().setResult(result);
			}
		}
		
	}
	
	
}
