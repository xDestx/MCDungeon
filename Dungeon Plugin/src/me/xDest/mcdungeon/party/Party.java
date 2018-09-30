package me.xDest.mcdungeon.party;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import me.xDest.mcdungeon.Messenger;

public class Party {

	private List<UUID> members = new ArrayList<UUID>();
	private List<UUID> requestlist = new ArrayList<UUID>();
	private Player owner;
	private final String prefix = ChatColor.GOLD + "[PARTY] " + ChatColor.GRAY;
	
	public Party(Player owner){
		this.owner = owner;
		members.add(owner.getUniqueId());
		owner.sendMessage(prefix + " Party Created!");
		}
	
	
	public void addToParty(UUID id) {
		
		members.add(id);
		sendPartyCMessage(Bukkit.getPlayer(id).getDisplayName() + " has joined!");
	}
	
	public void requestJoin(Player requester) {
		if (playerIsInParty(requester.getUniqueId()))
			return;
		if (requestlist.contains(requester.getUniqueId()))
			return;
		owner.sendMessage(prefix + requester.getDisplayName() + " wants to join your party! Type /party accept " + requester.getName() + " to allow them to join");
		requestlist.add(requester.getUniqueId());
	}
	
	public List<UUID> getMemberList() {
		for (UUID id : members) {
			Messenger.info("Player: " + Bukkit.getPlayer(id).getDisplayName());
		}
		if (members.isEmpty())
			return null;
		return members;
	}
	
	public String getMemberListAsString() {
		String returned = "";
		try {
		for (UUID s : members) {
			returned = returned + Bukkit.getPlayer(s).getDisplayName() + ", ";
		}
		returned = returned.substring(0, returned.length() -2);
		} catch (Exception e) {
			returned = "No members";
		}
		return returned;
	}
	
	public List<UUID> getRequestList() {
		if (requestlist.isEmpty())
			return null;
		return requestlist;
	}
	
	
	public String getRequestListAsString() {
		String returned = "";
		try {
		for (UUID s : requestlist) {
			returned = returned + Bukkit.getPlayer(s) + ", ";
		}
		returned = returned.substring(0, returned.length() -2);
		} catch (Exception e) {
			returned = "No requests";
		}
		return returned;
	}
	
	public void confirmRequest(Player sender, Player confirmed) {
		if (sender.getName().equals(owner.getName())) {
			if (requestlist.contains(confirmed.getUniqueId())) {
				requestlist.remove(confirmed.getUniqueId());
				addToParty(confirmed.getUniqueId());
				confirmed.sendMessage(prefix + " You have joined the party");
			}
		}
	}
	
	public Player getOwner() {
		return owner;
	}
	
	public boolean hasPlayer(Player player) {
		if (members.contains(player.getUniqueId()))
			return true;
		return false;
	}
	
	public void removeFromParty(Player p, Player sender) {
		if (p == null) {
			Messenger.severe("Tried to kick a null player");
		}
		if (sender == null) {
			Messenger.severe("A null player tried to kick");
		}
		if(!playerIsInParty(p.getUniqueId()))
			return;
		members.remove(p.getUniqueId());
		if (p == owner) {
			if (members.isEmpty()) {
				PartyManager.removeParty(owner);
				//Messenger.broadcast("Tried to remove the party");
				owner = null;
			} else {
				owner = Bukkit.getPlayer(members.get(0));
				PartyManager.newOwner(p, owner, this);
				owner.sendMessage("You are the new party owner!");
			}
		}
		
	}
	
	public boolean playerIsInParty(UUID p) {
		if (members.contains(p))
			return true;
		return false;
	}
	
	public void sendPartyMessage(Player sender, String msg) {
		for (UUID s : members) {
			Bukkit.getPlayer(s).sendMessage(prefix + sender.getDisplayName() + ": " + msg);
		}
	}
	
	public void sendPartyCMessage(String msg) {
		try {
			for (UUID s : members) {
				Bukkit.getPlayer(s).sendMessage(prefix + msg);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public int getSize() {
		return members.size();
	}


	public boolean isEmpty() {
		if (members.size() == 0) 
			return true;
		
		return false;
	}
	
}
