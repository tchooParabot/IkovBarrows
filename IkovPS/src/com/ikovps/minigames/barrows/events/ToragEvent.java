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

public class ToragEvent implements Strategy
{
	public boolean activate()
	{
		if(BarrowMethods.isLoggedIn() && ((BarrowLibrary.TORAG_ZONE.inTheZone() || BarrowLibrary.KARIL_ZONE.inTheZone()) && 
				BarrowLibrary.killCount == 5 && Inventory.getCount(BarrowLibrary.SHARK) > 0))
		{
			return true;
		}
		return false;
	}
	
	@Override
	public void execute()
	{
		BarrowLibrary.somethingWrong = false;
		if(!BarrowLibrary.TORAG_ZONE.inTheZone()){
			BarrowLibrary.status = "Getting to Torag";
			BarrowLibrary.TORAG_PATH.traverse();
		}
		
		if(BarrowLibrary.rangeIsOn)
			BarrowMethods.prayRange();
		Time.sleep(new SleepCondition()
	    {
	        @Override
	        public boolean isValid() {
	            return BarrowLibrary.TORAG_ZONE.inTheZone();
	        }
	    }, 4500);
		
		if(BarrowLibrary.TORAG_ZONE.inTheZone())
		{
				Menu.sendAction(74, BarrowLibrary.SPADE - 1, 0, 3214);
				Time.sleep(new SleepCondition()
		        {
		            @Override
		            public boolean isValid() {
		                return SceneObjects.getClosest(BarrowLibrary.AHRIM_TOMB) != null;
		            }
		        }, 1500);
		}
		
		BarrowMethods.potUp(); 
		Time.sleep(500);
		BarrowLibrary.status = "Summoning Torag";
		
		if (SceneObjects.getNearest(BarrowLibrary.TORAG_TOMB) != null)
		{
			 if(!BarrowLibrary.meleeIsOn)   
				 BarrowMethods.prayMelee();//turn on pray
			 
			Time.sleep(500);
            SceneObject toragTomb = SceneObjects.getClosest(BarrowLibrary.TORAG_TOMB);
            try{
                toragTomb.interact(0);
	            } catch(ArrayIndexOutOfBoundsException a){
	            	System.out.println("Out of Bounds Caught");
	            }catch (NullPointerException e){
	            	System.out.println("Null Pointer Caught");
	            }
            Time.sleep(4000);
            
            //in case coffin didn't activate 1st time
            if(Players.getMyPlayer().getInteractingCharacter() == null)
            	try{
                    toragTomb.interact(0);
	            	} catch(ArrayIndexOutOfBoundsException a){
		            	System.out.println("Out of Bounds Caught");
		            }catch (NullPointerException e){
		            	System.out.println("Null Pointer Caught");
		            }
            Time.sleep(500);
            BarrowLibrary.status = "Killing Torag";
            BarrowMethods.startKillTime();
            
            while(Players.getMyPlayer().getInteractingCharacter()!=null || Players.getMyPlayer().isInCombat())
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

            	if(!BarrowMethods.isLoggedIn() || BarrowLibrary.killTime.getElapsedTime() > 65000)
            	{
            		BarrowLibrary.status = "Something went wrong, restarting";
            		BarrowLibrary.somethingWrong = true;
            		
            		if (SceneObjects.getNearest(BarrowLibrary.TORAG_STAIRS) != null)
            		{
    	                SceneObject toragStairs = SceneObjects.getClosest(BarrowLibrary.TORAG_STAIRS);
    	                
    	                while(!BarrowLibrary.TORAG_ZONE.inTheZone())
    	                {
    	                	try{
    	                        toragStairs.interact(0);
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
    	        					return BarrowLibrary.TORAG_ZONE.inTheZone();
    	        				}
    	        			}, 1500); 	                	
    	                }
    	                
            		}
            		break;
            	}
            }
            
            Time.sleep(new SleepCondition() {
				@Override
				public boolean isValid() {
					return Players.getMyPlayer().getInteractingCharacter() == null;
				}

			}, 1500);	
            
            if (BarrowLibrary.killCount == 5 && !BarrowLibrary.somethingWrong)
            {
            	BarrowLibrary.killCount += 1;
            	BarrowMethods.updatePaint(BarrowLibrary.killCount);
            	BarrowLibrary.paintIsUpdated = true;
            }
                    
            if(!BarrowLibrary.somethingWrong && SceneObjects.getNearest(BarrowLibrary.TORAG_STAIRS) != null &&
            		!BarrowLibrary.LOOT_ZONE.inTheZone())
            {
            	BarrowLibrary.status = "Torag Killed, leaving tomb";
                SceneObject toragStairs = SceneObjects.getClosest(BarrowLibrary.TORAG_STAIRS);	    	               
                Time.sleep(1500);
                while(!BarrowLibrary.BARROW_ZONE.inTheZone() && !BarrowLibrary.LOOT_ZONE.inTheZone())
                {
                	try{
                        toragStairs.interact(0);
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
                	
                	if(BarrowLibrary.falseLoot){
                		BarrowLibrary.status = "Teleported too early, restarting run";
                		break;
                	}
                	if(!BarrowMethods.isLoggedIn() || BarrowLibrary.TORAG_ZONE.inTheZone())
                		break;
                }
                
                System.out.println("torag Done");                          
                Time.sleep(800);
                
                if(BarrowLibrary.TORAG_ZONE.inTheZone())
                {
                	BarrowLibrary.killCount = 0;
                	BarrowMethods.updatePaint(BarrowLibrary.killCount);
                	BarrowLibrary.paintIsUpdated = true;
                	BarrowLibrary.TORVER_PATH.traverse();
                	BarrowLibrary.status = "Missed one, Making another round...";
                	Time.sleep(new SleepCondition()
    		        {
    		            @Override
    		            public boolean isValid() {
    		                return BarrowLibrary.VERAC_ZONE.inTheZone();
    		            }
    		        }, 3000);
                }
                
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

