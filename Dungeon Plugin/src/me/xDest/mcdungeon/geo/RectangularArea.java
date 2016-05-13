/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package me.xDest.mcdungeon.geo;

import java.util.ArrayList;
import java.util.List;

import me.xDest.mcdungeon.Messenger;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

/**
 *
 * @author Ben
 */
public class RectangularArea extends Area{
	
    public Vector lowerlefthandcorner, upperrighthandcorner;
    
    public RectangularArea(Vector lowerlefthandcorner, Vector upperrighthandcorner){
        this.lowerlefthandcorner = lowerlefthandcorner;
        this.upperrighthandcorner = upperrighthandcorner;
    }
    
    public RectangularArea(){
        
    }

    @Override
    public boolean isInArea(Location l) {
        if(l.getX() >= lowerlefthandcorner.getX() && l.getX() <= upperrighthandcorner.getX() + 1){
            if(l.getZ() <= lowerlefthandcorner.getZ() && l.getZ() >= upperrighthandcorner.getZ() + 1){
            	if (l.getY() >= lowerlefthandcorner.getY() && l.getY() <= upperrighthandcorner.getY()) {
            		return true;
            	}
            }
        }
        return false;
    }

	@Override
	public void update(Player p, boolean in) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public List<Chunk> getChunks(World w) {
		List<Chunk> chunks = new ArrayList<Chunk>();
		int x1 = (int) lowerlefthandcorner.toLocation(w).getX();
		int x2 = (int) upperrighthandcorner.toLocation(w).getX();
        Messenger.severe("X1 " + x1 + " X2 " + x2);
		if (x2 > x1) {
			int a = x2;
			x2 = x1;
			x1 = a;
		}
		Messenger.severe("X1 " + x1 + " X2 " + x2);
		int z1 = (int) lowerlefthandcorner.toLocation(w).getZ();
		int z2 = (int) upperrighthandcorner.toLocation(w).getZ();
		Messenger.severe("Z1 " + z1 + " Z2 " + z2);
		if (z2 > z1) {
			int a = z2;
			z2 = z1;
			z1 = a;
		}
	//	Messenger.severe("Z1 " + z1 + " Z2 " + z2);
		int repeater = 0;
		for(int x = x1; x > (x2 - 16); x = x - 16) {
			//Messenger.severe("CUSTOM DUNGEON CHUNKS: " + chunks.size() + " REPEATER: " + repeater);
		    for(int z = z1; z > (z2 - 16); z = z - 16) {
		        chunks.add(w.getChunkAt(new Location(w, x, 64, z)));
		        repeater++;
		    }
		}

		//Messenger.severe("CUSTOM DUNGEON CHUNKS: " + chunks.size() + "THIS IS FINAL REPEATER: " + repeater);

		return chunks;
	}
    
}
