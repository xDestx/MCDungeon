package me.xDest.mcdungeon;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.entity.Player;

public class PlayerMoveManager {

	private static List<UUID> frozenplayers = new ArrayList<UUID>();
	
	public static void freeze(Player p) {
		frozenplayers.add(p.getUniqueId());
	}
	
	public static void unFreeze(Player p) {
		frozenplayers.remove(p.getUniqueId());
	}
	
	public  static List<UUID> getFrozen() {
		return frozenplayers;
	}
	
	
}
