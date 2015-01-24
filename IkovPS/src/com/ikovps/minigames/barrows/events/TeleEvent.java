package com.ikovps.minigames.barrows.events;
import java.awt.Point;
import java.awt.event.KeyEvent;

import org.parabot.environment.api.utils.Time;
import org.parabot.environment.input.Keyboard;
import org.parabot.environment.input.Mouse;
import org.parabot.environment.scripts.framework.SleepCondition;
import org.parabot.environment.scripts.framework.Strategy;
import org.rev317.min.Loader;
import org.rev317.min.api.methods.Inventory;
import org.rev317.min.api.methods.Menu;

import com.ikovps.minigames.barrows.BarrowLibrary;
import com.ikovps.minigames.barrows.BarrowMethods;
//teleport to barrows if we have certain items - and player is at edge bank area
public class TeleEvent implements Strategy{
	@Override
	public boolean activate()
	{
		if(BarrowMethods.isLoggedIn() && (BarrowLibrary.BANK_ZONE.inTheZone() && Inventory.getCount(BarrowLibrary.RENEWAL4) == 5 && 
				Inventory.getCount(BarrowLibrary.SHARK) == 2 || BarrowLibrary.falseLoot))
		{
			return true;
		}
		return false;
	}
	
	@Override
	public void execute()
	{
		if(!BarrowLibrary.START_ZONE.inTheZone())
		{
			BarrowLibrary.status = "Teleporting to Barrows...";
			System.out.println("teleporting to barrows");
			
			final Point miniTp = new Point(660, 250);
	        final Point barrowTp = new Point(260, 380);
	        
			Keyboard.getInstance().clickKey(KeyEvent.VK_F4);
			Time.sleep(500);
			
			if(!BarrowLibrary.autoCastOn)
			{
	    		Menu.sendAction(104, 2444, 510, 12891);//enable auto-cast
	            Time.sleep(500);
	            BarrowLibrary.autoCastOn = true;
	    	}
			
			
			Mouse.getInstance().click(miniTp);
			
			Time.sleep(new SleepCondition()
	        {
	            @Override
	            public boolean isValid() {
	                return Loader.getClient().getBackDialogId() == 2492;
	            }
	
	        }, 2000);
			
			
			if(Loader.getClient().getBackDialogId() == 2492)
				Mouse.getInstance().click(barrowTp);
			
			Time.sleep(new SleepCondition()
	        {
	            @Override
	            public boolean isValid() {
	                return BarrowLibrary.START_ZONE.inTheZone();
	            }
	
	        }, 2000);
	
			Keyboard.getInstance().clickKey(KeyEvent.VK_F1);//go back to inventory
			
			BarrowLibrary.killCount = 0;
			BarrowMethods.updatePaint(BarrowLibrary.killCount);
			BarrowLibrary.paintIsUpdated = true;
		}
		
		if(BarrowLibrary.falseLoot)
		{
			BarrowLibrary.falseLoot = false;
		}
		Time.sleep(3000);
	}
}
