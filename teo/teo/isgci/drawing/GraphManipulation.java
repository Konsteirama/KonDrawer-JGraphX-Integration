package teo.isgci.drawing;

import java.awt.Color;
import java.awt.Point;

import com.mxgraph.layout.hierarchical.mxHierarchicalLayout;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.util.mxConstants;
import com.mxgraph.util.mxEvent;
import com.mxgraph.util.mxEventObject;
import com.mxgraph.util.mxUtils;
import com.mxgraph.util.mxEventSource.mxIEventListener;
import com.mxgraph.util.mxUndoManager;
import com.mxgraph.util.mxUndoableEdit;
import com.mxgraph.view.mxGraph;

import org.jgrapht.Graph;

import javax.swing.JScrollBar;

/**
 * Dumbed down version of the original, WIP GraphEventInterface.
 * TODO: replace this with the final one
 * @param <V>
 * @param <E>
 */
class GraphManipulation<V, E> implements GraphManipulationInterface<V, E> {

    /**
     * Adapter holding the current graph in JgraphX and JGraphT data structure.
     */
    private JGraphXAdapter<V, E> graphAdapter;

    /**
     * GraphComponent is the panel the graph is drawn in.
     */
    private mxGraphComponent graphComponent;

    /**
     * Manages the undo-operations on the calling graph.
     */
    private mxUndoManager undoManager;

    /**
     * Constructor of the class. Creates an instance of the GraphManipulation
     * class that operates on a given graphComponent.
     *
     * @param graphComponent : a JGRaphX graphComponent, shown on the panel
     * @param graphXAdapter  :
     */
    public GraphManipulation(mxGraphComponent graphComponent,
                             JGraphXAdapter<V, E> graphXAdapter) {
        this.graphComponent = graphComponent;
        this.graphAdapter = graphXAdapter;

        // initiation of undoManager variable
        this.undoManager = new mxUndoManager();

        //notify undoManager about edits
        graphComponent.getGraph().getModel().
                    addListener(mxEvent.UNDO, undoHandler);
        graphComponent.getGraph().getView().
                    addListener(mxEvent.UNDO, undoHandler);
    }

    protected mxIEventListener undoHandler = new mxIEventListener() {
        public void invoke(Object source, mxEventObject evt) {
            undoManager.undoableEditHappened((mxUndoableEdit) evt
                    .getProperty("edit"));
        }
    };

    /**
     * Returns a boolean denoting whether the calling graph is able to perform
     * a redo-operation.
     *
     * @return if false then there was no undoable action performed earlier
     */
    @Override
    public boolean canRedo() {
        return undoManager.canRedo();
    }

    /**
     * Returns a boolean denoting whether the calling graph is able to perform
     * an undo-operation.
     *
     * @return if true then there is an action that can be undone
     */
    @Override
    public boolean canUndo() {
        return undoManager.canUndo();
    }

    /**
     * Centers the view of the panel on the selected node.
     *
     * @param node : a node of the graph
     */
    @Override
    public void centerNode(V node) {
        mxGraph graph = graphComponent.getGraph();

        graph.getModel().beginUpdate();

        graphComponent.scrollCellToVisible(node, true);

        graph.getModel().endUpdate();
    }

    /**
     * Colors a given node in a given color.
     *
     * @param nodes : an array of nodes of the graph
     * @param color : a color-parameter
     */
    @Override
    public void colorNode(V[] nodes, Color color) {
        mxGraph graph = graphComponent.getGraph();
        graph.setCellStyles(mxConstants.STYLE_FILLCOLOR,
                mxUtils.hexString(color), nodes);
    }

    /**
     * Marks the edge between two given nodes by adding a small grey arrow and
     * coloring the edge.
     *
     * @param edges : an array of edges of the graph
     */
    @Override
    public void markEdge(E[] edges) {
        mxGraph graph = graphComponent.getGraph();        
        graph.setCellStyles(mxConstants.STYLE_STROKECOLOR,
                mxUtils.hexString(Color.black), edges);
    }
    
    /**
     * Unmarks the edge between two given nodes by removing
     * the small grey arrow and uncoloring the edge.
     *
     * @param edges : an array of edges of the graph
     */
    @Override
    public void unmarkEdge(E[] edges) {
        mxGraph graph = graphComponent.getGraph();        
        graph.setCellStyles(mxConstants.STYLE_STROKECOLOR,
                mxUtils.hexString(Color.blue), edges);
    }

