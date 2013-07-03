/*
 * A lightweight latex label.
 *
 * $Header$
 *
 * This file is part of the Information System on Graph Classes and their
 * Inclusions (ISGCI) at http://www.graphclasses.org.
 * Email: isgci@graphclasses.org
 */

package teo.isgci.drawing;

import java.awt.Font;
import java.awt.Rectangle;

import com.mxgraph.util.mxConstants;
import javax.swing.SwingConstants;
import teo.isgci.gui.LatexLabel;

/**
 * A lightweight latex label.
 */
public class LightweightLatexLabel extends LatexLabel {

    /** Serial version. */
    private static final long serialVersionUID = 1L;
    
    
    /**
     * The shared instance of this object.
     */
    protected static LightweightLatexLabel sharedInstance;

    /**
     * Initializes the shared instance.
     */
    static {
        try {
            sharedInstance = new LightweightLatexLabel();
        } catch (Exception e) {
            System.err.println("Failed to create lightweightlatexlabel!");
        }
    }

    /**
     *
     *
     */
    public LightweightLatexLabel() {
        setFont(new Font(mxConstants.DEFAULT_FONTFAMILY, 0,
                mxConstants.DEFAULT_FONTSIZE));
        setVerticalAlignment(SwingConstants.TOP);
    }

    /**
     * Returns the shared instance of this label.
     * @return The shared instance
     */
    public static LightweightLatexLabel getSharedInstance() {
        return sharedInstance;
    }

    
    /* Methods are overridden for performance reasons. */
    
    @Override
    public void firePropertyChange(String propertyName, byte oldValue,
                                   byte newValue) { }

    @Override
    public void firePropertyChange(String propertyName, char oldValue,
                                   char newValue) { }

    @Override
    public void firePropertyChange(String propertyName, short oldValue,
                                   short newValue) { }

    @Override
    public void firePropertyChange(String propertyName, int oldValue,
                                   int newValue) { }

    @Override
    public void firePropertyChange(String propertyName, long oldValue,
                                   long newValue) { }

    @Override
    public void firePropertyChange(String propertyName, float oldValue,
                                   float newValue) { }

    @Override
    public void firePropertyChange(String propertyName, double oldValue,
                                   double newValue) { }

    @Override
    public void firePropertyChange(String propertyName, boolean oldValue,
                                   boolean newValue) { }

    @Override
    public void repaint(long tm, int x, int y, int width, int height) {
    }

    @Override
    public void repaint(Rectangle r) { }

    @Override
    public void revalidate() { }
    
    @Override
    public void validate() { }

    @Override
    protected void firePropertyChange(String propertyName, Object oldValue,
                                      Object newValue) {
        // Strings get interned...
        if (propertyName == "text" || propertyName == "font") {
            super.firePropertyChange(propertyName, oldValue, newValue);
        }
    }

}

/* EOF */
