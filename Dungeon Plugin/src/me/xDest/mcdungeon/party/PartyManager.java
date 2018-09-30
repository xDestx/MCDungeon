package me.xDest.mcdungeon.party;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import me.xDest.mcdungeon.Messenger;

public class PartyManager {

	private static HashMap<String, Party> parties = new HashMap<String, Party>();
	
	public static void enable(JavaPlugin pl) {
	}
	
	public static void createNew(Player owner) {
		Party party = new Party(owner);
		parties.put(owner.getName(), party);
	}
	
	public static void newOwner(Player owner, Player newOwner, Party party) {
		parties.put(newOwner.getName(), party);
	//	Messenger.broadcast("A new party was created");
	}
	
	public static void removeParty(Player owner) {
		if(!parties.containsKey(owner.getName())) {
		//	Messenger.broadcast("Coulndt remove party");
			return;
		}
		parties.remove(owner.getName());
		//Messenger.broadcast("Removed Party");
	}
	
	public static void disband(Player owner) {
		String ownern = owner.getName();
		if (parties.containsKey(ownern)) {
			Party party = parties.get(ownern);
			party.sendPartyCMessage("Party disbanded!");
			List<UUID> members = party.getMemberList();
			try {
				for (UUID p : members) {
					Messenger.info("Removing player " + p);
					party.removeFromParty(Bukkit.getPlayer(p), owner);
				}
			} catch (Exception e) {
				
			}
			parties.remove(ownern);
		}
	}
	
	public static void requestToJoin(Player sender, Player owner) {
		String ownern = owner.getName();
		boolean playerAlreadyInParty = false;
		for (String s : parties.keySet()) {
			Party p = parties.get(s);
			if (p.hasPlayer(sender)) {
				playerAlreadyInParty = true;
				sender.sendMessage(ChatColor.DARK_GRAY + "You are already in a party!");
				break;
			}
		}
		if(playerAlreadyInParty)
			return;
		
		if (parties.containsKey(ownern)) {
			Party party = parties.get(ownern);
			party.requestJoin(sender);
		}
	}
	
	public static String getReqAsString(Player sender, Player owner) {
		if (!parties.containsKey(owner.getName()))
			return "Party does not exist";
		Party p = parties.get(owner.getName());
		return p.getRequestListAsString();
	}
	
	public static boolean partyExists(Player owner) {
		if (!parties.containsKey(owner.getName()))
			return false;

		//Messenger.broadcast("A new party exists with that owner");
		return true;
	}
	
	public static Party getParty(Player owner) {
		Party p = parties.get(owner.getName());
		//Messenger.broadcast(p.getMemberListAsString());
		return p;
	}
	
	public static void leaveParty(Player sender) {
		boolean notinparty = true;
		for (String s : parties.keySet()) {
			Party p = parties.get(s);
			if (p.hasPlayer(sender)) {
				if (p.getOwner().getName().equals(sender.getName())) {
					sender.sendMessage("You must disband your party.");
					notinparty = false;
					break;
				}
				p.removeFromParty(sender, p.getOwner());
				sender.sendMessage(ChatColor.RED + "You have left " + p.getOwner().getName() + "'s party");
				notinparty = false;
				break;
			}
		}
		if (notinparty) {
			sender.sendMessage(ChatColor.RED + "You are not in a party!");
		} else {
			
		}
			
	}
	
	public static Party isInParty(Player sender) {
		for (String s : parties.keySet()) {
			Party p = parties.get(s);
			if (p.hasPlayer(sender)) {
				//p.removeFromParty(sender, p.getOwner());
				//sender.sendMessage(ChatColor.RED + "You have left " + p.getOwner().getName() + "'s party");
				return p;
			}
		}
		return null;
	}
	
	public static String getMemAsString(Player sender) {
		Party p = null;
		for (String s : parties.keySet()) {
			p = parties.get(s);
			if (p.hasPlayer(sender)) {
				break;
			}
		}
		if (p == null) {
			return "Not in a party";
		}
		return p.getMemberListAsString();
	}
	
	public static void kickPlayerFromParty(Player sender, Player owner, Player kicked) {
		String ownern = owner.getName();
		String sendern = sender.getName();
		if (!sender.equals(owner)) 
			return;
		if (!parties.containsKey(sendern))
			return;
		Party party = parties.get(ownern);
		party.removeFromParty(kicked, owner);
	}
	
	public static void sendPartyMessage(Player sender, String msg) {
	//	String sendern = sender.getName();
		Party p = isInParty(sender);

		if (p == null) {
			sender.sendMessage("Not in a party");
			return;
		}
		
		//Messenger.info("Made it to here");
		p.sendPartyMessage(sender, msg);
	}
	
	public static void acceptPlayerJoin(Player owner, Player accepted) {
		String ownern = owner.getName();
		if (!parties.containsKey(ownern))
			return;
		Party p = parties.get(ownern);
		p.confirmRequest(owner, accepted);
		
	}
	
}
