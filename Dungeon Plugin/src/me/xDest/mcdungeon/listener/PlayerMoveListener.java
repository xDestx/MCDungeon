package me.xDest.mcdungeon.listener;

import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.plugin.java.JavaPlugin;

import me.xDest.mcdungeon.PlayerMoveManager;

public class PlayerMoveListener implements Listener {

	JavaPlugin plugin;
	
	public PlayerMoveListener(JavaPlugin plugin) {
		this.plugin = plugin;
	}
	
	@EventHandler
	public void playerMove(PlayerMoveEvent event) {
		if (event.getTo().equals(event.getPlayer().getLocation()))
			return;
		List<UUID> frozen = PlayerMoveManager.getFrozen();
		for (UUID s : frozen) {
			if (event.getPlayer().getUniqueId().equals(s)) {
				Bukkit.getPlayer(s).teleport(event.getPlayer());
				break;
			}
		}
	}
	
	
}
