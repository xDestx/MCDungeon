package me.xDest.mcdungeon.custommob;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Zombie;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;

import me.xDest.mcdungeon.PotionManager;
import me.xDest.mcdungeon.dungeon.DungeonManager;

public class CustomZombie implements CustomMob {

	private Zombie z;
	private World w;
	private int str;
	private int spd;
	private double hp;
	private Location spawn;
	private final int forever = 999999; 
	private final boolean isBoss;
	
	public CustomZombie(World w, boolean isBoss) {
		this.w = w;
		this.isBoss = isBoss;
	}
	
	
	@Override
	public void setMaxHealth(double hp) {
		z.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(hp);
		z.setHealth(hp);
	}
	
	@Override
	public void setSpeed(int speed) {
		z.addPotionEffect(PotionManager.getPotionEffect("SPEED", forever, speed));
	}
	
	public World getWorld() {
		return w;
	}
	
	@Override
	public void spawnInWorld() {
		z = (Zombie)w.spawnEntity(spawn, EntityType.ZOMBIE);
		z.setMetadata("dungeon", new FixedMetadataValue(DungeonManager.getPlugin(), true));
		z.setBaby(false);
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
		this.str = dmg;
		z.addPotionEffect(PotionManager.getPotionEffect("INCREASE_DAMAGE", forever, dmg));
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
		EntityEquipment ee = z.getEquipment();
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
		z.setMetadata("isboss", new FixedMetadataValue(DungeonManager.getPlugin(), true));
	}
	
	@Override
	public void setFireRes() {
		z.addPotionEffect(PotionManager.getPotionEffect("FIRE_RESISTANCE", forever, 1));
	}
	
	@Override
	public void setNormArmor() {
		EntityEquipment ee = z.getEquipment();
		ee.setHelmet(new ItemStack(Material.AIR));
		ee.setChestplate(new ItemStack(Material.AIR));
		ee.setLeggings(new ItemStack(Material.AIR));
		ee.setBoots(new ItemStack(Material.AIR));
		ee.setItemInMainHand(new ItemStack(Material.AIR));
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
