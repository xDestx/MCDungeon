package me.xDest.mcdungeon.listener;

import java.util.List;

import me.xDest.mcdungeon.PlayerMoveManager;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class PlayerMoveListener implements Listener {

	JavaPlugin plugin;
	
	public PlayerMoveListener(JavaPlugin plugin) {
		this.plugin = plugin;
	}
	
	@EventHandler
	public void playerMove(PlayerMoveEvent event) {
		if (event.getTo().equals(event.getPlayer().getLocation()))
			return;
		List<String> frozen = PlayerMoveManager.getFrozen();
		for (String s : frozen) {
			if (event.getPlayer().getName().equals(s)) {
				Bukkit.getPlayer(s).teleport(event.getPlayer());
				break;
			}
		}
	}
	
	
}
