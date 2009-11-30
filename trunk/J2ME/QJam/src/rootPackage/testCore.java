package rootPackage;

import java.io.IOException;

import javax.microedition.lcdui.Graphics;
import javax.microedition.media.MediaException;
import javax.microedition.media.Player;

public class testCore extends QCore {
    
    QSprite testSprite;
    Player testMusic;
    boolean bPlay = false;
    
    public testCore() { }
    
    public void Load() {    	
        try {
        	
        	CheckMemObject("init");
        	
            testSprite = new QSprite(0, 0, 30, 30, "Images/Note.gif");
            CheckMemObject("testSprite");
            
        	testMusic = PlayerAmr("Sound/002.amr");
        	CheckMemObject("testMusic");
		
        } catch (IOException e) {e.printStackTrace();} catch (MediaException e) {e.printStackTrace();}
	}
    
    public void Update() {
    	
    	if(bPlay) {
	    	if(Timer_TickMs(600)) { 
	    		testSprite.NextFrame();
	    		//testSprite.mainSprite.move(10, 0);
	    	}	    	
    	}
    }
    
    public void paint(Graphics g)
    {   
    	super.paint(g);
        testSprite.Draw(g);
        DrawString(0, 0, "Hello");        
    }
    
    protected void pointerPressed(int x, int y) {
    	testSprite.SetPosition(x-15, y-15);
    }
    protected void pointerDragged(int x, int y) {
    	testSprite.SetPosition(x-15, y-15);
    }
	public void KeyFirePressed() {	
		if(!bPlay)
		{
			bPlay = true;
//			try {
//				testMusic.start();
//			} catch (MediaException e) { e.printStackTrace(); }
		}
	}
    public void KeyDownPressed() {
    	testSprite.Move(0, 1);
    }
    public void KeyUpPressed() {
    	testSprite.Move(0, -1);
    }
    public void KeyLeftPressed() {
    	testSprite.Move(-1, 0);
    }
    public void KeyRightPressed() {
    	testSprite.Move(1, 0);
    }
}
