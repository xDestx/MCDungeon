package me.xDest.mcdungeon.custommob;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;

import me.xDest.mcdungeon.PotionManager;
import me.xDest.mcdungeon.dungeon.DungeonManager;

public class CustomCreeper implements CustomMob {

	private Creeper c;
	private World w;
	private int str;
	private int spd;
	private double hp;
	private Location spawn;
	private final int forever = 999999; 
	private final boolean isBoss;
	
	public CustomCreeper(World w, boolean isBoss) {
		this.w = w;
		this.isBoss = isBoss;
	}
	
	
	@Override
	public void setMaxHealth(double hp) {
		c.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(hp);
		c.setHealth(hp);
	}
	
	@Override
	public void setSpeed(int speed) {
		c.addPotionEffect(PotionManager.getPotionEffect("SPEED", forever, speed));
	}
	
	public World getWorld() {
		return w;
	}
	
	@Override
	public void spawnInWorld() {
		c = (Creeper)w.spawnEntity(spawn, EntityType.CREEPER);
		c.setMetadata("dungeon", new FixedMetadataValue(DungeonManager.getPlugin(), true));
		setMaxHealth(hp);
		setDamage(str);
		setSpeed(spd);
		if (isBoss)
			setBossArmor();
		else
			setNormArmor();
		setFireRes();
	}

	@Override
	public void setDamage(int dmg) {
		c.setMetadata("explosionpower", new FixedMetadataValue(DungeonManager.getPlugin(), dmg));
		this.str = dmg;
	}

	@Override
	public void setProperties(World w, Location l, double hp, int dmg, int speed) {
		this.w = w;
		this.spawn = l;
		this.hp = hp;
		this.str = dmg;
		this.spd = speed;
	}
	
	@Override
	public void setSpawnLocation(Location l) {
		this.spawn = l;
	}
	

	@Override
	public void setBossArmor() {
		EntityEquipment ee = c.getEquipment();
		ee.setHelmetDropChance(100f);
		ee.setChestplateDropChance(100f);
		ee.setLeggingsDropChance(100f);
		ee.setBootsDropChance(100f);
		ee.setHelmet(new ItemStack(Material.DIAMOND_HELMET));
		ee.setChestplate(new ItemStack(Material.DIAMOND_CHESTPLATE));
		ee.setLeggings(new ItemStack(Material.DIAMOND_LEGGINGS));
		ee.setBoots(new ItemStack(Material.DIAMOND_BOOTS));
		ee.setItemInMainHand(new ItemStack(Material.DIAMOND_SWORD));
		ee.setItemInMainHandDropChance(0f);
		c.setPowered(true);
		c.setMetadata("isboss", new FixedMetadataValue(DungeonManager.getPlugin(), true));
	}
	
	@Override
	public void setNormArmor() {
		EntityEquipment ee = c.getEquipment();
		ee.setHelmet(new ItemStack(Material.AIR));
		ee.setChestplate(new ItemStack(Material.AIR));
		ee.setLeggings(new ItemStack(Material.AIR));
		ee.setBoots(new ItemStack(Material.AIR));
		ee.setItemInMainHand(new ItemStack(Material.AIR));
	}


	@Override
	public void setFireRes() {
		c.addPotionEffect(PotionManager.getPotionEffect("FIRE_RESISTANCE", forever, 1));
	}


	@Override
	public void setDamage(int dmgsp, int dmgsk) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void setProperties(World w, Location l, double hp, int dmgsp,
			int dmgsk, int speed) {
		// TODO Auto-generated method stub
		
	}

}
