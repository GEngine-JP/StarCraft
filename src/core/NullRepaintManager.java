package core;


import javax.swing.JComponent;
import javax.swing.RepaintManager;

/**
    The NullRepaintManager is a RepaintManager that doesn't
    do any repainting. Useful when all the rendering is done
    manually by the application.
*/
public class NullRepaintManager extends RepaintManager {

    /**
        Installs the NullRepaintManager.
    */
    public static void install() {
        RepaintManager repaintManager = new NullRepaintManager();
        repaintManager.setDoubleBufferingEnabled(false);
        RepaintManager.setCurrentManager(repaintManager);
    }

    public void addInvalidComponent(JComponent c) {
        // do nothing
    }

    public void addDirtyRegion(JComponent c, int x, int y,
        int w, int h)
    {
        // do nothing
    }

    public void markCompletelyDirty(JComponent c) {
        // do nothing
    }

    public void paintDirtyRegions() {
        // do nothing
    }

}
