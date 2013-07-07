/*
 * A Label that can contain latex.
 *
 * $Header: /home/ux/CVSROOT/teo/teo/isgci/gui/LatexLabel.java,v 2.0 2011/09/25 12:37:13 ux Exp $
 *
 * This file is part of the Information System on Graph Classes and their
 * Inclusions (ISGCI) at http://www.graphclasses.org.
 * Email: isgci@graphclasses.org
 */

package teo.isgci.gui;

import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.JLabel;

/**
 * A Label that can depict a subset of latex. drawLatexString can also
 * be called from other classes.
 */
public class LatexLabel extends JLabel {   

    /** Serial version. */
    private static final long serialVersionUID = 1422815423371691395L;

    /** Contents of this label. */
    private String content;

    /** Alignment of the label. */
    private int align;
    //private int border;


    /**
     * Creates a new label without text.
     */
    public LatexLabel() {
        this("", JLabel.LEFT);
    }

    /**
     * Creates a new label.
     *
     * @param text the contents of the label
     */
    public LatexLabel(String text) {
        this(text, JLabel.LEFT);
    }

    /**
     * Creates a new label.
     *
     * @param text contents of the label
     * @param alignment alignment of the label (only LEFT supported )
     */
    public LatexLabel(String text, int alignment) {
        this.content = text;
        align = alignment;
        setBorder(BorderFactory.createEmptyBorder(3, 0, 3, 0));
        //border = 5;
    }

    /**
     * Paints the label.
     *
     * @param graphics the graphics context
     */
    protected void paintComponent(Graphics graphics) {
        Graphics g = graphics.create();
        // Get component width and height
        int width = getSize().width;
        int height = getSize().height;

        // Clear screen
        g.setColor(getBackground());
        g.fillRect(0, 0, width, height);

        // Set current component color and font
        g.setColor(getForeground());
        g.setFont(getFont());

        // Get the metrics of the current font
        FontMetrics fm = g.getFontMetrics();
        Insets insets = getInsets();

        drawLatexString(g, content, insets.left, //border,
                insets.top /*border*/ + fm.getLeading() + fm.getMaxAscent());
    }

    /**
     * Changes the text of the label.
     *
     * @param text the new text
     */
    public void setText(String text) {
        this.content = text;
        repaint();
    }

    /**
     * Returns the contents of the label.
     * @return the contents of the label
     */
    public String getText() {
        return content;
    }

    /**
     * Changes the alignment of the label (only LEFT supported).
     *
     * @param alignment the new alignment
     */
    public void setAlignment(int alignment) {
        align = alignment;
    }

    /**
     * Returns the alignment of the label.
     * @return alignment
     */
    public int getAlignment() {
        return align;
    }

    @Override
    public Dimension getPreferredSize() {
        if (getFont() == null) {
            return getSize();
        }

        Insets insets = getInsets();
        Graphics g;
        if (getGraphics() == null) {
            g = new NulGraphics();
        } else {
            g = getGraphics().create();
        }
        g.setFont(getFont());
        FontMetrics fm = g.getFontMetrics();
        
        final double paddedWidth = LatexGraphics.getInstance().getLatexWidth(
                g, content) * 1.1;
        
        Dimension d = new Dimension(
                insets.left + insets.right + (int) paddedWidth,
                insets.top + insets.bottom + fm.getHeight());
        g.dispose();
        return d;
    }

    @Override
    public Dimension getMinimumSize() {
        return getPreferredSize();
    }

    @Override
    public Dimension getMaximumSize() {
        return new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE);
    }


    /**
     * Paints a string that contains latexcodes and returns its width.
     * @param g the graphics context
     * @param str the string to draw
     * @param x where to paint
     * @param y where to paint
     * 
     * @return The width of the latexstring.
     */
    public int drawLatexString(Graphics g, String str, int x, int y) {
        return LatexGraphics.getInstance().drawLatexString(g, str, x, y);
    }


    /**
     * Returns the width of a latexstring.
     * @param str the string
     * @param g the graphics context
     * @return the resulting width
     */
    public int getLatexWidth(Graphics g, String str) {
        return LatexGraphics.getInstance().getLatexWidth(g, str);
    }


}

/* EOF */
