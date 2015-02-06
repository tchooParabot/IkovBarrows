package com.ikovps.minigames.barrows.events;

import org.parabot.environment.api.utils.Time;
import org.parabot.environment.input.Keyboard;
import org.parabot.environment.scripts.framework.SleepCondition;
import org.parabot.environment.scripts.framework.Strategy;
import org.rev317.min.api.methods.Inventory;
import org.rev317.min.api.methods.Menu;
import org.rev317.min.api.methods.Players;
import org.rev317.min.api.methods.SceneObjects;
import org.rev317.min.api.wrappers.SceneObject;

import com.ikovps.minigames.barrows.BarrowLibrary;
import com.ikovps.minigames.barrows.BarrowMethods;

public class GuthanEvent implements Strategy
{
	public boolean activate(){
		if(BarrowMethods.isLoggedIn() && ((BarrowLibrary.GUTHAN_ZONE.inTheZone() || BarrowLibrary.DHAROK_ZONE.inTheZone()) && 
				BarrowLibrary.killCount == 3 && Inventory.getCount(BarrowLibrary.SHARK) > 0 ))
		{
			return true;
		}
		return false;
	}
	
	@Override
	public void execute()
	{
		BarrowLibrary.somethingWrong = false;
		if(!BarrowLibrary.GUTHAN_ZONE.inTheZone())
		{
			BarrowLibrary.status = "Getting to Guthan";
			BarrowLibrary.GUTHAN_PATH.traverse();
		}
		
		if(BarrowLibrary.meleeIsOn)
			BarrowMethods.prayMelee();
		Time.sleep(new SleepCondition()
	     {
	        @Override
	        public boolean isValid() {
	            return BarrowLibrary.GUTHAN_ZONE.inTheZone();
         }
	     }, 5500);
		
		if(BarrowLibrary.GUTHAN_ZONE.inTheZone())
		{
				Menu.sendAction(74, BarrowLibrary.SPADE - 1, 0, 3214);
				Time.sleep(new SleepCondition()
		        {
		            @Override
		            public boolean isValid() {
		                return SceneObjects.getClosest(BarrowLibrary.GUTHAN_TOMB)!= null;
		            }
		        }, 1500);
		}
		
		BarrowMethods.potUp(); 
		Time.sleep(500);
		BarrowLibrary.status = "Summoning Guthan";
		
		if (SceneObjects.getNearest(BarrowLibrary.GUTHAN_TOMB) != null)
		{			 
            SceneObject guthanTomb = SceneObjects.getClosest(BarrowLibrary.GUTHAN_TOMB);
            try{
                guthanTomb.interact(0);
	            } catch(ArrayIndexOutOfBoundsException a){
	            	System.out.println("Out of Bounds Caught");
	            }catch (NullPointerException e){
	            	System.out.println("Null Pointer Caught");
	            }
            Time.sleep(4000);
            
            //in case coffin didn't activate 1st time
            if(Players.getMyPlayer().getInteractingCharacter() == null)
            	try{
                    guthanTomb.interact(0);
	            	} catch(ArrayIndexOutOfBoundsException a){
		            	System.out.println("Out of Bounds Caught");
		            }catch (NullPointerException e){
		            	System.out.println("Null Pointer Caught");
		            }
            Time.sleep(500);
            
            BarrowLibrary.status = "Killing Guthan";
            BarrowMethods.startKillTime();
            
            while(Players.getMyPlayer().getInteractingCharacter()!=null || Players.getMyPlayer().isInCombat())
            {
            	if(Players.getMyPlayer().isInCombat())
            	{
            		 if(!BarrowLibrary.meleeIsOn)   
        				 BarrowMethods.prayMelee();
        			 Time.sleep(500);
            	}
            	
            	if(Players.getMyPlayer().getHealth() < 60 && Players.getMyPlayer().isInCombat())
            	{
            		BarrowLibrary.meleeIsOn = false;
            		BarrowMethods.prayMelee();	
            		
            		if(Inventory.getCount(BarrowLibrary.SHARK) > 0)
            		{
            			BarrowMethods.eat();
            			Time.sleep(new SleepCondition() {
            				@Override
            				public boolean isValid() {
            					return Players.getMyPlayer().getHealth() > 70;
            				}

            			}, 1500);	
            		}
            		
            		else
            		{
            			BarrowLibrary.somethingWrong = true;            		
            			BarrowLibrary.status = "Failsafe banking due to health";
            			Keyboard.getInstance().sendKeys("::Home");
            			
            			Time.sleep(new SleepCondition() {
            				@Override
            				public boolean isValid() {
            					return BarrowLibrary.BANK_ZONE.inTheZone();
            				}

            			}, 1500);	
            			break;
            		}
            	}
            	       
            	if(!BarrowMethods.isLoggedIn() || BarrowLibrary.killTime.getElapsedTime() > 65000)
            	{
            		BarrowLibrary.status = "Something went wrong, restarting";
            		BarrowLibrary.somethingWrong = true;
            		if (SceneObjects.getNearest(BarrowLibrary.GUTHAN_STAIRS) != null){
    	                SceneObject guthanStairs = SceneObjects.getClosest(BarrowLibrary.GUTHAN_STAIRS);
    	                
    	                while(!BarrowLibrary.GUTHAN_ZONE.inTheZone() && BarrowMethods.isLoggedIn())
    	                {
    	                	try{
    	                        guthanStairs.interact(0);
	    	                	} catch(ArrayIndexOutOfBoundsException a){
	    	    	            	System.out.println("Out of Bounds Caught");
	    	    	            	break;
	    	    	            }catch (NullPointerException e){
	    	    	            	System.out.println("Null Pointer Caught");
	    	    	            	break;
	    	    	            }
    	                	Time.sleep(new SleepCondition() {
                				@Override
                				public boolean isValid() {
                					return BarrowLibrary.GUTHAN_ZONE.inTheZone();
                				}

                			}, 1500);	
    	                }
            		}
            		break;
            	}
            }
            
            Time.sleep(2500);//give it a couple seconds to record the kill
            if (BarrowLibrary.killCount == 3 && !BarrowLibrary.somethingWrong)
            {
            	BarrowLibrary.killCount += 1;
            	System.out.println("4 KC");
            	BarrowMethods.updatePaint(BarrowLibrary.killCount);
            	BarrowLibrary.paintIsUpdated = true;
            }
            
            if(!BarrowLibrary.somethingWrong && SceneObjects.getNearest(BarrowLibrary.GUTHAN_STAIRS) != null &&
            		!BarrowLibrary.LOOT_ZONE.inTheZone())
            {
            	BarrowLibrary.status = "Guthan killed, leaving tomb";
                SceneObject guthanStairs = SceneObjects.getClosest(BarrowLibrary.GUTHAN_STAIRS);	    	               
                Time.sleep(1500);
                
                while(!BarrowLibrary.BARROW_ZONE.inTheZone() && !BarrowLibrary.LOOT_ZONE.inTheZone())
                {
                	try{
                        guthanStairs.interact(0);
	                	} catch(ArrayIndexOutOfBoundsException a){
	    	            	System.out.println("Out of Bounds Caught");
	    	            	break;
	    	            }catch (NullPointerException e){
	    	            	System.out.println("Null Pointer Caught");
	    	            	break;
	    	            }
                	
                	Time.sleep(new SleepCondition() {
        				@Override
        				public boolean isValid() {
        					return BarrowLibrary.BARROW_ZONE.inTheZone();
        				}
        			}, 2500);
                	
                	if(BarrowLibrary.falseLoot)
                	{
                		BarrowLibrary.status = "Teleported too early, restarting run";
                		break;
                	}
                	if(!BarrowMethods.isLoggedIn() || BarrowLibrary.GUTHAN_ZONE.inTheZone())
                		break;
                }
                
                System.out.println("guthan Done");
             
                if(Inventory.getCount(BarrowLibrary.SHARK) < 1 && BarrowMethods.isLoggedIn())
                {
                	BarrowLibrary.status = "out of food, getting more";
                	Keyboard.getInstance().sendKeys("::Home");
                }
                Time.sleep(1500);
            }
		}
	}
}


