package com.ikovps.minigames.barrows.events;
import org.parabot.environment.api.utils.Time;
import org.parabot.environment.input.Keyboard;
import org.parabot.environment.input.Mouse;
import org.parabot.environment.scripts.framework.SleepCondition;
import org.parabot.environment.scripts.framework.Strategy;
import org.rev317.min.Loader;

import com.ikovps.minigames.barrows.BarrowLibrary;
import com.ikovps.minigames.barrows.BarrowMethods;

import java.awt.Point;
import java.awt.event.KeyEvent;

/*
 * Minimal's relog method provided by Empathy
 */
public class RelogEvent implements Strategy
{
    @Override
    public boolean activate()
    {
        return !BarrowMethods.isLoggedIn();
    }

    @Override
    public void execute()
    {
        if (!BarrowMethods.isLoggedIn())
        {
            BarrowLibrary.status = "Logging in";
            System.out.println("Relogging....");

            Time.sleep(5000);

            Keyboard.getInstance().clickKey(KeyEvent.VK_ENTER);

            BarrowLibrary.status = "Waiting to login...";

            Time.sleep(new SleepCondition()
            {
                @Override
                public boolean isValid()
                {
                    return BarrowMethods.isLoggedIn();
                }
            }, 5000);

            if (BarrowMethods.isLoggedIn())
            {
            	BarrowLibrary.status = "Waiting after login";
                Time.sleep(4000);
            }
             
            if(Loader.getClient().getBackDialogId() == 4900)
            {
            	final Point p = new Point(310, 450);
            	Mouse.getInstance().click(p);
            	Time.sleep(300);
            }
            BarrowLibrary.falseLoot = true;
            BarrowLibrary.meleeIsOn = false;
            BarrowLibrary.mageIsOn = false;
            BarrowLibrary.rangeIsOn = false;
            BarrowLibrary.firstRenewDrink = false;
            BarrowLibrary.autoCastOn = false;
            
        }
    }
  

}