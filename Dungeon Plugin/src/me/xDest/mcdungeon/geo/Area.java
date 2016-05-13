package me.xDest.mcdungeon.geo;

import java.util.List;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

/**
 *
 * @author Ben
 */
public abstract class Area {
    
    public abstract boolean isInArea(Location l);
    
    public abstract void update(Player p, boolean in);
    
    public abstract List<Chunk> getChunks(World w);
}
