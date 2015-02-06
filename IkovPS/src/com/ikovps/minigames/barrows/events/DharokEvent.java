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

public class DharokEvent implements Strategy
{
	@Override
	public boolean activate()
	{
		if(BarrowMethods.isLoggedIn() && ((BarrowLibrary.DHAROK_ZONE.inTheZone() || BarrowLibrary.AHRIM_ZONE.inTheZone()) && 
				BarrowLibrary.killCount == 2 && Inventory.getCount(BarrowLibrary.SHARK) > 0 ))
		{
			return true;
		}
		return false;
	}
	@Override
	public void execute()
	{
		BarrowLibrary.somethingWrong = false;
		
		if(!BarrowLibrary.DHAROK_ZONE.inTheZone())
		{
			BarrowLibrary.status = "Getting to Dharok";
			BarrowLibrary.DHAROK_PATH.traverse();
		}
		
		if(BarrowLibrary.mageIsOn)
			BarrowMethods.prayMage();
		
		Time.sleep(new SleepCondition()
	    {
	        @Override
	        public boolean isValid() {
	            return BarrowLibrary.DHAROK_ZONE.inTheZone();
	        }
	    }, 4500);
		
		if(BarrowLibrary.DHAROK_ZONE.inTheZone())
		{
			Menu.sendAction(74, BarrowLibrary.SPADE - 1, 0, 3214);
			Time.sleep(new SleepCondition()
			{
				@Override
				public boolean isValid() {
					return SceneObjects.getClosest(BarrowLibrary.DHAROK_TOMB)!= null;
				}
			}, 1500);			
		}
		
		BarrowMethods.potUp(); 
		Time.sleep(500);
		BarrowLibrary.status = "Summoning Dharok";
		
		if (SceneObjects.getNearest(BarrowLibrary.DHAROK_TOMB) != null)
		{
			 if(!BarrowLibrary.meleeIsOn)   
				 BarrowMethods.prayMelee();//turn on pray
			Time.sleep(500);
			 
            SceneObject dharokTomb = SceneObjects.getClosest(BarrowLibrary.DHAROK_TOMB);
            try{
                dharokTomb.interact(0);
	            } catch(ArrayIndexOutOfBoundsException a){
	            	System.out.println("Out of Bounds Caught");
	            }catch (NullPointerException e){
	            	System.out.println("Null Pointer Caught");
	            }
            Time.sleep(4000);
            //in case coffin didn't activate 1st time
            
            if(Players.getMyPlayer().getInteractingCharacter() == null)
            	try{
                    dharokTomb.interact(0);
	            	} catch(ArrayIndexOutOfBoundsException a){
		            	System.out.println("Out of Bounds Caught");
		            }catch (NullPointerException e){
		            	System.out.println("Null Pointer Caught");
		            }
            Time.sleep(500);
            
            BarrowLibrary.status = "Killing Dharok";
            BarrowMethods.startKillTime();
            
            while(Players.getMyPlayer().getInteractingCharacter()!=null || 
            		Players.getMyPlayer().isInCombat() && BarrowMethods.isLoggedIn())
            {
            	Time.sleep(1000);
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
            	
            	if(!BarrowMethods.isLoggedIn() || BarrowLibrary.killTime.getElapsedTime() > 70000)
            	{
            		BarrowLibrary.status = "Something went wrong, restarting";
            		BarrowLibrary.somethingWrong = true;
            		if (SceneObjects.getNearest(BarrowLibrary.DHAROK_STAIRS) != null)
            		{
    	                SceneObject dharokStairs = SceneObjects.getClosest(BarrowLibrary.DHAROK_STAIRS);
    	                
    	                while(!BarrowLibrary.DHAROK_ZONE.inTheZone() && BarrowMethods.isLoggedIn())
    	                {
    	                	try{
    	                        dharokStairs.interact(0);
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
                					return BarrowLibrary.DHAROK_ZONE.inTheZone();
                				}

                			}, 1500);	
    	                }
            		}
            		break;
            	}
            }
            
            Time.sleep(2500);//give it a couple seconds to record the kill
            if (BarrowLibrary.killCount == 2 && !BarrowLibrary.somethingWrong)
            {
            	BarrowLibrary.killCount += 1;
            	System.out.println("3 KC");
            	BarrowMethods.updatePaint(BarrowLibrary.killCount);
            	BarrowLibrary.paintIsUpdated = true;
            }
                    
            if(!BarrowLibrary.somethingWrong && SceneObjects.getNearest(BarrowLibrary.DHAROK_STAIRS) != null &&
            		!BarrowLibrary.LOOT_ZONE.inTheZone())
            {
            	BarrowLibrary.status = "Dharok killed, leaving tomb";
                SceneObject dharokStairs = SceneObjects.getClosest(BarrowLibrary.DHAROK_STAIRS);	    	               
                Time.sleep(1500);
                
                while(!BarrowLibrary.BARROW_ZONE.inTheZone() && !BarrowLibrary.LOOT_ZONE.inTheZone())
                {
                	try{
                        dharokStairs.interact(0);
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
                	if(!BarrowMethods.isLoggedIn() || BarrowLibrary.DHAROK_ZONE.inTheZone())
                		break;
                }
                
                System.out.println("dharok Done");
                
                if(Inventory.getCount(BarrowLibrary.SHARK) < 1 &&  BarrowMethods.isLoggedIn())
                {
                	BarrowLibrary.status = "out of food, getting more";
                	Keyboard.getInstance().sendKeys("::Home");
                }
                
                Time.sleep(1500);
            }
		}
	}
}

