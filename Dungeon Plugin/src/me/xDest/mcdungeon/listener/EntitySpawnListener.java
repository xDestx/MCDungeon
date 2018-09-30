package me.xDest.mcdungeon.listener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import me.xDest.mcdungeon.dungeon.DungeonManager;
import me.xDest.mcdungeon.geo.RectangularArea;

public class EntitySpawnListener implements Listener {

	JavaPlugin plugin;
	private List<RectangularArea> fields = new ArrayList<RectangularArea>();
	private EntityType[] passivefood = {EntityType.COW, EntityType.SHEEP,EntityType.PIG,EntityType.CHICKEN};
	private HashMap<EntityType,Material> passivedrops = new HashMap<EntityType,Material>();
	
	
	public EntitySpawnListener(JavaPlugin pl, List<RectangularArea> dfields) {
		this.plugin = pl;
		this.fields = dfields;
		passivedrops.put(EntityType.COW, Material.BEEF);
		passivedrops.put(EntityType.SHEEP, Material.WHITE_WOOL);
		passivedrops.put(EntityType.PIG, Material.PORKCHOP);
		passivedrops.put(EntityType.CHICKEN, Material.CHICKEN);
	}
	
	@EventHandler
	public void onCreatureSpawn(CreatureSpawnEvent e) {
		final CreatureSpawnEvent event = e;
		fields = DungeonManager.getFields();
		Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
			@Override
			public void run() {
				if (!event.getEntity().hasMetadata("dungeon")) {
					for (RectangularArea field : fields) {
						if (field.isInArea(event.getEntity().getLocation())) {
							event.setCancelled(true);
							event.getEntity().remove();
						//	Messenger.info("Cancelled a spawn.");
						} else {
							EntityEquipment ee = event.getEntity().getEquipment();
							ee.setHelmet(new ItemStack(Material.IRON_HELMET, 1));
							ee.setChestplate(new ItemStack(Material.IRON_CHESTPLATE, 1));
							ee.setLeggings(new ItemStack(Material.IRON_LEGGINGS, 1));
							ee.setBoots(new ItemStack(Material.IRON_BOOTS, 1));
							event.getEntity().getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(70);
							event.getEntity().setHealth(70);
					
						}
					}
				} else {
				//	Messenger.info("Has it");
				}
			}
		}, 5l);
	}
	
	@EventHandler
	public void onEntityDeath(EntityDeathEvent e) {
		for (EntityType et : passivefood) {
			if (e.getEntityType().equals(et)) {
				Random x = new Random();
				int a = x.nextInt(3) + 1;
				e.getEntity().getWorld().dropItem(e.getEntity().getLocation(), new ItemStack(passivedrops.get(et), a));
				break;
			}
		}
	}
}
