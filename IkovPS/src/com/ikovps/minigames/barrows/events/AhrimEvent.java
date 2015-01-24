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

public class AhrimEvent implements Strategy
{
	public boolean activate()
	{
		if((BarrowMethods.isLoggedIn() && (BarrowLibrary.VERAC_ZONE.inTheZone() || BarrowLibrary.AHRIM_ZONE.inTheZone()) && 
				BarrowLibrary.killCount == 1 && Inventory.getCount(BarrowLibrary.SHARK) > 0 ))
		{
			return true;
		}
		return false;
	}
	@Override
	public void execute()
	{
		BarrowLibrary.somethingWrong = false;
		if(!BarrowLibrary.AHRIM_ZONE.inTheZone())
		{
			BarrowLibrary.status = "Getting to Ahrim";
			BarrowLibrary.AHRIM_PATH.traverse();
		}
		
		if(BarrowLibrary.meleeIsOn)
			BarrowMethods.prayMelee();
		
		Time.sleep(new SleepCondition()
	     {
	         @Override
	         public boolean isValid() {
	             return BarrowLibrary.AHRIM_ZONE.inTheZone();
	         }
	     }, 4500);	
		
		if(BarrowLibrary.AHRIM_ZONE.inTheZone())
		{
			Menu.sendAction(74, BarrowLibrary.SPADE - 1, 0, 3214);
			Time.sleep(new SleepCondition()
		    {
	            @Override
		        public boolean isValid() {
	                return SceneObjects.getClosest(BarrowLibrary.AHRIM_TOMB) != null;
	            }
	        }, 2000);
		}
		
		BarrowMethods.potUp(); 
		Time.sleep(500);
		BarrowLibrary.status = "Summoning Ahrim";
		
		if (SceneObjects.getNearest(BarrowLibrary.AHRIM_TOMB) != null)
		{			
			BarrowLibrary.AHRIM_SPATH.traverse(); 
            SceneObject ahrimTomb = SceneObjects.getClosest(BarrowLibrary.AHRIM_TOMB);
            try{
                ahrimTomb.interact(0);
	            } catch(ArrayIndexOutOfBoundsException a){
	            	System.out.println("Out of Bounds Caught");
	            }catch (NullPointerException e){
	            	System.out.println("Null Pointer Caught");
	            }
            Time.sleep(4000);
            
            BarrowLibrary.status = "Killing Ahrim...";
            
            //in case coffin didn't activate 1st time
            if(Players.getMyPlayer().getInteractingCharacter() == null)
            	try{
                    ahrimTomb.interact(0);
	            	 } catch(ArrayIndexOutOfBoundsException a){
	                 	System.out.println("Out of Bounds Caught");
	                 }catch (NullPointerException e){
	                 	System.out.println("Null Pointer Caught");
	                 }
            Time.sleep(500);
            
            BarrowMethods.startKillTime();          
            while(Players.getMyPlayer().getInteractingCharacter()!=null || 
            		Players.getMyPlayer().isInCombat())
            {
            	if(Players.getMyPlayer().isInCombat())
            	{
            		 if(!BarrowLibrary.mageIsOn)   
        				 BarrowMethods.prayMage();//turn on pray
        			Time.sleep(500);
        		}  
        			
            	if(Players.getMyPlayer().getHealth() < 60 && Players.getMyPlayer().isInCombat())
            	{
            		BarrowLibrary.mageIsOn = false;
            		BarrowMethods.prayMage();	 
            		if(Inventory.getCount(BarrowLibrary.SHARK) > 0)
            		{
            			BarrowMethods.eat();
            			Time.sleep(new SleepCondition() {
            				@Override
            				public boolean isValid() {
            					return Players.getMyPlayer().getHealth() > 70;
            				}

            			}, 2000);	
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
            		
            	if(!BarrowMethods.isLoggedIn() ||BarrowLibrary.killTime.getElapsedTime() > 70000)
            	{
            		BarrowLibrary.status = "Something went wrong, restarting";
            		BarrowLibrary.somethingWrong = true;
            		if (SceneObjects.getNearest(BarrowLibrary.AHRIM_STAIRS) != null)
            		{
    	                SceneObject ahrimStairs = SceneObjects.getClosest(BarrowLibrary.AHRIM_STAIRS);
    	                
    	                while(!BarrowLibrary.AHRIM_ZONE.inTheZone() && BarrowMethods.isLoggedIn())
    	                {
    	                	try{
    	                        ahrimStairs.interact(0);
    	                        }catch(ArrayIndexOutOfBoundsException a){
    	                        	System.out.println("Out of Bounds Caught");
    	                        	break;
    	                        }catch (NullPointerException e){
    	                        	System.out.println("Null Pointer Caught");
    	                        	break;
    	                        }
    	                	Time.sleep(new SleepCondition() {
                				@Override
                				public boolean isValid() {
                					return BarrowLibrary.AHRIM_ZONE.inTheZone();
                				}

                			}, 1500);	
    	                }
            		}
            		break;
            	}
            }
            Time.sleep(2500);//give it a couple seconds to record the kill
            
            if (BarrowLibrary.killCount == 1 && !BarrowLibrary.somethingWrong)
            {
            	BarrowLibrary.killCount += 1;
            	System.out.println("2 KC");
            	BarrowMethods.updatePaint(BarrowLibrary.killCount);
            	BarrowLibrary.paintIsUpdated = true;
            }
                       
            if(!BarrowLibrary.somethingWrong && SceneObjects.getNearest(BarrowLibrary.AHRIM_STAIRS) != null &&
            		!BarrowLibrary.LOOT_ZONE.inTheZone())
            {
            	BarrowLibrary.status = "Ahrim Killed, Leaving tomb";
                SceneObject ahrimStairs = SceneObjects.getClosest(BarrowLibrary.AHRIM_STAIRS);	    	               
                Time.sleep(1500);
                while(!BarrowLibrary.AHRIM_ZONE.inTheZone() && !BarrowLibrary.LOOT_ZONE.inTheZone())
                {
                	try{
                        ahrimStairs.interact(0);
                        } catch(ArrayIndexOutOfBoundsException a){
                        	System.out.println("Out of Bounds Caught");
                        	break;
                        }catch (NullPointerException e){
                        	System.out.println("Null Pointer Caught");
                        	break;
                        }
                		                	
                	Time.sleep(2500);
                	if(BarrowLibrary.falseLoot)
                	{
                		BarrowLibrary.status = "Teleported too early, restarting run";
                		break;
                	}
                	if(!BarrowMethods.isLoggedIn() || BarrowLibrary.AHRIM_ZONE.inTheZone())
                		break;
                }
                
                System.out.println("ahrim Done");
                              
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

