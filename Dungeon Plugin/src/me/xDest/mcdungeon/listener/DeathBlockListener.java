package me.xDest.mcdungeon.listener;

import me.xDest.mcdungeon.Messenger;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class DeathBlockListener implements Listener {

	@EventHandler
	public void playerMoveEvent(PlayerMoveEvent event) {
		Player p = event.getPlayer();
		Location l = new Location(p.getWorld(), p.getLocation().getX(), p.getLocation().getY() - 1, p.getLocation().getZ());
		if (l.getBlock().getType().equals(Material.BARRIER)) {
			if (!event.getPlayer().getGameMode().equals(GameMode.CREATIVE)) {
				event.getPlayer().setHealth(0);
			}
		}
	}
	
}
