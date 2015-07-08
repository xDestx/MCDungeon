package me.xDest.mcdungeon.custommob;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Monster;

public interface CustomMob {

	
	void setMaxHealth(double hp);
	void setDamage(int dmg);
	void setProperties(World w, Location l, double hp, int dmg, int speed);
	void setSpawnLocation(Location l);
	void spawnInWorld();
	void setSpeed(int speed);
	void setBossArmor();
	void setNormArmor();
	void setFireRes();
	
	
	
}
