/*
 * TODO Replace this line with a (multi-line) description of this file...
 *
 * $Header$
 *
 * This file is part of the Information System on Graph Classes and their
 * Inclusions (ISGCI) at http://www.graphclasses.org.
 * Email: isgci@graphclasses.org
 */

package teo.isgci.gui;

import javax.swing.JButton;
import javax.swing.JToolBar;

public class ISGCIToolBar extends JToolBar {

    public ISGCIToolBar() {
        // set basic layout
        setFloatable(false);
        setRollover(true);

        // add some button
        JButton button = new JButton("Hello, world!");
        add(button);
    }
}

/* EOF */