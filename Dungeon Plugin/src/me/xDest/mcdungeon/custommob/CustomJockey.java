package me.xDest.mcdungeon.custommob;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;

import me.xDest.mcdungeon.Messenger;
import me.xDest.mcdungeon.PotionManager;
import me.xDest.mcdungeon.dungeon.DungeonManager;
import net.minecraft.server.v1_8_R3.EntitySpider;

import org.bukkit.EntityEffect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Egg;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.Skeleton;
import org.bukkit.entity.Snowball;
import org.bukkit.entity.Spider;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

public class CustomJockey implements CustomMob {

	private Spider sp;
	private Skeleton sk;
	private World w;
	private int strsp;
	private int strsk;
	private int spd;
	private double hp;
	private Location spawn;
//	private final int default_effectdur = 200;
	private int effectdur;
	//10 Second poison duration
	private final int forever = 999999; 
	private final boolean isBoss;
	
	public CustomJockey(World w, boolean isBoss, int effectdur) {
		this.w = w;
		this.isBoss = isBoss;
		this.effectdur = effectdur;
	}
	
	
	@Override
	public void setMaxHealth(double hp) {
		try {
			sp.setMaxHealth(hp);
			sp.setHealth(sp.getMaxHealth());
			sk.setMaxHealth(hp);
			sk.setHealth(sk.getMaxHealth());
		} catch (NullPointerException e) {
			
		}
	}
	
	@Override
	public void setSpeed(int speed) {
		
		sp.addPotionEffect(PotionManager.getPotionEffect("SPEED", forever, speed));
	}
	
	public World getWWorld() {
		return w;
	}
	
	@Override
	public void spawnInWorld() {
		sp = (Spider)w.spawnEntity(spawn, EntityType.SPIDER);
		sp.setMetadata("dungeon", new FixedMetadataValue(DungeonManager.getPlugin(), true));
		sp.setMetadata("isspider", new FixedMetadataValue(DungeonManager.getPlugin(), effectdur));
		sk = (Skeleton)w.spawnEntity(spawn, EntityType.SKELETON);
		sk.setMetadata("dungeon", new FixedMetadataValue(DungeonManager.getPlugin(), true));
		//Messenger.severe("Setting Spider hp to " + hp);
		setMaxHealth(hp);
		setDamage(strsp, strsk);
		setSpeed(spd);
		if (isBoss)
			setBossArmor();
		else
			setNormArmor();
		setFireRes();
		sp.setPassenger(sk);
		//Messenger.broadcast("I THINK I DID IT " + hp);
	}

	@Override
	public void setDamage(int dmgsp, int dmgsk) {
		try {
			this.strsp = dmgsp;
			this.strsk = dmgsk;
			sp.addPotionEffect(PotionManager.getPotionEffect("INCREASE_DAMAGE", forever, dmgsp));
			sp.addPotionEffect(PotionManager.getPotionEffect("INVISIBILITY", forever, 0));
			sk.setMetadata("iscustom", new FixedMetadataValue(DungeonManager.getPlugin(), dmgsk));
		} catch (NullPointerException e) {
			
		}
	}

	@Override
	public void setProperties(World w, Location l, double hp, int dmgsp, int dmgsk, int speed) {
		this.w = w;
		this.spawn = l;
		this.hp = hp;
		this.strsp = dmgsp;
		this.strsk = dmgsk;
		this.spd = speed;
	}

	@Override
	public void setSpawnLocation(Location l) {
		this.spawn = l;
	}


	@Override
	public void setBossArmor() {
		EntityEquipment ee = sp.getEquipment();
		ee.setHelmetDropChance(100f);
		ee.setChestplateDropChance(100f);
		ee.setLeggingsDropChance(100f);
		ee.setBootsDropChance(100f);
		ee.setHelmet(new ItemStack(Material.DIAMOND_HELMET));
		ee.setChestplate(new ItemStack(Material.DIAMOND_CHESTPLATE));
		ee.setLeggings(new ItemStack(Material.DIAMOND_LEGGINGS));
		ee.setBoots(new ItemStack(Material.DIAMOND_BOOTS));
		ee.setItemInHand(new ItemStack(Material.DIAMOND_SWORD));
		ee.setItemInHandDropChance(0f);
		sp.setMetadata("isboss", new FixedMetadataValue(DungeonManager.getPlugin(), true));
		
		EntityEquipment eek = sk.getEquipment();
		eek.setHelmetDropChance(100f);
		eek.setChestplateDropChance(100f);
		eek.setLeggingsDropChance(100f);
		eek.setBootsDropChance(100f);
		eek.setHelmet(new ItemStack(Material.DIAMOND_HELMET));
		eek.setChestplate(new ItemStack(Material.DIAMOND_CHESTPLATE));
		eek.setLeggings(new ItemStack(Material.DIAMOND_LEGGINGS));
		eek.setBoots(new ItemStack(Material.DIAMOND_BOOTS));
		eek.setItemInHandDropChance(0f);
		sk.setMetadata("isboss", new FixedMetadataValue(DungeonManager.getPlugin(), true));
	}
	
	@Override
	public void setFireRes() {
		sp.addPotionEffect(PotionManager.getPotionEffect("FIRE_RESISTANCE", forever, 1));
		sk.addPotionEffect(PotionManager.getPotionEffect("FIRE_RESISTANCE", forever, 1));
	}
	
	@Override
	public void setNormArmor() {
		EntityEquipment ee = sp.getEquipment();
		ee.setHelmet(new ItemStack(Material.AIR));
		ee.setChestplate(new ItemStack(Material.AIR));
		ee.setLeggings(new ItemStack(Material.AIR));
		ee.setBoots(new ItemStack(Material.AIR));
		ee.setItemInHand(new ItemStack(Material.AIR));
		
		EntityEquipment eek = sp.getEquipment();
		eek.setHelmet(new ItemStack(Material.AIR));
		eek.setChestplate(new ItemStack(Material.AIR));
		eek.setLeggings(new ItemStack(Material.AIR));
		eek.setBoots(new ItemStack(Material.AIR));
		eek.setItemInHand(new ItemStack(Material.AIR));
	}


	@Override
	public void setDamage(int dmg) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void setProperties(World w, Location l, double hp, int dmg, int speed) {
		// TODO Auto-generated method stub
		
	}
	
    //public void forceFindTarget() {}
	

}
