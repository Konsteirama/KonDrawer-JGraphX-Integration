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

import javax.swing.JPanel;
import javax.swing.JTabbedPane;

public class ISGCITabbedPane extends JTabbedPane {

    private boolean startpageActive = false;
    private JPanel startpage;
	
    public ISGCITabbedPane() {
    	addStartpage();
    }
    
    /**
     * Adds a Startpage to the ISGCITabbedPane.
     * Should only be called in the Constructor.
     */
    private void addStartpage(){
    	if(startpageActive) return;
    	startpageActive = true;
    	startpage = new JPanel();
        addTab("Welcome", startpage);     	
    }
    
    /**
     * Removes the startpage from the ISGCITabbedPane.
     */
    public void removeStartpage(){
    	remove(startpage);
    }
    
    /**
     * @return true if the startpage is active, else false
     */
    public boolean startpageIsActive(){
    	return startpageActive;
    }
}

/* EOF */
