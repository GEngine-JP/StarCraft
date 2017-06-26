package core;

import java.awt.Color;
import java.awt.DisplayMode;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Toolkit;

import javax.swing.JComponent;
import javax.swing.JFrame;

/**
    Simple abstract class used for testing. Subclasses should
    implement the draw() method.
*/
public abstract class FullGameCore {

    protected static final int FONT_SIZE = 24;

    private static final DisplayMode POSSIBLE_MODES[] = {
        new DisplayMode(800, 600, 16, 0),
        new DisplayMode(800, 600, 32, 0),
        new DisplayMode(800, 600, 24, 0),
        new DisplayMode(640, 480, 16, 0),
        new DisplayMode(640, 480, 32, 0),
        new DisplayMode(640, 480, 24, 0),
        new DisplayMode(1024, 768, 16, 0),
        new DisplayMode(1024, 768, 32, 0),
        new DisplayMode(1024, 768, 24, 0),
    };

    private boolean isRunning;
    protected ScreenManager screen;

    /**
        Signals the game loop that it's time to quit
    */
    public void stop() {
        isRunning = false;
    }


    /**
        Calls init() and gameLoop()
    */
    public void run() {
        try {
            init();
            gameLoop();
        }
        finally {
            screen.restoreScreen();
            lazilyExit();
        }
    }


    /**
        Exits the VM from a daemon thread. The daemon thread waits
        2 seconds then calls System.exit(0). Since the VM should
        exit when only daemon threads are running, this makes sure
        System.exit(0) is only called if neccesary. It's neccesary
        if the Java Sound system is running.
    */
    public void lazilyExit() {
        Thread thread = new Thread() {
            public void run() {
                try {
                    Thread.sleep(2000);
                }
                catch (InterruptedException ex) { }
                System.exit(0);
            }
        };
        thread.setDaemon(true);
        thread.start();
    }


    /**
        Sets full screen mode and initiates and objects.
    */
    public void init() {
    	
        screen = new ScreenManager();
        DisplayMode displayMode = screen.findFirstCompatibleMode(POSSIBLE_MODES);
        screen.setFullScreen(displayMode);
        JFrame frame = screen.getFullScreenWindow();
        frame.setFont(new Font("Dialog", Font.PLAIN, FONT_SIZE));
        frame.setBackground(Color.white);
        frame.setForeground(Color.white);
        frame.setTitle("JStarCraft");
        frame.setIconImage(ResourceManager.loadImage("title.png"));
        isRunning = true;
        frame.setCursor(Toolkit.getDefaultToolkit().createCustomCursor(
				ResourceManager.loadImage("cur.png"), new Point(0, 0), "cur"));
    	NullRepaintManager.install();
		frame.setLayout(null);
		((JComponent) frame.getContentPane()).setOpaque(false);
		
    }



    /**
        Runs through the game loop until stop() is called.
    */
    public void gameLoop() {
        long startTime = System.currentTimeMillis();
        long currTime = startTime;

        while (isRunning) {
            long elapsedTime =
                System.currentTimeMillis() - currTime;
            currTime += elapsedTime;

            // update
            update(elapsedTime);

            // draw the screen
            Graphics2D g = screen.getGraphics();
            draw(g);
            g.dispose();
            screen.update();

//             don't take a nap! run as fast as possible
            try {
                Thread.sleep(5);
            }
            catch (InterruptedException ex) { }
        }
    }


    /**
        Updates the state of the game/animation based on the
        amount of elapsed time that has passed.
    */
    public void update(long elapsedTime) {
        // do nothing
    }


    /**
        Draws to the screen. Subclasses must override this
        method.
    */
    public abstract void draw(Graphics2D g);
    
    public int getHeight(){
    	
    	return  screen.getFullScreenWindow().getHeight();
    
    }
    
    public int getWidth(){
    	
    	return  screen.getFullScreenWindow().getWidth();
    	
    }
    
   public JFrame getWindow(){
	   return screen.getFullScreenWindow();
   }
}
