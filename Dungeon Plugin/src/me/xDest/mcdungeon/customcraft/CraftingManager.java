package me.xDest.mcdungeon.customcraft;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.material.MaterialData;

@SuppressWarnings("deprecation")
public class CraftingManager {
	
	final static Material[] helmets = {Material.LEATHER_HELMET, Material.GOLDEN_HELMET, Material.IRON_HELMET, Material.CHAINMAIL_HELMET, Material.DIAMOND_HELMET};
	final static Material[] chests = {Material.LEATHER_CHESTPLATE, Material.GOLDEN_CHESTPLATE, Material.IRON_CHESTPLATE, Material.CHAINMAIL_CHESTPLATE, Material.DIAMOND_CHESTPLATE};
	final static Material[] legs = {Material.LEATHER_LEGGINGS, Material.GOLDEN_LEGGINGS, Material.IRON_LEGGINGS, Material.CHAINMAIL_LEGGINGS, Material.DIAMOND_LEGGINGS};
	final static Material[] boots = {Material.LEATHER_BOOTS, Material.GOLDEN_BOOTS, Material.IRON_BOOTS, Material.CHAINMAIL_BOOTS, Material.DIAMOND_BOOTS};
	final static Material[] swords = {Material.STONE_SWORD, Material.GOLDEN_SWORD, Material.IRON_SWORD, Material.DIAMOND_SWORD, Material.WOODEN_SWORD};
	final static ItemStack lapis = new ItemStack(Material.INK_SAC, 1, (short)4);
	final static MaterialData md = lapis.getData();
	
	private static boolean doneonce = false;
	
	public static void enable() {
		//Creating all recipes
		createArmor();
		createSwords();
		doneonce = true;
	}
	
	private static void createArmor() {
		if (!doneonce) {
			
			
			for (Material helmet : helmets) {
				ItemStack helm1 = new ItemStack(helmet, 1);
				helm1.addUnsafeEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
				ShapedRecipe newhelmet = new ShapedRecipe(helm1);
				newhelmet.shape("***","*%*","***");
				newhelmet.setIngredient('*', md);
				newhelmet.setIngredient('%', helmet);
				Bukkit.getServer().addRecipe(newhelmet);
			}
			for (Material chest : chests) {
				ItemStack chest1 = new ItemStack(chest, 1);
				chest1.addUnsafeEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
				ShapedRecipe newchest = new ShapedRecipe(chest1);
				newchest.shape("***","*%*","***");
				newchest.setIngredient('*', md);
				newchest.setIngredient('%', chest);
				Bukkit.getServer().addRecipe(newchest);
			}
			for (Material leg : legs) {
				ItemStack leg1 = new ItemStack(leg, 1);
				leg1.addUnsafeEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
				ShapedRecipe newleg = new ShapedRecipe(leg1);
				newleg.shape("***","*%*","***");
				newleg.setIngredient('*', md);
				newleg.setIngredient('%', leg);
				Bukkit.getServer().addRecipe(newleg);
			}
			for (Material boot : boots) {
				ItemStack boot1 = new ItemStack(boot, 1);
				boot1.addUnsafeEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
				ShapedRecipe newboot = new ShapedRecipe(boot1);
				newboot.shape("***","*%*","***");
				newboot.setIngredient('*', md);
				newboot.setIngredient('%', boot);
				Bukkit.getServer().addRecipe(newboot);
			}
		}
	}
	
	private static void createSwords() {
		if (!doneonce) {
			for (Material sword : swords) {
				ItemStack sword1 = new ItemStack(sword, 1);
				sword1.addUnsafeEnchantment(Enchantment.DAMAGE_ALL, 1);
				ShapedRecipe newsword = new ShapedRecipe(sword1);
				newsword.shape("***","*%*","***");
				newsword.setIngredient('*', md);
				newsword.setIngredient('%', sword);
				Bukkit.getServer().addRecipe(newsword);
			}
		}
	}
	
	
}
