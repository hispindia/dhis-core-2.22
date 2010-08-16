/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.hisp.dhis.cbhis.gui;

import java.util.*;
import javax.microedition.lcdui.*;


/**
 *
 * @author abyotag_adm
 */
public class SplashScreen extends Canvas
{
    
    private Display display;
    
    private Displayable  nextScreen;

    private Image image;
    
    private Timer timer = new Timer();

    public SplashScreen(Image image, Display display, Displayable nextScreen)
    {
        this.image = image;
        this.display = display;
        this.nextScreen = nextScreen;
        display.setCurrent( this );
    }

    protected void keyPressed( int keyCode ){
        dismissSplashScreen();
    }

    protected void paint( Graphics g ){
        g.setColor(255, 255, 255);
        g.fillRect(0, 0, getWidth(), getHeight());
        if (image != null)
            g.drawImage (image, getWidth()/2, getHeight()/2, Graphics.HCENTER | Graphics.VCENTER);
    }

    protected void pointerPressed( int x, int y ){
        dismissSplashScreen();
    }

    protected void showNotify(){
        timer.schedule( new CountDown(), 3000 );
    }

    private void dismissSplashScreen(){
        timer.cancel();
        display.setCurrent( nextScreen );
    }

   //count down for the splash display
    private class CountDown extends TimerTask
    {
        public void run(){
            dismissSplashScreen();
        }
    }
}
