package teo.isgci.drawing;

import java.awt.Color;
import java.awt.Point;
import java.awt.Rectangle;

import org.jgrapht.Graph;

import com.mxgraph.layout.hierarchical.mxHierarchicalLayout;
import com.mxgraph.model.mxICell;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.util.mxConstants;
import com.mxgraph.util.mxEvent;
import com.mxgraph.util.mxEventObject;
import com.mxgraph.util.mxEventSource.mxIEventListener;
import com.mxgraph.util.mxUndoManager;
import com.mxgraph.util.mxUndoableEdit;
import com.mxgraph.util.mxUtils;
import com.mxgraph.view.mxGraph;

/**
 * Dumbed down version of the original, WIP GraphEventInterface.
 * TODO: replace this with the final one
 * @param <V>
 * @param <E>
 */
class GraphManipulation<V, E> implements GraphManipulationInterface<V, E> {

    protected mxIEventListener undoHandler = new mxIEventListener() {
        public void invoke(Object source, mxEventObject evt) {
            undoManager.undoableEditHappened((mxUndoableEdit) evt
                    .getProperty("edit"));
        }
    };
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
     */
    public GraphManipulation(mxGraphComponent graphComponent) {
        this.graphComponent = graphComponent;

        // initiation of undoManager variable
        this.undoManager = new mxUndoManager();

        //notify undoManager about edits
        graphComponent.getGraph().getModel().
                addListener(mxEvent.UNDO, undoHandler);
        graphComponent.getGraph().getView().
                addListener(mxEvent.UNDO, undoHandler);
    }

    /**
     * Returns the current graph adapter
     *
     * @return The current graph adapter
     */
    private JGraphXAdapter<V, E> getGraphAdapter() {
        return (JGraphXAdapter<V, E>) graphComponent.getGraph();
    }

    /**
     * Returns the cell associated with the given node
     *
     * @param node
     * @return
     */
    private mxICell getCellFromNode(V node) {
        return getGraphAdapter().getVertexToCellMap().get(node);
    }

    /**
     * Returns the cell associated with the given edge
     *
     * @param edge
     * @return
     */
    private mxICell getCellFromEdge(E edge) {
        return getGraphAdapter().getEdgeToCellMap().get(edge);
    }

    /**
     * Returns the cells associated with the given nodes
     *
     * @param nodes
     * @return
     */
    private mxICell[] getCellsFromNodes(V[] nodes) {
        mxICell[] cells = new mxICell[nodes.length];
        for (int i = 0; i < nodes.length; i++) {
            cells[i] = getCellFromNode(nodes[i]);
        }
        return cells;
    }

    /**
     * Returns the cells associated with the given edges
     *
     * @param edges
     * @return
     */
    private mxICell[] getCellsFromEdges(E[] edges) {
        mxICell[] cells = new mxICell[edges.length];
        for (int i = 0; i < edges.length; i++) {
            cells[i] = getCellFromEdge(edges[i]);
        }
        return cells;
    }

    /**
     * Translates a point from graphComponent to graph
     *
     * @param p
     * @return
     */
    private Point getPointOnGraph(Point p) {
        mxGraph graph = graphComponent.getGraph();

        double s = graph.getView().getScale();
        Point tr = graph.getView().getTranslate().getPoint();

        double off = graph.getGridSize() / 2;
        double x = graph.snap(p.getX() / s - tr.getX() - off);
        double y = graph.snap(p.getY() / s - tr.getY() - off);

        return new Point((int) x, (int) y);
    }

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

        graphComponent.scrollCellToVisible(getCellFromNode(node), true);

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

        graph.getModel().beginUpdate();

        graph.setCellStyles(mxConstants.STYLE_FILLCOLOR,
                mxUtils.hexString(color), getCellsFromNodes(nodes));

        graph.getModel().endUpdate();
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

        graph.getModel().beginUpdate();

        graph.setCellStyles(mxConstants.STYLE_STROKECOLOR,
                mxUtils.hexString(Color.black), getCellsFromEdges(edges));

        graph.getModel().endUpdate();
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

        graph.getModel().beginUpdate();

        graph.setCellStyles(mxConstants.STYLE_STROKECOLOR,
                mxUtils.hexString(null), getCellsFromEdges(edges));

        graph.getModel().endUpdate();
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

        Object[] cells = new Object[]{getCellFromNode(node)};

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

        getCellFromNode(node).setValue(newName);

        graph.getModel().endUpdate();
    }

    /**
     * Resets the layout of the JGrapgX-graph on the panel to the original
     * JGraphT-graph.
     */
    @Override
    public void resetLayout() {
        Graph<V, E> graphT = getGraphAdapter().getGraph();

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
     *               (ranges from 0 to infinite, 1 is 100%)
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
        graphComponent.setCenterZoom(true);

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
        Point pointOnCanvas = getPointOnGraph(center);

        zoom(zoomIn);

        Point newPointOnCanvas = getPointOnGraph(center);

        double zoomFactor = zoomIn ? graphComponent.getZoomFactor() 
                : 1 / graphComponent.getZoomFactor();

        Rectangle rect = graphComponent.getGraphControl().getVisibleRect();

        rect.setLocation((int) (rect.getX() * zoomFactor),
                (int) (rect.getY() * zoomFactor));

        graphComponent.getGraphControl().scrollRectToVisible(rect);
    }
}
