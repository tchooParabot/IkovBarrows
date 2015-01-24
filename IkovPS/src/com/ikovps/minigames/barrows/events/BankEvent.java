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
import org.rev317.min.api.methods.Players;
import org.rev317.min.api.methods.SceneObjects;
import org.rev317.min.api.wrappers.SceneObject;

import com.ikovps.minigames.barrows.BarrowLibrary;
import com.ikovps.minigames.barrows.BarrowMethods;

public class BankEvent implements Strategy
{
	@Override
	public boolean activate() 
	{							
        if (BarrowMethods.isLoggedIn() && ((Inventory.getCount() > 25 || Inventory.getCount(BarrowLibrary.SPADE) != 1 || 
        		Inventory.getCount(BarrowLibrary.RENEWALS) < 1) || Inventory.getCount(BarrowLibrary.SHARK) < 1 && 
        		(BarrowLibrary.START_ZONE.inTheZone()) || BarrowLibrary.BANK_ZONE.inTheZone()))
        {
     	   return true;
        }
        return false;
	}
	
	@Override
	public void execute()
	{
		final Point xMark = new Point(488, 30);
		BarrowLibrary.status = "Banking...";
		
		if(!BarrowLibrary.BANK_ZONE.inTheZone())
		{
			Keyboard.getInstance().sendKeys("::Home");
			
			Time.sleep(new SleepCondition() {
				@Override
				public boolean isValid() {
					return BarrowLibrary.BANK_ZONE.inTheZone();
				}

			}, 3500);
		}
		
		if(BarrowLibrary.BANK_ZONE.inTheZone())
		{
			BarrowLibrary.BANK_PATH.traverse();
			Time.sleep(3000);
		}
		
		if(SceneObjects.getNearest(BarrowLibrary.BANK) != null)
		{
			 SceneObject bankBooth = SceneObjects.getClosest(BarrowLibrary.BANK);
             if(bankBooth != null) 
             {
                 if(Players.getMyPlayer().getAnimation() == -1)
                 {
                      bankBooth.interact(0);
                      Time.sleep(new SleepCondition()
                      {
                          @Override
                          public boolean isValid() {
                              return Loader.getClient().getOpenInterfaceId() == 5292;
                          }
          
                      }, 1500);
                 }
             }
		}
		
		//deposit all inventory
        while(!Inventory.isEmpty() && Loader.getClient().getOpenInterfaceId() == 5292)
        {
      	  Mouse.getInstance().click(400, 300, true);
            Time.sleep(1000);
        }
        
        Menu.sendAction(632, BarrowLibrary.SPADE - 1, 1, 5382);//withdraw spade
        Time.sleep(300); 
        Menu.sendAction(78, BarrowLibrary.RENEWAL4 - 1, 0, 5382);//withdraw pray renewals
        Time.sleep(300);                            
     	Menu.sendAction(53, BarrowLibrary.DEATHS - 1, 3, 5382);//withdraw deaths
     	Time.sleep(1000);
     	
     	if(Loader.getClient().getOpenInterfaceId() == 5292)
     	{
     		  Keyboard.getInstance().sendKeys("12000");
     		  Time.sleep(500);
     	      Keyboard.getInstance().clickKey(KeyEvent.VK_ENTER);
     	}
     	
     	Time.sleep(800);
     	Menu.sendAction(53, BarrowLibrary.BLOODS - 1, 4, 5382);//withdraw bloods
        Time.sleep(1000);
        
        if(Loader.getClient().getOpenInterfaceId() == 5292)
        {
      	  Keyboard.getInstance().sendKeys("6000");
      	  Time.sleep(500);
      	  Keyboard.getInstance().clickKey(KeyEvent.VK_ENTER);
        }
        Time.sleep(600);
        
        if(BarrowLibrary.somethingWrong)//banking due to low health
        {
        	Menu.sendAction(75, BarrowLibrary.SHARK - 1, 2, 5382);//withdraw 5 shark
        	Mouse.getInstance().click(xMark);
	        Time.sleep(800);
        }
        else
        {
	        Menu.sendAction(632, BarrowLibrary.SHARK - 1, 2, 5382);//withdraw 1 shark
	        Time.sleep(500);
	        Menu.sendAction(632, BarrowLibrary.SHARK - 1, 2, 5382);//withdraw 1 shark
	        Time.sleep(500);
	        Mouse.getInstance().click(xMark);
	        Time.sleep(800);
        }
        
        if(BarrowLibrary.somethingWrong)
        {
        	while(Inventory.getCount(BarrowLibrary.SHARK) > 2)
        	{
        		BarrowMethods.eat();
        		Time.sleep(new SleepCondition()
                {
                    @Override
                    public boolean isValid() {
                        return Players.getMyPlayer().getHealth() > 70;
                    }
    
                }, 2000);
        		BarrowLibrary.somethingWrong = false;
        		if(Inventory.getCount(BarrowLibrary.SHARK) == 2) break;
        	}
        }
	}
}

