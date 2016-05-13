package me.xDest.mcdungeon;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.Player;

public class PlayerMoveManager {

	private static List<String> frozenplayers = new ArrayList<String>();
	
	public static void freeze(Player p) {
		frozenplayers.add(p.getName());
	}
	
	public static void unFreeze(Player p) {
		frozenplayers.remove(p.getName());
	}
	
	public  static List<String> getFrozen() {
		return frozenplayers;
	}
	
	
}
