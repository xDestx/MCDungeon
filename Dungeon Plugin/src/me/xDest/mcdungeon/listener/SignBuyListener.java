package me.xDest.mcdungeon.listener;

import me.xDest.mcdungeon.Messenger;
import me.xDest.mcdungeon.dungeon.Dungeon;
import me.xDest.mcdungeon.dungeon.DungeonManager;
import me.xDest.mcdungeon.party.PartyManager;

import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Sign;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.plugin.java.JavaPlugin;

public class SignBuyListener implements Listener {

	JavaPlugin plugin;
	final int[] numbers = {0,1,2,3,4,5,6,7,8,9};
	final String[] materialprefixes = {"WOOD_","STONE_","IRON_","LEATHER_","GOLD_","DIAMOND_","CHAINMAIL_"};
	final String[] materialshorter = {"W_","S_","I_","L_","G_","D_","C_"};
	
	public SignBuyListener(JavaPlugin pl) {
		plugin = pl;
	}
	
	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent event) {
		if (!event.getAction().toString().equals("RIGHT_CLICK_BLOCK"))
			return;
		Player p = event.getPlayer();
		World w = p.getWorld();
		try {
			if (event.getClickedBlock().getState() instanceof Sign) {
				//signs.containsKey(event.getClickedBlock().getLocation())
				Sign s = (Sign) event.getClickedBlock().getState();
				Location bl = s.getLocation();
				//Messenger.info("MADE IT TO INSIDE");
				if (s.getLine(0).equals("[Trade]")) {// Line 0 [Area] //Line 1 Lapis Cost //Line Item Material z //Line 3 Info -- 
					int cost = 0;
					try {
						cost = Integer.parseInt(s.getLine(1));
					} catch (Exception e) {
						return;
					}
					String itemmaterial = s.getLine(2);
					String temp = "";
					for (String str : materialshorter) {
						if (itemmaterial.substring(0, str.length()).equals(str)) {
							for (String str1 : materialprefixes) {
								if (str1.startsWith(itemmaterial.substring(0, 1))) {
									temp = temp + str1;
									break;
								}
							}
							break;
						}
					}
					if (temp.equals("")) {
						
					} else {
						//Messenger.info("Temp is now " + temp);
						temp = temp + itemmaterial.substring(2, itemmaterial.length());
						itemmaterial = temp;
						//Messenger.info("itemmaterial is now " + itemmaterial);
					}
					String iteminfo = s.getLine(3);
					boolean goodmaterial = false;
					for (Material m : Material.values()) {
						if (m.toString().equals(itemmaterial)) {
							goodmaterial = true;
							break;
						}
					}
					if (!goodmaterial) {
						Messenger.severe(itemmaterial + " is an Invalid Material");
						return;
					}
					if (iteminfo.length() <2) {
						//p.sendMessage(ChatColor.DARK_RED + "Invalid trade");
						PlayerInventory pInv = p.getInventory();
						String type = "";
						if (!pInv.containsAtLeast(new ItemStack(Material.INK_SACK, 1, (short) 4), cost) && (!pInv.containsAtLeast(new ItemStack(Material.GOLD_INGOT), cost * 5))) {
							p.sendMessage(ChatColor.DARK_GREEN + "You don't have enough lapis or gold.");
							return;
						} else {
							if (pInv.containsAtLeast(new ItemStack(Material.INK_SACK, 1, (short) 4), cost)) {
								type = "LAPIS";
							} else {
								type = "GOLD";
							}
						}
						ItemStack item = new ItemStack(Material.getMaterial(itemmaterial), 1);
						if (pInv.firstEmpty() == -1) {
							p.sendMessage(ChatColor.DARK_GREEN + "Your inventory may be full or something else happened, sorry.");
						} else {
							pInv.addItem(item);
							if (type.equals("LAPIS")) {
								pInv.removeItem(new ItemStack(Material.INK_SACK, cost, (short) 4));
							} else {
								pInv.removeItem(new ItemStack(Material.GOLD_INGOT, cost * 5));
							}
							p.sendMessage(ChatColor.GREEN + "Purchased.");
						}
						return;
					}
					PlayerInventory pInv = p.getInventory();
					String type = "";
					if (!pInv.containsAtLeast(new ItemStack(Material.INK_SACK, 1, (short) 4), cost) && (!pInv.containsAtLeast(new ItemStack(Material.GOLD_INGOT), cost * 5))) {
						p.sendMessage(ChatColor.DARK_GREEN + "You don't have enough lapis or gold.");
						return;
					} else {
						if (pInv.containsAtLeast(new ItemStack(Material.INK_SACK, 1, (short) 4), cost)) {
							type = "LAPIS";
						} else {
							type = "GOLD";
						}
					}
					//int amount = Integer.parseInt(iteminfo.substring(1, 2));
					//Messenger.broadcast("AMOUNT: " + amount);
					ItemStack item = new ItemStack(Material.getMaterial(itemmaterial), 1);
					//S = Sharpness - P = protection - U = unbreaking
					int next = 0;
					int repeat = 0;
					for (int i = 0; i < iteminfo.length(); i++) {
						String ench1 = iteminfo.substring(next, next + 1);
						Enchantment enchant = getEnchant(ench1);
						String currentChar = "";
						int x = 0;
						String lvl = "";
						int lvl_int = 0;
						boolean failed = false;
						//System.out.println("On repeat number " + i + ": x is " + x + " == lvl is " + lvl + " == failed is " + failed + " ench1 is " + ench1);
						do {
							x = x + 1;
							boolean end = false;
							boolean broken = false;
							currentChar = iteminfo.substring(next + x, next + x+1);
							//System.out.println("BROKEN: " + broken + " CURRENTCHAR: " + currentChar);
							for (int num : numbers) {
								if (("" + num).equals(currentChar)) {
									broken = true;
									lvl = lvl + num;
									//System.out.println("Added a number " + lvl);
									break;
								}
								if(currentChar.equals(":")) {
									broken = true;
									end = true;
									break;
								}
							}
							if (!broken) {
								failed = true;
								break;
							}
							if (end)
								break;
						} while (currentChar != ":");
						x++;
						//System.out.println("failed? " + failed);
						i = i + x;
						
						next = i - repeat;
						repeat++;
						if (failed)
							return;
						try {
							lvl_int = Integer.parseInt(lvl);
						} catch (Exception e) {
							lvl_int = 1;
						}
						item.addUnsafeEnchantment(enchant, lvl_int);
					}
					if (pInv.firstEmpty() == -1) {
						p.sendMessage(ChatColor.DARK_GREEN + "Your inventory may be full or something else happened, sorry.");
					} else {
						pInv.addItem(item);
						if (type.equals("LAPIS")) {
							pInv.removeItem(new ItemStack(Material.INK_SACK, cost, (short) 4));
						} else {
							pInv.removeItem(new ItemStack(Material.GOLD_INGOT, cost * 5));
						}
						p.sendMessage(ChatColor.GREEN + "Purchased.");
					}
				}
			}
		} catch (NullPointerException e) {
				//e.printStackTrace();
		}
	}
	
	
	private Enchantment getEnchant(String ench) {
		//S = Sharpness - P = protection - U = unbreaking
		if (ench.equals("S")) {
			return Enchantment.DAMAGE_ALL;
		} else if (ench.equals("P")) {
			return Enchantment.PROTECTION_ENVIRONMENTAL;
		} else if (ench.equals("U")) {
			return Enchantment.DURABILITY;
		} else if (ench.equals("K")) {
			return Enchantment.KNOCKBACK;
		} else if (ench.equals("B")) {
			return Enchantment.ARROW_KNOCKBACK;
		} else if (ench.equals("D")) {
			return Enchantment.ARROW_DAMAGE;
		} else if (ench.equals("I")) {
			return Enchantment.ARROW_INFINITE;
		} else if(ench.equals("T")) { 
			return Enchantment.THORNS;
		} else if (ench.equals("W")) {
			return Enchantment.WATER_WORKER;
		} else {
			return Enchantment.DAMAGE_ALL;
		}
	}

	@EventHandler
	public void onSignChange(SignChangeEvent event) {
		
		
	}
	
}
