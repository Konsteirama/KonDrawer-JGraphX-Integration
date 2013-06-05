/*
 * The ISGCI specific implementation of the tabbedpane. Will create a
 * startpage upon creation. Tabs can (and should be created) via 
 * the {link {@link #addTab(Graph)} method, because it closes the startpage
 * and draws a new graph - which this class should be used for.
 *
 * $Header$
 *
 * This file is part of the Information System on Graph Classes and their
 * Inclusions (ISGCI) at http://www.graphclasses.org.
 * Email: isgci@graphclasses.org
 */

package teo.isgci.gui;

import java.awt.Component;
import java.util.HashMap;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import org.jgrapht.Graph;

import teo.isgci.drawing.JGraphXInterface;
import teo.isgci.drawing.DrawingLibraryInterface;

/**
 * The ISGCI specific implementation of the tabbedpane. Will create a
 * startpage upon creation. Tabs can (and should be created) via 
 * the {link {@link #addTab(Graph)} method, because it closes the startpage
 * and draws a new graph - which this class should be used for.
 */
public class ISGCITabbedPane extends JTabbedPane {

    /**
     * The version of this class. Should be changed every time
     * this class is modified.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Indicates whether the startpage is currently a tab in the tabbedpane.
     */
    private boolean startpageActive = false;
    
    /**
     * The startpage that is displayed upon start of the window. 
     */
    private JPanel startpage;

    /**
     * Maps the content of the tabs to their corresponding
     * DrawingLibraryInterface.
     */
    private HashMap<JComponent, DrawingLibraryInterface> panelToInterfaceMap
        = new HashMap<JComponent, DrawingLibraryInterface>();
    
    
    /**
     * Creates a new Tabbed pane with a startpage as only active tab.
     */
    public ISGCITabbedPane() {
        addStartpage();
    }

    /**
     * Adds a Startpage to the ISGCITabbedPane. Should only be called in the
     * Constructor.
     */
    private void addStartpage() {
        if (startpageActive) {
            return;
        }
        startpageActive = true;
        startpage = new JPanel();
        addTab("Welcome to ISGCI", startpage);
    }

    /**
     * Removes the startpage from the ISGCITabbedPane.
     */
    public void removeStartpage() {
        remove(startpage);
    }
    
    /**
     * Adds a new tab with a graph that is drawn via the DrawingInterface.
     * Will close the startpage if it's still open.
     * @param <V>
     *          The class of the vertices.
     * @param <E>
     *          The class of the edges.
     * @param graph
     *          The graph that will be drawn and interacted with within 
     *          this tab.
     */
    public <V, E> void drawInNewTab(Graph<V, E> graph) {
        if (startpageActive) {
            removeStartpage();
        }
        
        // TODO jannis
        JGraphXInterface<V, E> graphXInterface 
        = new JGraphXInterface<V, E>(graph);

        JComponent panel = graphXInterface.getPanel();
        addTab("TODO", panel);
        
        // save context for later reference
        panelToInterfaceMap.put(panel, graphXInterface);
    }
    
    /**
     * Draws the graph in the currently active tab. If the startpage is still
     * active, the startpage will be closed and a new tab will be created
     * instead.
     * 
     * @param graph
     *          The graph that will be drawn.
     *          
     * @param <V>
     *          The class of the vertex.
     *          
     * @param <E>
     *          The class of the edge.
     */
    public <V, E> void drawInActiveTab(Graph<V, E> graph) {
        
        // TODO jannis
        
        
        
    }
    
    /**
     * Returns the active DrawingLibraryInterface.
     * 
     * @return
     *          The DrawingLibraryInterface that is associated with the 
     *          currently active tab.
     */
    public DrawingLibraryInterface getActiveDrawingLibraryInterface() {
        Component panel = getSelectedComponent();
        if (panel == startpage || !panelToInterfaceMap.containsKey(panel)) {
            return null; // TODO ?
        }
        
        return panelToInterfaceMap.get(panel);
    }
}

/* EOF */
