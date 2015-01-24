package com.ikovps.minigames.barrows;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import org.parabot.environment.api.interfaces.Paintable;
import org.parabot.environment.scripts.Category;
import org.parabot.environment.scripts.Script;
import org.parabot.environment.scripts.ScriptManifest;
import org.parabot.environment.scripts.framework.Strategy;
import org.parabot.environment.api.utils.Time;
import org.parabot.environment.api.utils.Timer;
import org.parabot.environment.input.Keyboard;
import org.rev317.min.api.events.MessageEvent;
import org.rev317.min.api.events.listeners.MessageListener;
import org.rev317.min.api.methods.Inventory;
import org.rev317.min.api.methods.Menu;

import com.ikovps.minigames.barrows.events.AhrimEvent;
import com.ikovps.minigames.barrows.events.BankEvent;
import com.ikovps.minigames.barrows.events.ChestEvent;
import com.ikovps.minigames.barrows.events.DharokEvent;
import com.ikovps.minigames.barrows.events.GuthanEvent;
import com.ikovps.minigames.barrows.events.KarilEvent;
import com.ikovps.minigames.barrows.events.RelogEvent;
import com.ikovps.minigames.barrows.events.TeleEvent;
import com.ikovps.minigames.barrows.events.ToragEvent;
import com.ikovps.minigames.barrows.events.VeracEvent;


@ScriptManifest(author = "Tchoo", category = Category.OTHER, description = "Does barrows minigame on Ikov, supports prayer renewals and sharks",
name = "IkovBarrows", servers = { "Ikov" }, version = 1.13)

public class IkovBarrows extends Script implements Paintable, MessageListener
{
	private final ArrayList<Strategy> strategies = new ArrayList<Strategy>();
	
	public static Timer time;
	private boolean picIsOn = false;
	@Override
	public boolean onExecute()
	{
		time = new Timer();
		BarrowLibrary.killCount = 0; //initialize KC to 0
		BarrowMethods.updatePaint(BarrowLibrary.killCount);
		picIsOn = true;
		
		if(!BarrowLibrary.autoCastOn && Inventory.containts(BarrowLibrary.BLOODS, BarrowLibrary.DEATHS))
		{
			Keyboard.getInstance().clickKey(KeyEvent.VK_F4);
			Time.sleep(500);
			
    		Menu.sendAction(104, 2444, 510, 12891);//enable auto-cast
            Time.sleep(500);
            
            BarrowLibrary.autoCastOn = true;
            Keyboard.getInstance().clickKey(KeyEvent.VK_F1);
			Time.sleep(500);
    	}
		
		strategies.add(new RelogEvent()); 
		strategies.add(new TeleEvent());
		strategies.add(new BankEvent());
		strategies.add(new VeracEvent());
		strategies.add(new AhrimEvent());
		strategies.add(new DharokEvent());
		strategies.add(new GuthanEvent());
		strategies.add(new KarilEvent());
		strategies.add(new ToragEvent());
		strategies.add(new ChestEvent());
		
		provide(strategies);
		return true;
	}
	
	@Override
    public void onFinish()
    {
        System.out.println("IkovBarrows ran for: " + time.toString());
        System.out.println("Completed runs " + BarrowLibrary.totalRuns);
    }	
		
	   private Image getImage(String url) 
	   {
	        try {
	            return ImageIO.read(new URL(url));
	        } catch(IOException e) {
	            return null;
	        }
	    }

	    private final Color color1 = new Color(51, 153, 0);

	    private final Font font1 = new Font("Copperplate Gothic Bold", 1, 10);
	    private final Font font2 = new Font("Copperplate Gothic Bold", 0, 10);
	    private final Font font3 = new Font("Copperplate Gothic Bold", 0, 14);
	    private final Font font4 = new Font("Copperplate Gothic Bold", 0, 17);

	    public Image img1 = getImage(BarrowLibrary.imageLink);
	    
	    @Override
	    public void paint (Graphics g1) 
	    {
	        Graphics2D g = (Graphics2D)g1;
	        if(picIsOn || BarrowLibrary.paintIsUpdated)
	        {
	        	img1 = getImage(BarrowLibrary.imageLink);	        
	        	g.drawImage(img1, 0, 325, null);
	        	BarrowLibrary.paintIsUpdated = false;
	        }
	        	
	        g.setFont(font1);
	        g.setColor(color1);
	        g.drawString("By: Tchoo", 2, 450);
	        g.setFont(font2);
	        g.drawString("V1.0", 475, 450);
	        g.setFont(font3);
	        g.drawString("Runs: " + BarrowLibrary.totalRuns, 2, 380);
	        g.drawString("Time: " +time.toString() , 2, 410);
	       
	        g.setFont(font2);
	        g.drawString("Status:  " + BarrowLibrary.status, 125, 450);
	        g.setFont(font4);
	        g.drawString("IkovBarrows", 2, 350);
	    }
	    
	    /*
	     * Message listening for generic system messages in order to toggle image to see chat window
	     * and to determine if we are sent to loot chest before all barrows are killed
	     */
	    @Override
	    public void messageReceived(MessageEvent m)
	    {                        
	        if (m.getType() == 0)
	        {   
	        	if((m.getMessage().contains("command does not exist"))){
	        		if(picIsOn)
	        			picIsOn = false;
	        		else
	        			picIsOn = true;
	        	}
	        	
	        	if((m.getMessage().contains("have to kill all barrow brothers"))){
	        		BarrowLibrary.falseLoot = true;
	        	}
	        }
	    }	   	   
}
