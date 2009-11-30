package rootPackage;
import javax.microedition.midlet.*;
import javax.microedition.lcdui.*;

/**
 * @author BSQUANG
 */
public class MainMidlet extends MIDlet implements CommandListener{
    
    private final static int PAUSED = 0;
    private final static int ACTIVE = 1;
    private final static int DESTROYED = 2;
    private int stateTracker = PAUSED;
    //rootCanvas mainCanvas;
    testCore core;
    Display myDisplay;
    
    Command exitCommand = new Command("Exit",Command.EXIT,1);
    
    public void startApp() {
        myDisplay = Display.getDisplay(this);
        if(core == null)
        {
            try {
                core = new testCore();
                core.Start();
//                mainCanvas = new rootCanvas();
//                mainCanvas.startGame();
//                mainCanvas.addCommand(exitCommand);
//                mainCanvas.setCommandListener(this);
                core.setFullScreenMode(true);

                myDisplay.setCurrent(core);
                stateTracker = ACTIVE;
                
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        
    }
    
    public void pauseApp() {
        stateTracker = PAUSED;
        core.Paused();
        notifyPaused();
    }

    public void destroyApp(boolean unconditional)  { performDestroyCleanup(); }
    
    public void destroyApp() { performDestroyCleanup(); }

    private void performDestroyCleanup() {
       if(stateTracker != DESTROYED)
       {
           stateTracker = DESTROYED;
           notifyDestroyed();
       }
    }

    public void commandAction(Command c, Displayable d) {
        if(c.getCommandType() == Command.EXIT){destroyApp();}
    }
}