    /**
     * Gives a hierarchical order to the displayed graph.
     */
    @Override
    public void reapplyHierarchicalLayout() {
        mxGraph graph = graphComponent.getGraph();

        graph.getModel().beginUpdate();

        mxHierarchicalLayout layout = new mxHierarchicalLayout(graph);
        layout.execute(graph.getDefaultParent());

        graph.getModel().endUpdate();
    }

    /**
     * Redoes a previously undone action on the graph.
     */
    @Override
    public void redo() {
        undoManager.redo();
    }

    /**
     * Removes the given node from the graph. Removal only effects the
     * JGraphX-graph.
     *
     * @param node : a JGraphX-graph node object
     */
    @Override
    public void removeNode(V node) {
        mxGraph graph = graphComponent.getGraph();

        Object[] cells = new Object[1];
        cells[0] = node;

        // Adds all edges connected to the node
        cells = graph.addAllEdges(cells);
        
        graph.getModel().beginUpdate();
        
        // Deletes every cell
        for (Object object : cells) {
            graph.getModel().remove(object);
        }
        
        graph.getModel().endUpdate();
    }

    /**
     * Alters the attribute name of a given node by replacing it by a given new
     * name. Renaming only effects the JGraphX-graph.
     *
     * @param node    : a node of the graph
     * @param newName : the name the node is given
     */
    @Override
    public void renameNode(V node, String newName) {
        mxGraph graph = graphComponent.getGraph();

        String nodeName = newName;

        graph.getModel().beginUpdate();

        graphComponent.labelChanged(node, nodeName, null);

        graph.getModel().endUpdate();
    }

    /**
     * Resets the layout of the JGrapgX-graph on the panel to the original
     * JGraphT-graph.
     */
    @Override
    public void resetLayout() {
        Graph<V, E> graphT = graphAdapter.getGraph();

        JGraphXAdapter<V, E> newGraphAdapter = new JGraphXAdapter<V, E>(graphT);
    }

    /**
     * Undoes a previously performed action on the graph.
     */
    @Override
    public void undo() {
        undoManager.undo();
    }
    
    /**
     * Zooms the panel to the given factor. It will magnify the graph, if the
     * graph is too big for the panel only a section of the whole graph will be
     * shown. This method zooms to the center of the panel.
     *
     * @param factor : a double that represents the zoom factor
     *                  (ranges from 0 to infinite, 1 is 100%)
     */
    @Override
    public void zoomTo(double factor) {
        graphComponent.zoomTo(factor, true);
    }

    /**
     * Zooms the panel. It will magnify the graph, if the
     * graph is too big for the panel only a section of the whole graph will be
     * shown. This method zooms to the center of the panel.
     *
     * @param zoomIn : a boolean to zoom in or out
     */
    @Override
    public void zoom(boolean zoomIn) {
        if (!graphComponent.isCenterZoom()) {
            graphComponent.setCenterZoom(true);
        }

        // factor isn't a good measure ask if it could be changed
        if (zoomIn) {
            graphComponent.zoomIn();
        } else {
            graphComponent.zoomOut();
        }
    }

    /**
     * Zooms the panel, centering on the given coordinates.
     *
     * @param zoomIn : a boolean to zoom in or out
     * @param center : the point zoom centers on
     */
    @Override
    public void zoom(boolean zoomIn, Point center) {
        zoom(zoomIn);
        
        // Get the horizontal and vertical Scrollbar
        JScrollBar horScrollBar = graphComponent.getHorizontalScrollBar(); 
        JScrollBar vertScrollBar = graphComponent.getVerticalScrollBar();  
        
        // Get the relative position of the mousepointer in x and y
        double relPosX = center.getX() / graphComponent.getWidth();
        double relPosY = center.getY() / graphComponent.getHeight();
        
        // Zoom to the relative x and y coordinates
        horScrollBar.setValue((int) (relPosX * horScrollBar.getMaximum()));
        vertScrollBar.setValue((int) (relPosY * vertScrollBar.getMaximum()));
    }
}
