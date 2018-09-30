package me.xDest.mcdungeon.listener;

import org.bukkit.GameMode;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.plugin.java.JavaPlugin;

import me.xDest.mcdungeon.Messenger;
import me.xDest.mcdungeon.dungeon.DungeonManager;

public class OnDeathInDungeon implements Listener {


	
	public OnDeathInDungeon(JavaPlugin pl) {
		
	}
	
	@EventHandler //Which means when they die??
	public void onPlayerDeath(PlayerDeathEvent event) {
		if (event.getEntity() == null)
			Messenger.severe("A null player has died");
		
		DungeonManager.playerHasDied(event.getEntity());
		Messenger.info("Found that a player has died");
		event.getEntity().setGameMode(GameMode.ADVENTURE);
	}
	
}
