package me.xDest.mcdungeon.custommob;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.attribute.Attribute;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Skeleton;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;

import me.xDest.mcdungeon.PotionManager;
import me.xDest.mcdungeon.dungeon.DungeonManager;

public class CustomSkeleton implements CustomMob {

	private Skeleton s;
	private World w;
	private int str;
	private int spd;
	private double hp;
	private Location spawn;
	private final int forever = 999999; 
	private final boolean isBoss;
	
	public CustomSkeleton(World w, boolean isBoss) {
		this.w = w;
		this.isBoss = isBoss;
	}
	
	
	@Override
	public void setMaxHealth(double hp) {
		s.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(hp);
		s.setHealth(hp);
	}
	
	@Override
	public void setSpeed(int speed) {
		s.addPotionEffect(PotionManager.getPotionEffect("SPEED", forever, speed));
	}
	
	public World getWorld() {
		return w;
	}
	
	@Override
	public void spawnInWorld() {
		s = (Skeleton)w.spawnEntity(spawn, EntityType.SKELETON);
		s.setMetadata("dungeon", new FixedMetadataValue(DungeonManager.getPlugin(), true));
		setMaxHealth(hp);
		setDamage(str);
		setSpeed(spd);
		if (isBoss)
			setBossArmor();
		else
			setNormArmor();
		setFireRes();
		//Messenger.broadcast("I THINK I DID IT " + hp);
	}

	@Override
	public void setDamage(int dmg) {
		try {
			this.str = dmg;
			//EntityEquipment se = s.getEquipment();
			//ItemStack iteminhand = se.getItemInHand();
			s.setMetadata("iscustom", new FixedMetadataValue(DungeonManager.getPlugin(), dmg));
			//w.dropItem(s.getLocation(), iteminhand);
			//Messenger.broadcast("Item in hand is: " + iteminhand.getType());
		} catch (NullPointerException e) {
			
		}
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
		EntityEquipment ee = s.getEquipment();
		ee.setHelmetDropChance(100f);
		ee.setChestplateDropChance(100f);
		ee.setLeggingsDropChance(100f);
		ee.setBootsDropChance(100f);
		ee.setHelmet(new ItemStack(Material.DIAMOND_HELMET));
		ee.setChestplate(new ItemStack(Material.DIAMOND_CHESTPLATE));
		ee.setLeggings(new ItemStack(Material.DIAMOND_LEGGINGS));
		ee.setBoots(new ItemStack(Material.DIAMOND_BOOTS));
		//ee.setItemInHand(new ItemStack(Material.DIAMOND_SWORD));
		ee.getItemInMainHand().addUnsafeEnchantment(Enchantment.ARROW_FIRE, (this.str / 2));
		ee.setItemInMainHandDropChance(0f);
		s.setMetadata("isboss", new FixedMetadataValue(DungeonManager.getPlugin(), true));
	}
	
	@Override
	public void setNormArmor() {
		EntityEquipment ee = s.getEquipment();
		ee.setHelmet(new ItemStack(Material.AIR));
		ee.setChestplate(new ItemStack(Material.AIR));
		ee.setLeggings(new ItemStack(Material.AIR));
		ee.setBoots(new ItemStack(Material.AIR));
		//ee.setItemInHand(new ItemStack(Material.AIR));
	}


	@Override
	public void setFireRes() {
		s.addPotionEffect(PotionManager.getPotionEffect("FIRE_RESISTANCE", forever, 1));
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
