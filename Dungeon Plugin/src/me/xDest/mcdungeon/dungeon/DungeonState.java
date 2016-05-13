package me.xDest.mcdungeon.dungeon;


public enum DungeonState {
	//Test
	JOINING(), START(), WAVES(), BOSS(), END(), KICK();
	
	private static  boolean canJoin;
	
	private static DungeonState currentState;

	DungeonState(){
	}
	
	public boolean canJoin(){
		return canJoin;
	}
	
	public void setState(DungeonState state){
		DungeonState.currentState = state;
	}
	
	public boolean isState(DungeonState state){
		return DungeonState.currentState == state;
	}
	
	public DungeonState getState(){
		return currentState;
	}
}
