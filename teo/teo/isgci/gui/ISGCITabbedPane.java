/*
 * The ISGCI specific implementation of the tabbedpane. Will create a
 * startpage upon creation. Tabs can (and should be created) via 
 * the {@link #addTab(Graph)} method, because it closes the startpage
 * and draws a new graph - which this class should be used for.
 *
 * $Header$
 *
 * This file is part of the Information System on Graph Classes and their
 * Inclusions (ISGCI) at http://www.graphclasses.org.
 * Email: isgci@graphclasses.org
 */

package teo.isgci.gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultEdge;

import teo.isgci.db.Algo;
import teo.isgci.db.Algo.NamePref;
import teo.isgci.db.DataSet;
import teo.isgci.drawing.DrawingLibraryFactory;
import teo.isgci.drawing.DrawingLibraryInterface;
import teo.isgci.gc.GraphClass;
import teo.isgci.grapht.GAlg;
import teo.isgci.grapht.Inclusion;
import teo.isgci.problem.Complexity;
import teo.isgci.problem.Problem;
import teo.isgci.util.Updatable;
import teo.isgci.util.UserSettings;

/**
 * The ISGCI specific implementation of the tabbedpane. Will create a
 * startpage upon creation. Tabs can (and should be created) via 
 * the {@link #addTab(Graph)} method, because it closes the startpage
 * and draws a new graph - which this class should be used for.
 */
public class ISGCITabbedPane extends JTabbedPane implements Updatable {
    
    /**
     * The version of this class. Should be changed every time
     * this class is modified.
     */
    private static final long serialVersionUID = 3L;

    /**
     * Indicates whether the startpage is currently a tab in the tabbedpane.
     */
    private boolean startpageActive = false;
    
    /**
     * The startpage that is displayed upon start of the window. 
     */
    private StartPanel startpage;

    /**
     * Maps the content of the tabs to their corresponding
     * DrawingLibraryInterface.
     */
    private HashMap<JComponent, DrawingLibraryInterface> panelToInterfaceMap
        = new HashMap<JComponent, DrawingLibraryInterface>();
    
    /**
     * Maps the tab to their corresponding drawUnproper state.
     */
    private HashMap<JComponent, Boolean> panelToDrawUnproper
        = new HashMap<JComponent, Boolean>();
    
    /**
     * Maps the tab to their corresponding Problem.
     */
    private HashMap<JComponent, Problem> panelToProblem
        = new HashMap<JComponent, Problem>();
    
    /**
     * Maps the content of the tabs to their corresponding
     * naming preference.
     */
    private HashMap<JComponent, Algo.NamePref> panelToNamingPref
        = new HashMap<JComponent, Algo.NamePref>();

    
    /**
     * The mode which indicates the default preferred name of a Node.
     */
    private Algo.NamePref defaultMode = UserSettings.getDefaultNamingPref();
    

    /**
     * A popup menu which is shown after right-clicking a node.
     */
    private NodePopup nodePopup;

    /**
     * A popup menu which is shown after right-clicking an edge.
     */
    private EdgePopup edgePopup;
    
    
    /**
     * The reference to the mainframe that contains this panel.
     */
    private ISGCIMainFrame mainframe;
    
    /**
     * A listener which triggers if a tab is changed and then adjusts the
     * state of the drawUnproper menu item in the mainframe to match the
     * drawUnproper state of the tab.
     */
    private ChangeListener changeListener = new ChangeListener() {
        public void stateChanged(ChangeEvent changeEvent) {

            if (!startpageActive && getSelectedIndex() >= 0) {
                mainframe.setDrawUnproper(
                        getDrawUnproper(getSelectedComponent()));
            }
        }
    };
    
    /**
     * A mouse listener.
     * On right-click it opens a popup window if the clicked object is a node
     * or a edge.
     */
    private MouseAdapter mouseAdapter = new MouseAdapter() {        
        public void mouseClicked(MouseEvent e) {  
            
            Object node = getActiveDrawingLibraryInterface().
                    getNodeAt(e.getPoint());
            Object edge = getActiveDrawingLibraryInterface().
                    getEdgeAt(e.getPoint());
            
            Container parent = getParent();

            while (parent != null
                    && !(parent instanceof ISGCIMainFrame)) {
                parent = parent.getParent();
            }    
            
            // Right-click event
            if (e.getButton() == MouseEvent.BUTTON3 
                    && getActiveDrawingLibraryInterface() != null) {
                if (node != null) {
                    nodePopup = new NodePopup((ISGCIMainFrame) parent);
                    nodePopup.setNode((Set<GraphClass>) node);
                    nodePopup.show(e.getComponent(), e.getX(), e.getY());
                } else if (edge != null){
                    edgePopup = new EdgePopup((ISGCIMainFrame) parent);
                    edgePopup.setEdge((DefaultEdge) edge);
                    edgePopup.show(e.getComponent(), e.getX(), e.getY());
                }
            // Double-click event
            } else if (e.getClickCount() == 2)  { 
                JDialog d = new GraphClassInformationDialog(
                        (ISGCIMainFrame) parent
                        , ((Set<GraphClass>) node).iterator().next());
                d.setLocation(50, 50);
                d.pack();
                d.setSize(800, 600);
                d.setVisible(true);
            }
        }
    };
    
