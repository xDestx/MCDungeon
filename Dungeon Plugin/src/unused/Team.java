package unused;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

public class Team {
	
	private String name;
	private ChatColor color = ChatColor.WHITE;
	private ChatColor[] colors = ChatColor.values();
	
	private ArrayList<UUID> players = new ArrayList<UUID>();
	
	public static HashMap<String,Team> teams = new HashMap<String,Team>();
	
	private int TEAM_MAX = -1;
	
	
	public Team(String name) {
		this.name = name;
		teams.put(name, this);
	}
	
	public Team(String name, String color) {
		this.name = name;
		for (ChatColor col : colors)
		{
			if (col.toString().equalsIgnoreCase(color))
			{
				this.color = col;
			}
		}
		teams.put(name, this);
	}
	
	public boolean join(UUID player)
	{
		if (players.size() >= TEAM_MAX && TEAM_MAX > -1)
		{
			Bukkit.getPlayer(player).sendMessage(ChatColor.DARK_RED + name + " is full!");
			return false;
		} else
		{
			if (players.contains(player))
			{
				Bukkit.getPlayer(player).sendMessage(ChatColor.DARK_RED + "You are already on this team!");
				return false;
			}
			players.add(player);
			Bukkit.getPlayer(player).sendMessage(ChatColor.GREEN + "You have joined team " + name);
			return true;
		}
	}
	
	public boolean leave(UUID player)
	{
		if (players.contains(player))
		{
			players.remove(player);
			Bukkit.getPlayer(player).sendMessage(ChatColor.RED + "You have left team " + name);
			return true;
		}
		return false;
	}
	
	public boolean containsPlayer(UUID player)
	{
		if (players.contains(player))
			return true;
		return false;
	}
	
	public static Team getTeam(String teamID)
	{
		if (teams.containsKey(teamID))
			return teams.get(teamID);
		return null;
	}
	
	//No idea if this works
	
	public boolean switchTeams(UUID player, String teamID)
	{
		if (!teams.containsKey(teamID))
			return false;
		
		for (String team : Team.teams.keySet())
		{
			if (Team.getTeam(team).containsPlayer(player))
				Team.getTeam(team).leave(player);
		}
		
		Team.getTeam(teamID).join(player);
		return true;
		
	}
	
	public void removeTeam()
	{
		teams.remove(name);
		if (players.size() != 0)
			players = new ArrayList<UUID>();
	}

}
