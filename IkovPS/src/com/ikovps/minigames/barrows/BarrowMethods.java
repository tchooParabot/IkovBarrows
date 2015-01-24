package com.ikovps.minigames.barrows;

import java.lang.reflect.Field;

import org.parabot.environment.api.utils.Time;
import org.parabot.environment.scripts.framework.SleepCondition;
import org.rev317.min.Loader;
import org.rev317.min.api.methods.Inventory;
import org.rev317.min.api.methods.Menu;
import org.rev317.min.api.methods.Players;
import org.rev317.min.api.methods.SceneObjects;
import org.rev317.min.api.methods.Skill;
import org.rev317.min.api.wrappers.Item;

/*
 * Methods class for IkovBarrows
 */
public class BarrowMethods 
{ 	
	
		public static void eat()
		{
		    int food = BarrowLibrary.SHARK;
		    
			if (Players.getMyPlayer().getHealth() < 60 ) 
			{	  
				for(int i = 0; i < Inventory.getItems(food).length && Players.getMyPlayer().getHealth() < 60; i++){
			        
					System.out.println("eating");
			        Menu.sendAction(74, food - 1, Inventory.getItems(food)[i].getSlot(), 3214); //eat sharks
			        
			        Time.sleep(new SleepCondition() {
						@Override
						public boolean isValid() {
							return Players.getMyPlayer().getHealth() > 55;
						}
					}, 2000);
			    }
			}		    
		}
		
		
		public static void potUp()
		{
			if (!BarrowLibrary.firstRenewDrink || Skill.PRAYER.getLevel() < 25 
					 && Inventory.getItems(BarrowLibrary.RENEWALS) != null && !BarrowLibrary.START_ZONE.inTheZone())
			{
	        	if(!BarrowLibrary.firstRenewDrink)
	        	{
	        		BarrowLibrary.firstRenewDrink = true;
	        		
	        		for(Item rnew : Inventory.getItems(BarrowLibrary.RENEWALS)){
	        			if(rnew != null)
	        				Menu.sendAction(74, rnew.getId()-1, rnew.getSlot(), 3214);
	        			    Time.sleep(2500);
	        			    break;
	        		}
	        		BarrowLibrary.renewTime.reset();
	        		BarrowLibrary.renewTime.restart();
	        		
	        	}
	        	
	        	if(BarrowLibrary.renewTime.getElapsedTime() > 295000 && Skill.PRAYER.getLevel() < 30){
	        		for(Item rnew : Inventory.getItems(BarrowLibrary.RENEWALS))
	        		{
	        			if(rnew != null)
	        				Menu.sendAction(74, rnew.getId()-1, rnew.getSlot(), 3214);
	        			Time.sleep(2500);
	        			break;
	        		}
	        		
	        		BarrowLibrary.renewTime.reset();
	        		BarrowLibrary.renewTime.restart();
	        	}
			}
		}
		
		
		public static void startKillTime()
		{
			BarrowLibrary.killTime.reset();
			BarrowLibrary.killTime.restart();
		}
		
		
		public static void prayMelee()
		{
			if(!BarrowLibrary.meleeIsOn && ((SceneObjects.getClosest(BarrowLibrary.VERAC_TOMB) != null ||
								SceneObjects.getClosest(BarrowLibrary.DHAROK_TOMB) != null ||
								SceneObjects.getClosest(BarrowLibrary.GUTHAN_TOMB) != null ||
								SceneObjects.getClosest(BarrowLibrary.TORAG_TOMB) != null)))
			{
				Menu.sendAction(169, -1, -1, 5623);
				Time.sleep(500);
				BarrowLibrary.meleeIsOn = true;
			}else if(BarrowLibrary.meleeIsOn){
				Menu.sendAction(169, -1, -1, 5623);
				Time.sleep(500);
				BarrowLibrary.meleeIsOn = false;
			}
		}	
		
		
		public static void prayMage()
		{
			if(!BarrowLibrary.mageIsOn && SceneObjects.getClosest(BarrowLibrary.VERAC_TOMB) != null)
			{
				Menu.sendAction(169, -1, -1, 5621);
				Time.sleep(500);
				BarrowLibrary.mageIsOn = true;
			}else if(BarrowLibrary.mageIsOn){
				Menu.sendAction(169, -1, -1, 5621);
				Time.sleep(500);
				BarrowLibrary.mageIsOn = false;
			}
		}

		
		public static void prayRange()
		{
			if(!BarrowLibrary.rangeIsOn && SceneObjects.getClosest(BarrowLibrary.KARIL_TOMB) != null)
			{
				Menu.sendAction(169, -1, -1, 5622);
				Time.sleep(500);
				BarrowLibrary.rangeIsOn = true;
			}else if(BarrowLibrary.rangeIsOn){
				Menu.sendAction(169, -1, -1, 5622);
				Time.sleep(500);
				BarrowLibrary.rangeIsOn = false;
			}
		}
		
		public static boolean isLoggedIn()
	    {
	        try
	        {
	            Field f = Loader.getClient().getClass().getDeclaredField("bz");
	            f.setAccessible(true);

	            return f.getBoolean(Loader.getClient());
	        }
	        catch (Exception e)
	        {
	            e.printStackTrace();
	            return false;
	        }
	    }
		
		public static void updatePaint(int killCount)
		{
			switch (killCount){
			case 0: 
				BarrowLibrary.imageLink = "http://i.imgur.com/wfedMXm.png";
				break;
			case 1:
				BarrowLibrary.imageLink = "http://i.imgur.com/gRNHwbZ.png";
				break;
			case 2:
				BarrowLibrary.imageLink = "http://i.imgur.com/VtrduyA.png";
				break;
			case 3:
				BarrowLibrary.imageLink = "http://i.imgur.com/Bmk9Ybp.png";
				break;
			case 4:
				BarrowLibrary.imageLink = "http://i.imgur.com/t6jfv3N.png";
				break;
			case 5:
				BarrowLibrary.imageLink = "http://i.imgur.com/orPp6Ui.png";
				break;
			case 6:
				BarrowLibrary.imageLink = "http://i.imgur.com/hnXSJOL.png";
				break;
				
			}
		}
}
