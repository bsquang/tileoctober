package rootPackage;

import java.io.IOException;
import java.io.InputStream;

import javax.microedition.lcdui.*;
import javax.microedition.lcdui.game.GameCanvas;
import javax.microedition.media.Manager;
import javax.microedition.media.MediaException;
import javax.microedition.media.Player;
import javax.microedition.midlet.MIDlet;

public abstract class QCore extends GameCanvas implements Runnable {
	
	final int LOAD = 0;
    final int RUN = 1;
    final int PAUSED = 2;
    final int EXIT = 3;
    byte GAMESTATE = LOAD;
	
	private static int m_FPSLimiter;
    private long m_frameCoheranceTimer;
    private long g_currentMemory, g_LastMemory;
    
    int keyState = 0;
    boolean bTimeMark = true;
    long lTimerStart,lTimerStop;
    
    static {
        m_FPSLimiter = 1000 / QConfig.MAX_FPS;
    }
    
    public MIDlet qApplication;
    public Display qDisplay;
    
    public QCore()//MIDlet application, Display display)
    {
    	super(true);
        //qApplication = application;
        //qDisplay = display;
        m_frameCoheranceTimer = System.currentTimeMillis();
        Load();
    }
    
    public abstract void Load();
    
    public void Start()
    {    	
        Thread t = new Thread(this);
        t.setPriority(Thread.MAX_PRIORITY);
        t.start();
        
        GAMESTATE = RUN;
    }
    
    public void Paused()
    {
        GAMESTATE = PAUSED;
    }
    
    public void run() {
    	Debug("Update");
    	FontSet();
    	try {
	        while(GAMESTATE == RUN)
	        {
	        	if(QConfig.INPUT_MODE == QConfig.KEY_MODE) UpdateKeyPad();
	        	if(QConfig.FPSLimit_MODE) UpdateFPS();
				else Thread.sleep(10);
	        	
	        	Update();
	        	
	        	this.repaint();
	        }
    	} catch (InterruptedException e) {
    		e.printStackTrace();
    		GAMESTATE = EXIT;
		}
    }
    
    public Player PlayerAmr(String path) throws IOException, MediaException
    {
    	return Manager.createPlayer(this.GetResourceFromStream(path), "audio/amr");
    }    
    public void Timer_Start() { if(bTimeMark) { lTimerStart = System.currentTimeMillis(); bTimeMark = false; } }    
    public long Timer_MSElapsed() { lTimerStop = System.currentTimeMillis(); return lTimerStop - lTimerStart; }
    public void Timer_Reset() { bTimeMark = true; }
    public boolean Timer_TickMs(int ms)
    {
    	Timer_Start(); if( Timer_MSElapsed() > ms) { Timer_Reset(); return true; }
    	return false;
    }
    public boolean Timer_TickS(int seconds)
    {
    	Timer_Start(); if( Timer_MSElapsed() > seconds) { Timer_Reset(); return true; }
    	return false;
    }
    
    public void UpdateKeyPad()
    {
    	keyState = this.getKeyStates();
    	
    	if((keyState & RIGHT_PRESSED)!=0) KeyRightPressed();
    	if((keyState & LEFT_PRESSED)!=0) KeyLeftPressed();
    	if((keyState & UP_PRESSED)!=0) KeyUpPressed();
    	if((keyState & DOWN_PRESSED)!=0) KeyDownPressed();
    	if((keyState & FIRE_PRESSED)!=0) KeyFirePressed();
    }
    public abstract void KeyRightPressed();
    public abstract void KeyLeftPressed();
    public abstract void KeyUpPressed();
    public abstract void KeyDownPressed();
    public abstract void KeyFirePressed();
    
    public void UpdateFPS() throws InterruptedException
    {
    	long curTime = System.currentTimeMillis();
        m_frameCoheranceTimer = Math.min(m_frameCoheranceTimer, curTime);        
		Thread.sleep(Math.max(1L, (long) m_FPSLimiter - (curTime - m_frameCoheranceTimer)));		
    }
    
    public abstract void Update();
    
    public void paint(Graphics g) { CoreDraw(g); }
    
    public void CoreDraw(Graphics g)
    {
    	g.setColor(255, 255, 255);
        g.fillRect(0, 0, getWidth(), getHeight());
    }
    
    public void DrawString(int x, int y, String content)
    {
    	getGraphics().setColor(0,0,0);
        getGraphics().drawString(content, 0, 0, Graphics.TOP|Graphics.LEFT);
    }
    
    public void FontSet()
    {
    	Font myFont = getGraphics().getFont();
        myFont = Font.getFont(Font.FACE_SYSTEM,Font.STYLE_BOLD,Font.SIZE_MEDIUM);
        getGraphics().setFont(myFont);
    }
    
    public void CheckMemObject(String object)
    {
        GBMachine(20);
        g_currentMemory = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
        Debug(
                "Max memory : " + Runtime.getRuntime().totalMemory() + " bytes \n" +
                "Used memory : " + (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) + " bytes \n" +
                object +" cost : " + (g_currentMemory - g_LastMemory) + " bytes \n"
        );
        GBMachine(20);
        g_LastMemory = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
    }
    
    public void GBMachine(int sleep)
    {
        try {
            
            System.gc();
            Thread.sleep(sleep);

        } catch (InterruptedException ex) { ex.printStackTrace(); }
    }    
    
    public void Debug(String content)
    {
        if(QConfig.DEBUG_MODE)
            System.out.println(content);
    }
    
    public InputStream GetResourceFromStream(String path)
    {
        return getClass().getResourceAsStream(path);
    }
    
    public Image getImage(String path) throws IOException
    {        
        return Image.createImage(GetResourceFromStream(path));
    }
}
