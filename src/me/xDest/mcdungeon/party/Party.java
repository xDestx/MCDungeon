package me.xDest.mcdungeon.party;

import java.util.ArrayList;
import java.util.List;

import me.xDest.mcdungeon.Messenger;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class Party {

	private List<String> members = new ArrayList<String>();
	private List<String> requestlist = new ArrayList<String>();
	private Player owner;
	private final String prefix = ChatColor.GOLD + "[PARTY] " + ChatColor.GRAY;
	
	public Party(Player owner){
		this.owner = owner;
		members.add(owner.getName());
		owner.sendMessage(prefix + " Party Created!");
		}
	
	
	public void addToParty(String str) {
		
		members.add(str);
		sendPartyCMessage(str + " has joined!");
	}
	
	public void requestJoin(Player requester) {
		if (playerIsInParty(requester.getName()))
			return;
		if (requestlist.contains(requester.getName()))
			return;
		owner.sendMessage(prefix + requester.getName() + " Wants to join your party...Type /party accept " + requester.getName() + " to allow them to join");
		requestlist.add(requester.getName());
	}
	
	public List<String> getMemberList() {
		for (String s : members) {
			Messenger.info("Player: " + s);
		}
		if (members.isEmpty())
			return null;
		final List<String> returned = members;
		return returned;
	}
	
	public String getMemberListAsString() {
		String returned = "";
		try {
		for (String s : members) {
			returned = returned + s + ", ";
		}
		returned = returned.substring(0, returned.length() -2);
		} catch (Exception e) {
			returned = "No members";
		}
		return returned;
	}
	
	public List<String> getRequestList() {
		if (requestlist.isEmpty())
			return null;
		return requestlist;
	}
	
	
	public String getRequestListAsString() {
		String returned = "";
		try {
		for (String s : requestlist) {
			returned = returned + s + ", ";
		}
		returned = returned.substring(0, returned.length() -2);
		} catch (Exception e) {
			returned = "No requests";
		}
		return returned;
	}
	
	public void confirmRequest(Player sender, Player confirmed) {
		if (sender.getName().equals(owner.getName())) {
			if (requestlist.contains(confirmed.getName())) {
				requestlist.remove(confirmed.getName());
				addToParty(confirmed.getName());
				confirmed.sendMessage(prefix + " You have joined the party");
			}
		}
	}
	
	public Player getOwner() {
		return owner;
	}
	
	public boolean hasPlayer(Player player) {
		if (members.contains(player.getName()))
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
		if(!playerIsInParty(p.getName()))
			return;
		members.remove(p.getName());
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
	
	public boolean playerIsInParty(String p) {
		if (members.contains(p))
			return true;
		return false;
	}
	
	public void sendPartyMessage(Player sender, String msg) {
		for (String s : members) {
			Bukkit.getPlayer(s).sendMessage(prefix + sender.getName() + ": " + msg);
		}
	}
	
	public void sendPartyCMessage(String msg) {
		try {
			for (String s : members) {
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
