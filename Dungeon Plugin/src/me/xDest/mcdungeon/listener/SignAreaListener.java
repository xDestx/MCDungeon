package me.xDest.mcdungeon.listener;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class SignAreaListener implements Listener {

	JavaPlugin plugin;
	
	public SignAreaListener(JavaPlugin pl) {
		plugin = pl;
	}
	
	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent event) {
		if (!event.getAction().toString().equals("RIGHT_CLICK_BLOCK"))
			return;
		Player p = event.getPlayer();
		World w = p.getWorld();
		try {
			if (event.getClickedBlock().getState() instanceof Sign) {
				//signs.containsKey(event.getClickedBlock().getLocation())
				Sign s = (Sign) event.getClickedBlock().getState();
				Location bl = s.getLocation();
				//Messenger.info("MADE IT TO INSIDE");
				if (s.getLine(0).equals("[Area]")) {// Line 0 [Area] //Line 1 x //Line 2 z //Line 3 gamemode
					Location tp;
					int x = Integer.parseInt(s.getLine(1));
					int z = Integer.parseInt(s.getLine(2));
					int y = (int) bl.getY();
					@SuppressWarnings("deprecation")
					GameMode newGm = GameMode.getByValue(Integer.parseInt(s.getLine(3)));
					tp = new Location(w, x,y,z);
					p.teleport(tp);
					p.setGameMode(newGm);
				}
			}
		} catch (NullPointerException e) {
				//e.printStackTrace();
		}
	}
	
	
	@EventHandler
	public void onSignBreak(BlockBreakEvent event) {
		if (event.getBlock().getState() instanceof Sign) {
			Sign sign = (Sign)event.getBlock().getState();
			//Messenger.info("Sign broken");
			if (sign.getLine(0).equals("[Area]") || sign.getLine(0).equals("[Trade]") || sign.getLine(1).equals("[Dungeon]")) {
				//Messenger.info("Sign had one of them");
				if (!event.getPlayer().isOp()) {
					event.setCancelled(true);
					event.getPlayer().sendMessage("You must be an op to break this.");
					//Messenger.info("Player not op");
				} else {
					if (event.getPlayer().getGameMode() != GameMode.CREATIVE) {
						event.setCancelled(true);
						event.getPlayer().sendMessage("You must be in creative to break this.");
						//Messenger.info("Player not creative");
					}
				}
			}
		}
		
	}
	
}
