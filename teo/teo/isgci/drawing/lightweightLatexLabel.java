package teo.isgci.drawing;

import com.mxgraph.util.mxConstants;
import teo.isgci.gui.LatexLabel;

import javax.swing.*;
import java.awt.*;

/**
 * A lightweight latex label
 */
public class lightweightLatexLabel extends LatexLabel {

    /**
     *
     */
    protected static lightweightLatexLabel sharedInstance;

    /**
     * Initializes the shared instance.
     */
    static {
        try {
            sharedInstance = new lightweightLatexLabel();
        } catch (Exception e) {
            // ignore
        }
    }

    /**
     *
     *
     */
    public lightweightLatexLabel() {
        setFont(new Font(mxConstants.DEFAULT_FONTFAMILY, 0,
                mxConstants.DEFAULT_FONTSIZE));
        setVerticalAlignment(SwingConstants.TOP);
    }

    /**
     *
     */
    public static lightweightLatexLabel getSharedInstance() {
        return sharedInstance;
    }

    /**
     * Overridden for performance reasons.
     */
    public void validate() {
    }

    /**
     * Overridden for performance reasons.
     */
    public void revalidate() {
    }

    /**
     * Overridden for performance reasons.
     */
    public void repaint(long tm, int x, int y, int width, int height) {
    }

    /**
     * Overridden for performance reasons.
     */
    public void repaint(Rectangle r) {
    }

    /**
     * Overridden for performance reasons.
     */
    protected void firePropertyChange(String propertyName, Object oldValue,
                                      Object newValue) {
        // Strings get interned...
        if (propertyName == "text" || propertyName == "font") {
            super.firePropertyChange(propertyName, oldValue, newValue);
        }
    }

    /**
     * Overridden for performance reasons.
     */
    public void firePropertyChange(String propertyName, byte oldValue,
                                   byte newValue) {
    }

    /**
     * Overridden for performance reasons.
     */
    public void firePropertyChange(String propertyName, char oldValue,
                                   char newValue) {
    }

    /**
     * Overridden for performance reasons.
     */
    public void firePropertyChange(String propertyName, short oldValue,
                                   short newValue) {
    }

    /**
     * Overridden for performance reasons.
     */
    public void firePropertyChange(String propertyName, int oldValue,
                                   int newValue) {
    }

    /**
     * Overridden for performance reasons.
     */
    public void firePropertyChange(String propertyName, long oldValue,
                                   long newValue) {
    }

    /**
     * Overridden for performance reasons.
     */
    public void firePropertyChange(String propertyName, float oldValue,
                                   float newValue) {
    }

    /**
     * Overridden for performance reasons.
     */
    public void firePropertyChange(String propertyName, double oldValue,
                                   double newValue) {
    }

    /**
     * Overridden for performance reasons.
     */
    public void firePropertyChange(String propertyName, boolean oldValue,
                                   boolean newValue) {
    }

}
