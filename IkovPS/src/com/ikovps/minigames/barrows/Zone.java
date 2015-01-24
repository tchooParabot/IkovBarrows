package com.ikovps.minigames.barrows;
import org.rev317.min.api.methods.Players;
import org.rev317.min.api.wrappers.Tile;

//Zone class implements constructor to create zones for IkovBarrows 
public class Zone 
{
	Tile topLeftTile;
	Tile botRightTile;
	//No default constructor needed - Should add for good practice?
	
	/*Constructor taking in topLeft and bottomRight tile inputs of a rectangle/square
     *to determine if player is within the zone for action.	
	 */
	public Zone(Tile topLeftTile, Tile botRightTile)
	{
		this.topLeftTile = topLeftTile;
		this.botRightTile = botRightTile;
	}
	
	//Returns true if player is within the zone, else returns false
	public boolean inTheZone(){
		if(Players.getMyPlayer().getLocation().getX() > topLeftTile.getX() &&
				Players.getMyPlayer().getLocation().getY() < topLeftTile.getY() &&
					Players.getMyPlayer().getLocation().getX() < botRightTile.getX() &&
						Players.getMyPlayer().getLocation().getY() > botRightTile.getY())
			return true;
		else
			return false;
	}
}
