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

public class VeracEvent implements Strategy
{
	@Override
	public boolean activate()
	{
		if(BarrowMethods.isLoggedIn() && ((BarrowLibrary.START_ZONE.inTheZone() || BarrowLibrary.VERAC_ZONE.inTheZone())&& 
				BarrowLibrary.killCount == 0 && Inventory.getCount(BarrowLibrary.SHARK) > 0)){
			return true;
		}
		return false;
	}
	
	@Override
	public void execute()
	{
		BarrowLibrary.somethingWrong = false;
		if(!BarrowLibrary.VERAC_ZONE.inTheZone())
		{
			BarrowLibrary.status = "Getting to Verac";
			BarrowLibrary.VERAC_PATH.traverse();
		}
		
		if(BarrowLibrary.meleeIsOn)
			BarrowMethods.prayMelee();
		
		Time.sleep(new SleepCondition()
	        {
	            @Override
	            public boolean isValid() {
	                return BarrowLibrary.VERAC_ZONE.inTheZone();
	            }
	        }, 5500);
			
		if(BarrowLibrary.VERAC_ZONE.inTheZone())
		{
			Menu.sendAction(74, BarrowLibrary.SPADE - 1, 0, 3214);
			Time.sleep(new SleepCondition()
		    {
		        @Override
		        public boolean isValid() {
		            return SceneObjects.getClosest(BarrowLibrary.VERAC_TOMB)!= null;
		        }	
		    }, 1500);
		}
		
		System.out.println("potting up");
		BarrowMethods.potUp(); 
		Time.sleep(500);
		BarrowLibrary.status = "Summoning Verac";
		
		if (SceneObjects.getNearest(BarrowLibrary.VERAC_TOMB) != null)
		{
			 if(!BarrowLibrary.meleeIsOn)   
				 BarrowMethods.prayMelee();//turn on pray
			Time.sleep(500);
			
            SceneObject veracTomb = SceneObjects.getClosest(BarrowLibrary.VERAC_TOMB);
            try{
            veracTomb.interact(0);
            } catch(ArrayIndexOutOfBoundsException a){
            	System.out.println("Out of Bounds Caught");
            }catch (NullPointerException e){
            	System.out.println("Null Pointer Caught");
            }
            
            Time.sleep(4000);
            //in case coffin didn't activate 1st time
            if(Players.getMyPlayer().getInteractingCharacter() == null)
            	try{
                    veracTomb.interact(0);
	            	} catch(ArrayIndexOutOfBoundsException a){
		            	System.out.println("Out of Bounds Caught");
		            }catch (NullPointerException e){
		            	System.out.println("Null Pointer Caught");
		            }
            
            Time.sleep(500);
            BarrowLibrary.status = "Killing Verac";
            BarrowMethods.startKillTime();
            
            while(Players.getMyPlayer().getInteractingCharacter()!=null && BarrowMethods.isLoggedIn())
            {
            	Time.sleep(1000);
            	if(Players.getMyPlayer().getHealth() < 60 && Players.getMyPlayer().isInCombat())
            	{
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

            	//if you get atked while doin verac or take longer than a min
            	if(!BarrowMethods.isLoggedIn() || Players.getMyPlayer().isInCombat() || BarrowLibrary.killTime.getElapsedTime() > 70000)
            	{
            		BarrowLibrary.status = "Something went wrong, restarting";
            		BarrowLibrary.somethingWrong = true;
            		
            		if (SceneObjects.getNearest(BarrowLibrary.VERAC_STAIRS) != null)
            		{
    	                SceneObject veracStairs = SceneObjects.getClosest(BarrowLibrary.VERAC_STAIRS);
    	                
    	                while(!BarrowLibrary.VERAC_ZONE.inTheZone() && BarrowMethods.isLoggedIn())
    	                {
    	                	try{
    	                        veracStairs.interact(0);
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
                					return BarrowLibrary.VERAC_ZONE.inTheZone();
                				}

                			}, 1500);	
    	                }
            		}
            		break;
            	}
            }
            
            Time.sleep(2500);//give it a couple seconds to record the kill
            
            if (BarrowLibrary.killCount == 0 && !BarrowLibrary.somethingWrong)
            {
            	BarrowLibrary.killCount += 1;
            	System.out.println("1 KC");
            	BarrowMethods.updatePaint(BarrowLibrary.killCount);
            	BarrowLibrary.paintIsUpdated = true;
            }
                       
            if(!BarrowLibrary.somethingWrong && SceneObjects.getNearest(BarrowLibrary.VERAC_STAIRS) != null &&
            		!BarrowLibrary.LOOT_ZONE.inTheZone())
            {
            	BarrowLibrary.status = "Verac Killed, leaving tomb";
                SceneObject j = SceneObjects.getClosest(BarrowLibrary.VERAC_STAIRS);	    	               
                Time.sleep(1500);
                
                while(!BarrowLibrary.VERAC_ZONE.inTheZone() && !BarrowLibrary.LOOT_ZONE.inTheZone())
                {
                	try{
                        j.interact(0);
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
        					return BarrowLibrary.VERAC_ZONE.inTheZone();
        				}
        			}, 1500);
                	if(BarrowLibrary.falseLoot)
                	{
                		BarrowLibrary.status = "Teleported too early, restarting run";
                		break;
                	}else
                	if(!BarrowMethods.isLoggedIn() || BarrowLibrary.VERAC_ZONE.inTheZone())
                		break;
                }
                
                System.out.println("verac Done");                          
                
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
