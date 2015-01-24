package com.ikovps.minigames.barrows.events;
import org.parabot.environment.api.utils.Time;
import org.parabot.environment.scripts.framework.SleepCondition;
import org.parabot.environment.scripts.framework.Strategy;
import org.rev317.min.api.methods.Inventory;
import org.rev317.min.api.methods.Menu;
import org.rev317.min.api.methods.SceneObjects;
import org.rev317.min.api.wrappers.Item;
import org.rev317.min.api.wrappers.SceneObject;

import com.ikovps.minigames.barrows.BarrowLibrary;
import com.ikovps.minigames.barrows.BarrowMethods;
/*
 * *Class to loot reward from barrows, throw away trash, and stash gold
 */
public class ChestEvent implements Strategy
{
	@Override
	public boolean activate()
	{
		if(BarrowMethods.isLoggedIn() && BarrowLibrary.LOOT_ZONE.inTheZone())
		{
			return true;
		}
		return false;
	}
	
	@Override
	public void execute()
	{   		
		BarrowLibrary.status = "Looting Chest";
		if(SceneObjects.getNearest(BarrowLibrary.CHEST) != null )
		{
			SceneObject barrowChest = SceneObjects.getClosest(BarrowLibrary.CHEST);
			try{
				barrowChest.interact(0);
			}catch(NullPointerException e){
			}
			
			Time.sleep(new SleepCondition()
	        {
	            @Override
	            public boolean isValid() {
	                return BarrowLibrary.START_ZONE.inTheZone();
	            }

	        }, 2500);
		}
		
		if(!BarrowLibrary.falseLoot)
		{
			BarrowLibrary.totalRuns += 1;	
			
			//turn off prayers if they're on
			if(BarrowLibrary.meleeIsOn)
				BarrowMethods.prayMelee();
			
			if(BarrowLibrary.mageIsOn)
				BarrowMethods.prayMage();
			
			if(BarrowLibrary.rangeIsOn)
				BarrowMethods.prayRange();
			
			//check for trash
			BarrowLibrary.status = "Checking for trash/storing cash";
			System.out.println("Checking for trash...");
			Time.sleep(1000);
			
	    	for (Item trash : Inventory.getItems(BarrowLibrary.TRASH))
	    	{
	    		if(trash != null)
	    		{
	    			Menu.sendAction(847, trash.getId() - 1, trash.getSlot(), 3214);
	    			Time.sleep(1000);
	    		}
	    	}
	    	
	    	Time.sleep(1000);
	    	
	    	//store gold in pouch
	    	if(Inventory.getItem(BarrowLibrary.GOLD) != null)
	    	{		
	    		System.out.println("storing cash");
	    		int slot = Inventory.getItem(996).getSlot();
				Menu.sendAction(493, BarrowLibrary.GOLD - 1, slot - 1, 3214);
				Time.sleep(500);
			}
	    	
	    	BarrowLibrary.killCount = 0;
	    	BarrowMethods.updatePaint(BarrowLibrary.killCount);
	    	BarrowLibrary.paintIsUpdated = true;
		}
	}
}
