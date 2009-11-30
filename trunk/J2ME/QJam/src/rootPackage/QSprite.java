package rootPackage;

import java.io.IOException;
import java.io.InputStream;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.game.Sprite;

public class QSprite {
    
    public Image mainImg;
    public Sprite mainSprite;
    
    public QSprite(int x, int y, String path) throws IOException
    {
    	mainImg = getImage(path);
    	mainSprite = new Sprite(mainImg,mainImg.getWidth(),mainImg.getHeight());
    	mainSprite.setPosition(x, y);
    }
    public QSprite(int x, int y, int frameW, int frameH, String path) throws IOException
    {
    	mainImg = getImage(path);
    	mainSprite = new Sprite(mainImg,frameW,frameH);    	
    	mainSprite.setPosition(x, y);
    }
    
    public void Move(int dx, int dy) { mainSprite.move(dx, dy); }
    public void NextFrame() { mainSprite.nextFrame(); }
    public void SetPosition(int x, int y) { mainSprite.setPosition(x, y); }
    
    public void Draw(Graphics g) { mainSprite.paint(g); }
    
    public InputStream GetResourceFromStream(String path) { return getClass().getResourceAsStream(path); }
    
    public Image getImage(String path) throws IOException
    {        
        return Image.createImage(GetResourceFromStream(path));
    }
}
