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

public class KarilEvent implements Strategy
{
	public boolean activate()
	{
		if(BarrowMethods.isLoggedIn() && ((BarrowLibrary.KARIL_ZONE.inTheZone() || BarrowLibrary.GUTHAN_ZONE.inTheZone()) && 
				BarrowLibrary.killCount == 4 && Inventory.getCount(BarrowLibrary.SHARK) > 0 ))
		{
			return true;
		}
		return false;
	}
	
	@Override
	public void execute()
	{
		BarrowLibrary.somethingWrong = false;
		if(!BarrowLibrary.KARIL_ZONE.inTheZone())
		{
			BarrowLibrary.status = "Getting to Karil";
			BarrowLibrary.KARIL_PATH.traverse();
		}
		
		if(BarrowLibrary.meleeIsOn)
			BarrowMethods.prayMelee();
		
		Time.sleep(new SleepCondition()
	    {
	        @Override
	        public boolean isValid() {
	            return BarrowLibrary.KARIL_ZONE.inTheZone();
	        }
        }, 4500);
		
		if(BarrowLibrary.KARIL_ZONE.inTheZone())
		{
			while(BarrowLibrary.KARIL_ZONE.inTheZone())
			{
				Menu.sendAction(74, BarrowLibrary.SPADE - 1, 0, 3214);
				Time.sleep(new SleepCondition()
		        {
		            @Override
		            public boolean isValid() {
		                return SceneObjects.getClosest(BarrowLibrary.KARIL_TOMB) != null;
		            }
		        }, 1500);
			}
		}
		
		BarrowMethods.potUp(); 
		Time.sleep(500);
		BarrowLibrary.status = "Summoning Karil";
		
		if (SceneObjects.getNearest(BarrowLibrary.KARIL_TOMB) != null)
		{
			if(!BarrowLibrary.rangeIsOn)   
				BarrowMethods.prayRange();//turn on pray
			Time.sleep(500);
			
            SceneObject karilTomb = SceneObjects.getClosest(BarrowLibrary.KARIL_TOMB);
            try{
                karilTomb.interact(0);
	            } catch(ArrayIndexOutOfBoundsException a){
	            	System.out.println("Out of Bounds Caught");
	            }catch (NullPointerException e){
	            	System.out.println("Null Pointer Caught");
	            }
            Time.sleep(4000);
            
            //in case coffin didn't activate 1st time
            if(Players.getMyPlayer().getInteractingCharacter() == null)
            	try{
                    karilTomb.interact(0);
	            	} catch(ArrayIndexOutOfBoundsException a){
		            	System.out.println("Out of Bounds Caught");
		            }catch (NullPointerException e){
		            	System.out.println("Null Pointer Caught");
		            }
            Time.sleep(500);
            
            BarrowLibrary.status = "Killing Karil";
            BarrowMethods.startKillTime();
            
            while(Players.getMyPlayer().getInteractingCharacter()!=null || Players.getMyPlayer().isInCombat())
            {
            	Time.sleep(1000);
            	if(Players.getMyPlayer().getHealth() < 60 && Players.getMyPlayer().isInCombat())
            	{
            		BarrowLibrary.rangeIsOn = false;
            		BarrowMethods.prayRange();	 
            		
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
            	
            	if(!BarrowMethods.isLoggedIn() || BarrowLibrary.killTime.getElapsedTime() > 70000)
            	{
            		BarrowLibrary.somethingWrong = true;
            		
            		if (SceneObjects.getNearest(BarrowLibrary.KARIL_STAIRS) != null)
            		{
    	                SceneObject karilStairs = SceneObjects.getClosest(BarrowLibrary.KARIL_STAIRS);
    	                
    	                while(!BarrowLibrary.KARIL_ZONE.inTheZone() && BarrowMethods.isLoggedIn())
    	                {
    	                	try{
    	                        karilStairs.interact(0);
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
                					return BarrowLibrary.KARIL_ZONE.inTheZone();
                				}

                			}, 1500);	
    	                }
            		}
            		break;
            	}
            }
            
            Time.sleep(2500);//give it a couple seconds to record the kill
            if (BarrowLibrary.killCount == 4 && !BarrowLibrary.somethingWrong)
            {
            	BarrowLibrary.killCount += 1;
            	System.out.println("5 KC");
            	BarrowMethods.updatePaint(BarrowLibrary.killCount);
            	BarrowLibrary.paintIsUpdated = true;
            }
            
            if(!BarrowLibrary.somethingWrong && SceneObjects.getNearest(BarrowLibrary.KARIL_STAIRS) != null &&
            		!BarrowLibrary.LOOT_ZONE.inTheZone())
            {
            	BarrowLibrary.status = "Karil killed, leaving tomb";
            	
                SceneObject karilStairs = SceneObjects.getClosest(BarrowLibrary.KARIL_STAIRS);	    	               
                Time.sleep(1500);
                
                while(!BarrowLibrary.KARIL_ZONE.inTheZone() && !BarrowLibrary.LOOT_ZONE.inTheZone())
                {
                	try{
                        karilStairs.interact(0);
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
        					return BarrowLibrary.KARIL_ZONE.inTheZone();
        				}
        			}, 1500);	
                	
                	if(BarrowLibrary.falseLoot)
                	{
                		BarrowLibrary.status = "Teleported too early, restarting run";
                		break;
                	}
                	if(!BarrowMethods.isLoggedIn() || BarrowLibrary.KARIL_ZONE.inTheZone())
                		break;
                }
                
                System.out.println("karil Done");                               
                
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