    /**
     * Creates a new Tabbed pane with a startpage as only active tab.
     * @param parent
     *          The reference to the ISGCIMainFrame that contains this pane.
     */
    public ISGCITabbedPane(ISGCIMainFrame parent) {
        mainframe = parent;
        addStartpage();
        addMouseListener(mouseAdapter);
        addChangeListener(changeListener);
        UserSettings.subscribeToOptionChanges(this);
    }

    /**
     * Adds a Startpage to the ISGCITabbedPane. Should only be called in the
     * Constructor.
     */
    public void addStartpage() {
        if (startpageActive) {
            return;
        }
        
        startpageActive = true;
        
        startpage = new StartPanel(mainframe);
        addTab("", startpage);
        setSelectedComponent(startpage);
        ISGCITabComponent closeButton 
            = new ISGCITabComponent(this, "Welcome to ISGCI");
        
        setTabComponentAt(getSelectedIndex(), closeButton);
      
    }

    /**
     * Removes the startpage from the ISGCITabbedPane.
     */
    public void removeStartpage() {
        remove(startpage);
        startpageActive = false;
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
     * @param name
     *          The name of the Tab
     */
    public <V, E> void drawInNewTab(Graph<V, E> graph, String name) {
        if (startpageActive) {
            removeStartpage();
        }
        
        DrawingLibraryInterface<V, E> drawingInterface = 
                DrawingLibraryFactory.createNewInterface(graph);

        JComponent panel = drawingInterface.getPanel();
        addTab("", panel);
        ISGCITabComponent tabComponent = new ISGCITabComponent(this, name);
        
        // set tabcomponent as .. tabcomponent
        setSelectedComponent(panel);
        setTabComponentAt(getSelectedIndex(), tabComponent);
        
        drawingInterface.getGraphEventInterface().
                registerMouseAdapter(mouseAdapter);
         
        panelToInterfaceMap.put(panel, drawingInterface);
        panelToNamingPref.put(panel, defaultMode);
        
        setProperness();
        setProblem(null, panel);
        applyNamingPref(panel);
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
     *          
     * @param name
     *          The name of the Tab
     */
    public <V, E> void drawInActiveTab(Graph<V, E> graph, String name) {
        if (startpageActive || getSelectedComponent() == null) {
            drawInNewTab(graph, name);
        } else {
            getActiveDrawingLibraryInterface().setGraph(graph);
            //reapply properness and coloring
            setProperness();
            setProblem(getProblem(getSelectedComponent())
                    , getSelectedComponent());
            
            // set title
            ISGCITabComponent closeButton 
                = new ISGCITabComponent(this, name);
        
            setTabComponentAt(getSelectedIndex(), closeButton);
        }
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
        if (panel == startpage) {
            return startpage.getDrawingLibraryInterface();
        }
        
        if (!panelToInterfaceMap.containsKey(panel)) {
            return null;
        }
        
        return panelToInterfaceMap.get(panel);
    }

    /**
     * 
     * @return 
     *          The default naming preference of this tabbed pane.
     */
    public NamePref getNamingPref() {
        return defaultMode;
    }

    /**
     * Changes the default naming preference of this tabbed pane.
     * 
     * @param pref
     *          The new default naming preference of this tabbed pane.
     */
    public void setNamingPref(NamePref pref) {
        this.defaultMode = pref;
        for (Component tab : panelToNamingPref.keySet()) {
            setNamingPref(pref, tab);
        }
    }
    
    /**
     * @param c 
     *          The tab for which the naming preference is wanted.
     * 
     * @return 
     *          The default naming preference of the given Tab.
     */
    public NamePref getNamingPref(Component c) {
        if (panelToNamingPref.containsKey(c)) {
            return panelToNamingPref.get(c);
        } else {
            return defaultMode;
        }
    }

    /**
     * Changes the naming preferences of target tab.
     * 
     * @param pref
     *          The new default naming preference of this tab.
     * @param c
     *          The Tab for which the naming preference is changed.
     */
    public void setNamingPref(NamePref pref, Component c) {
        panelToNamingPref.put((JComponent) c, pref);
        applyNamingPref(c);
    }
    
    /**
     * Applies the naming preference of a given tab on each of its nodes.
     * 
     * @param c
     *         the tab on which the nodes will be renamed
     */
    private void applyNamingPref(Component c) {
        DrawingLibraryInterface graphInterface = panelToInterfaceMap.get(c);
        Graph graph = graphInterface.getGraph();
        Algo.NamePref namePref = panelToNamingPref.get(c);
        for (Object node : graph.vertexSet()) {
            String newName = Algo.getName((Set<GraphClass>) node, namePref);
            graphInterface.getGraphManipulationInterface()
                .renameNode(node, newName);
        }
    }

    /**
     * Sets the drawUnproper value for the currently open tab.
     * 
     * @param c
     *          The Tab for which the drawUnproper state is changed.
     * @param state
     *          the new drawUnproper state of the open tab
     */
    public void setDrawUnproper(boolean state, Component c) {
        panelToDrawUnproper.put((JComponent) getSelectedComponent(), state);
        setProperness();
    }
    
    /**
     * @param c
     *          The tab for which the drawUnproper state is wanted.
     * @return
     *          true if unproper inclusions shall be drawn, else false.
     */
    public boolean getDrawUnproper(Component c) {
        if (!panelToDrawUnproper.containsKey(getSelectedComponent())) {
            return true;
        } else {
            return (panelToDrawUnproper.get(getSelectedComponent()));
        }
    }

    /**
     * Sets the problem for a given tab. The problem of a tab determines the
     * coloring of the graph on it.
     * 
     * @param problem
     *          the new problem of the tab.
     *          
     * @param c
     *          the tab for which the problem is changed.
     */
    public void setProblem(Problem problem, Component c) {
        if (startpageActive) { return; }
        panelToProblem.put((JComponent) c, problem);
        Graph graph = getActiveDrawingLibraryInterface().getGraph();
        HashMap<Color , List<Set<GraphClass>>> colorToNodes = 
                new HashMap<Color, List<Set<GraphClass>>>();
        // Put all nodes with the same color in a list
        for (Object o : graph.vertexSet()) {
            Set<GraphClass> node = (Set<GraphClass>) o;
            Color color = complexityColor(node);
            if (!colorToNodes.containsKey(color)) {
                colorToNodes.put(color, new ArrayList<Set<GraphClass>>());
            }
            colorToNodes.get(color).add(node);           
        }
        // Color all lists with their color
        for (Color color : colorToNodes.keySet()) {
            getActiveDrawingLibraryInterface().getGraphManipulationInterface()
                .colorNode(colorToNodes.get(color).toArray(), color);
        }
        getSelectedComponent().repaint();
    }
    
    /**
     * Gives the problem for a given tab. The problem of a tab determines the
     * coloring of the graph on it.
     * 
     * @param c
     *          the tab for which the problem is wanted
     *          
     * @return
     *          the problem of the given tab, 
     *          null or "none" if no problem is chosen
     */
    public Problem getProblem(Component c) {
        if (panelToProblem.containsKey(c)) {
            return panelToProblem.get(c);
        }
        return null;
    }

    /**
     * Returns the properness of a given edge, in a given graph.
     * 
     * @param graph
     *          the graph in which the edge is in
     * 
     * @param edge
     *          the edge which is checked for properness.
     *          
     * @return
     *          true if the edge is proper, false otherwise.
     */
    private boolean getProperness(Graph graph, DefaultEdge edge){
        GraphClass source = (GraphClass) ((Set<GraphClass>) 
                            graph.getEdgeSource(edge)).iterator().next();
        GraphClass target = (GraphClass) ((Set<GraphClass>) 
                            graph.getEdgeTarget(edge)).iterator().next();
        List<Inclusion> path = GAlg.getPath(DataSet.inclGraph, source, target);
        return (Algo.isPathProper(path)  
                || Algo.isPathProper(Algo.makePathProper(path)));
    }
    
    /**
     * Marks edges of the currently active tab, depending on it's drawUnproper
     * state.
     */
    private void setProperness() {
        Graph graph = getActiveDrawingLibraryInterface().getGraph();
        List<DefaultEdge> markEdges = new ArrayList<DefaultEdge>();
        List<DefaultEdge> unmarkEdges = new ArrayList<DefaultEdge>();
        for (Object o : graph.edgeSet()) {
            DefaultEdge edge = (DefaultEdge) o;
            boolean isProper = getProperness(graph, edge);
            if (!isProper && getDrawUnproper(getSelectedComponent())) {
                markEdges.add(edge);
            } else if (!isProper 
                        && !getDrawUnproper(getSelectedComponent())){
                unmarkEdges.add(edge);
            }
        }
        if (markEdges.size() > 0){
            getActiveDrawingLibraryInterface().
            getGraphManipulationInterface().markEdge(markEdges.toArray());
        } else if (unmarkEdges.size() > 0) {
            getActiveDrawingLibraryInterface().
            getGraphManipulationInterface().unmarkEdge(unmarkEdges.toArray());
        } 
    }
    
    /**
     * @param node 
     *          the node for which the color is returned.
     * 
     * @return 
     *          the color for node considering its complexity for the active
     *          problem.
     */
    protected Color complexityColor(Set<GraphClass> node) {
        Problem problem = getProblem(getSelectedComponent());
        if (problem != null){
            Complexity complexity = 
                    problem.getComplexity(node.iterator().next());
            return UserSettings.getColor(complexity);
        }
        return UserSettings.getColor(Complexity.UNKNOWN);
    }

    @Override
    public void updateOptions() {
        // Reapply color, to change all nodes to the new color scheme
        for (Component tab : this.getComponents()) {
            setProblem(getProblem(tab), tab);
        }
        
        setTabPlacement(UserSettings.getCurrentTabPlacement());
        
        // TODO jannis throws error?
        //setNamingPref(UserSettings.getDefaultNamingPref());
        //setNamingPref(UserSettings.getDefaultNamingPref(), 
        //        getSelectedComponent());
    }
}

/* EOF */
